package kr.hoppang.adapter.outbound.jpa.repository.chassis.estimation;

import static kr.hoppang.util.converter.chassis.estimation.ChassisEstimationConverter.chassisEstimationAddressToEntity;
import static kr.hoppang.util.converter.chassis.estimation.ChassisEstimationConverter.chassisEstimationInfoToEntity;
import static kr.hoppang.util.converter.chassis.estimation.ChassisEstimationConverter.chassisEstimationSizeInfoToEntity;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationInfoEntity;
import kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.ChassisEstimationSizeInfoEntity;
import kr.hoppang.domain.chassis.ChassisType;
import kr.hoppang.domain.chassis.CompanyType;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationInfo;
import kr.hoppang.domain.chassis.estimation.ChassisEstimationSizeInfo;
import kr.hoppang.domain.chassis.estimation.repository.ChassisEstimationRepository;
import kr.hoppang.domain.chassis.estimation.repository.dto.FindChassisEstimationInfosResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.QChassisEstimationInfoEntity.chassisEstimationInfoEntity;
import static kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.QChassisEstimationAddressEntity.chassisEstimationAddressEntity;
import static kr.hoppang.adapter.outbound.jpa.entity.chassis.estimation.QChassisEstimationSizeInfoEntity.chassisEstimationSizeInfoEntity;

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

        // 조회 결과 순서 정의 start
        List<OrderSpecifier> sortOrder = new ArrayList<>();
        sortOrder.add(chassisEstimationInfoEntity.createdAt.desc());
        // 조회 결과 순서 정의 end

        // 조회
        return queryFactory.select(
                Projections.constructor(
                        (FindChassisEstimationInfosResult.class),
                        chassisEstimationInfoEntity.id,
                        chassisEstimationSizeInfoEntity.chassisType,
                        chassisEstimationSizeInfoEntity.width,
                        chassisEstimationSizeInfoEntity.height,
                        chassisEstimationSizeInfoEntity.price,
                        chassisEstimationInfoEntity.userId,
                        chassisEstimationInfoEntity.companyType,
                        chassisEstimationInfoEntity.laborFee,
                        chassisEstimationInfoEntity.ladderCarFee,
                        chassisEstimationInfoEntity.demolitionFee,
                        chassisEstimationInfoEntity.maintenanceFee,
                        chassisEstimationInfoEntity.freightTransportFee,
                        chassisEstimationInfoEntity.deliveryFee,
                        chassisEstimationInfoEntity.totalPrice,
                        chassisEstimationAddressEntity.address,
                        chassisEstimationAddressEntity.subAddress,
                        chassisEstimationInfoEntity.createdAt,
                        chassisEstimationInfoEntity.lastModified
        ))
                .from(chassisEstimationInfoEntity)
                .join(chassisEstimationInfoEntity.chassisEstimationSizeInfoList, chassisEstimationSizeInfoEntity)
                .join(chassisEstimationInfoEntity.chassisEstimationAddress, chassisEstimationAddressEntity)
                .where(whereClause)
                .orderBy(sortOrder.toArray(new OrderSpecifier<?>[0]))
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
}
