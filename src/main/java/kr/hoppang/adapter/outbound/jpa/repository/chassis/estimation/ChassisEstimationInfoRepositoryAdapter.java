package kr.hoppang.adapter.outbound.jpa.repository.chassis.estimation;

import static com.querydsl.core.types.ExpressionUtils.allOf;
import static com.querydsl.core.types.Projections.constructor;
import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_ESTIMATION;
import static kr.hoppang.adapter.common.util.CheckUtil.check;
import static kr.hoppang.util.converter.chassis.estimation.ChassisEstimationConverter.chassisEstimationAddressToEntity;
import static kr.hoppang.util.converter.chassis.estimation.ChassisEstimationConverter.chassisEstimationInfoToEntity;
import static kr.hoppang.util.converter.chassis.estimation.ChassisEstimationConverter.chassisEstimationSizeInfoToEntity;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationInfoEntity;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationSizeInfoEntity;
import kr.hoppang.adapter.outbound.jpa.repository.chassis.estimation.dto.FindChassisEstimationInfoByUserIdRepositoryDto;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationSizeInfo;
import kr.hoppang.domain.chassis.estimation.repository.ChassisEstimationRepository;
import kr.hoppang.domain.chassis.estimation.repository.dto.FindChassisEstimationInfosResult;
import kr.hoppang.domain.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.QChassisEstimationInfoEntity.chassisEstimationInfoEntity;
import static kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.QChassisEstimationAddressEntity.chassisEstimationAddressEntity;
import static kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.QChassisEstimationSizeInfoEntity.chassisEstimationSizeInfoEntity;
import static kr.hoppang.adapter.outbound.jpa.entity.user.QUserEntity.userEntity;


@Repository
@RequiredArgsConstructor
public class ChassisEstimationInfoRepositoryAdapter implements ChassisEstimationRepository {

    private final JPAQueryFactory queryFactory;
    private final ChassisEstimationInfoJpaRepository chassisEstimationJpaRepository;
    private final ChassisEstimationSizeInfoJpaRepository chassisEstimationSizeInfoJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<FindChassisEstimationInfosResult> findChassisEstimationInfosBy(
            final List<Long> estimationId,
            final CompanyType companyType,
            final ChassisType chassisType,
            final LocalDateTime startTime,
            final LocalDateTime endTime,
            final int limit, final int offset
    ) {

        // where 절 정의 start
        BooleanBuilder whereClause = new BooleanBuilder();

        if (startTime != null && endTime != null) {
            whereClause.and(chassisEstimationInfoEntity.createdAt.between(startTime, endTime));
        }

        if (estimationId != null) {
            whereClause.and(chassisEstimationInfoEntity.id.in(estimationId));
        }

        if (companyType != null) {
            whereClause.and(chassisEstimationInfoEntity.companyType.eq(companyType));
        }

        if (chassisType != null) {
            whereClause.and(chassisEstimationSizeInfoEntity.chassisType.eq(chassisType));
        }
        // where 절 정의 end

        // 조회
        return queryFactory.select(
                constructor(
                        (FindChassisEstimationInfosResult.class),
                        chassisEstimationInfoEntity.id,
                        chassisEstimationSizeInfoEntity.chassisType,
                        chassisEstimationSizeInfoEntity.width,
                        chassisEstimationSizeInfoEntity.height,
                        chassisEstimationSizeInfoEntity.price,
                        chassisEstimationInfoEntity.userId,
                        userEntity.name,
                        userEntity.email,
                        userEntity.tel,
                        chassisEstimationInfoEntity.companyType,
                        chassisEstimationInfoEntity.laborFee,
                        chassisEstimationInfoEntity.ladderCarFee,
                        chassisEstimationInfoEntity.demolitionFee,
                        chassisEstimationInfoEntity.maintenanceFee,
                        chassisEstimationInfoEntity.freightTransportFee,
                        chassisEstimationInfoEntity.deliveryFee,
                        chassisEstimationInfoEntity.totalPrice,
                        chassisEstimationAddressEntity.zipCode,
                        chassisEstimationAddressEntity.state,
                        chassisEstimationAddressEntity.city,
                        chassisEstimationAddressEntity.town,
                        chassisEstimationAddressEntity.remainAddress,
                        chassisEstimationAddressEntity.isApartment,
                        chassisEstimationAddressEntity.isExpanded,
                        chassisEstimationInfoEntity.createdAt,
                        chassisEstimationInfoEntity.lastModified
        ))
                .from(chassisEstimationInfoEntity)
                .join(chassisEstimationInfoEntity.chassisEstimationSizeInfoList, chassisEstimationSizeInfoEntity)
                .join(chassisEstimationInfoEntity.chassisEstimationAddress, chassisEstimationAddressEntity)
                .join(userEntity).on(chassisEstimationInfoEntity.userId.eq(userEntity.id))
                .where(whereClause)
                .limit(limit).offset(offset)
                .fetch();
    }

    @Override
    @Transactional
    public long registerChassisEstimation(
            final ChassisEstimationInfo chassisEstimationInfo,
            final List<ChassisEstimationSizeInfo> chassisEstimationSizeInfoList
    ) {

        // ChassisEstimationInfo 디비 저장 start
        ChassisEstimationInfoEntity chassisEstimationInfoEntity =
                chassisEstimationJpaRepository.save(
                        chassisEstimationInfoToEntity(chassisEstimationInfo));
        // ChassisEstimationInfo 디비 저장 end

        // address id 설정 start
        if (chassisEstimationInfo.getChassisEstimationAddress() != null) {
            // 샤시 견적 주소 객체에 id 설정
            chassisEstimationInfo.getChassisEstimationAddress()
                    .setChassisEstimationInfoId(chassisEstimationInfoEntity.getId());

            chassisEstimationInfoEntity.setChassisEstimationAddress(
                    chassisEstimationAddressToEntity(
                            chassisEstimationInfo.getChassisEstimationAddress()));
        }
        // address id 설정 end

        // ChassisEstimationSizeInfo 디비 저장 start
        List<ChassisEstimationSizeInfoEntity> chassisEstimationSizeInfoEntityList =
                chassisEstimationSizeInfoToEntity(chassisEstimationSizeInfoList, chassisEstimationInfoEntity.getId());

        chassisEstimationSizeInfoEntityList.forEach(
                e -> e.setChassisEstimationInfo(chassisEstimationInfoEntity));

        chassisEstimationSizeInfoJpaRepository.saveAll(chassisEstimationSizeInfoEntityList);
        // ChassisEstimationSizeInfo 디비 저장 end

        return chassisEstimationInfoEntity.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public ChassisEstimationInfo findChassisEstimationInfoById(final long estimationId) {

        ChassisEstimationInfoEntity entity = chassisEstimationJpaRepository.findById(estimationId)
                .orElse(null);

        check(entity == null, NOT_EXIST_ESTIMATION);

        return entity.toPojo();
    }

    @Override
    public FindChassisEstimationInfoByUserIdRepositoryDto.Response findChassisEstimationInfoByUserId(
            final long userId,
            final Pageable pageable,
            final Long lastEstimationId
    ) {

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(chassisEstimationInfoEntity.userId.eq(userId));

        if (lastEstimationId != null && lastEstimationId > 0) {
            whereClause.and(chassisEstimationInfoEntity.id.lt(lastEstimationId));
        }

        int pageSize = pageable.getPageSize();

        List<ChassisEstimationInfoEntity> results = queryFactory.selectFrom(chassisEstimationInfoEntity)
                .where(whereClause)
                .orderBy(chassisEstimationInfoEntity.id.desc())
                .limit(pageSize + 1)
                .fetch();

        Long lastId = null;
        boolean hasNextPage = results.size() > pageSize;
        if (hasNextPage) {
            lastId = results.get(pageSize).getId();
            results = results.subList(0, pageSize);
        }

        List<FindChassisEstimationInfoByUserIdRepositoryDto.Response.EstimationChassis> content = results.stream()
                .map(FindChassisEstimationInfoByUserIdRepositoryDto.Response::of)
                .toList();

        return FindChassisEstimationInfoByUserIdRepositoryDto.Response.builder()
                .estimationChassisList(new SliceImpl<>(content, pageable, hasNextPage))
                .lastEstimationId(lastId)
                .build();
    }

    @Override
    public long findAllEstimationsProvidedToCustomer() {
        Long count = queryFactory.select(chassisEstimationInfoEntity.count())
                .from(chassisEstimationInfoEntity)
                .innerJoin(userEntity)
                .on(chassisEstimationInfoEntity.userId.eq(userEntity.id))
                .where(
                        allOf(
                                userEntity.role.eq(UserRole.ROLE_CUSTOMER),
                                userEntity.tel.notIn(
                                        "01088257754", "01029143611"
                                ),
                                chassisEstimationInfoEntity.chassisEstimationAddress.state.ne("테스트 기간")
                        )
                )
                .fetchOne();

        return count == null ? 0 : count;
    }

    @Override
    public List<ChassisEstimationInfo> findAllRegisteredChassisEstimationsBetween(
            final LocalDateTime start, final LocalDateTime end) {

        List<ChassisEstimationInfoEntity> chassisEstimationInfoEntityList =
                queryFactory.select(chassisEstimationInfoEntity)
                        .from(chassisEstimationInfoEntity)
                        .innerJoin(userEntity)
                        .on(chassisEstimationInfoEntity.userId.eq(userEntity.id))
                        .where(
                                allOf(
                                        userEntity.role.eq(UserRole.ROLE_CUSTOMER),
                                        userEntity.tel.notIn(
                                                "01088257754", "01029143611"
                                        ),
                                        chassisEstimationInfoEntity.chassisEstimationAddress.state.ne(
                                                "테스트 기간"),
                                        chassisEstimationInfoEntity.createdAt.between(start, end)
                                )
                        )
                        .fetch();

        return chassisEstimationInfoEntityList.stream()
                .map(ChassisEstimationInfoEntity::toPojo)
                .toList();
    }

    @Override
    public List<ChassisEstimationInfo> findAll() {

        List<ChassisEstimationInfoEntity> chassisEstimationInfoEntityList =
                queryFactory.select(chassisEstimationInfoEntity)
                        .from(chassisEstimationInfoEntity)
                        .innerJoin(userEntity)
                        .on(chassisEstimationInfoEntity.userId.eq(userEntity.id))
                        .where(
                                allOf(
                                        userEntity.role.eq(UserRole.ROLE_CUSTOMER),
                                        userEntity.tel.notIn(
                                                "01088257754", "01029143611"
                                        ),
                                        chassisEstimationInfoEntity.chassisEstimationAddress.state.ne(
                                                "테스트 기간")
                                )
                        )
                        .fetch();

        return chassisEstimationInfoEntityList.stream()
                .map(ChassisEstimationInfoEntity::toPojo)
                .toList();
    }
}
