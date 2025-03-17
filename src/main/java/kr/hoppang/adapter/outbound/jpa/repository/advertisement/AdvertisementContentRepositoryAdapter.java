package kr.hoppang.adapter.outbound.jpa.repository.advertisement;

import static com.querydsl.core.types.Projections.constructor;
import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_ADVERTISEMENT_CONTENT;
import static kr.hoppang.adapter.common.util.CheckUtil.check;
import static kr.hoppang.adapter.outbound.jpa.entity.advertisement.QAdvertisementContentEntity.advertisementContentEntity;
import static kr.hoppang.adapter.outbound.jpa.entity.statistics.QUserTrafficSourceEntity.userTrafficSourceEntity;
import static kr.hoppang.adapter.outbound.jpa.entity.user.QUserEntity.userEntity;
import static kr.hoppang.util.converter.advertisement.AdvertisementConverter.advertisementContentToEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.advertisement.AdvertisementContentEntity;
import kr.hoppang.adapter.outbound.jpa.repository.advertisement.dto.GetAdvertisementContentListRepositoryDto;
import kr.hoppang.adapter.outbound.jpa.repository.advertisement.dto.GetAdvertisementTrafficListRepositoryDto;
import kr.hoppang.domain.advertisement.AdvertisementContent;
import kr.hoppang.domain.advertisement.repository.AdvertisementContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdvertisementContentRepositoryAdapter implements AdvertisementContentRepository {

    private final JPAQueryFactory queryFactory;
    private final AdvertisementContentJpaRepository advertisementContentJpaRepository;


    @Override
    public AdvertisementContent getAdvertisementContent(final String advId) {

        AdvertisementContentEntity advertisementContentEntity = advertisementContentJpaRepository.findByAdvId(
                advId);

        check(advertisementContentEntity == null, NOT_EXIST_ADVERTISEMENT_CONTENT);

        return advertisementContentEntity.toPojo();
    }

    @Override
    public void createAdvertisementContent(final AdvertisementContent advertisementContent) {
        advertisementContentJpaRepository.save(
                advertisementContentToEntity(advertisementContent)
        );
    }

    @Override
    public List<GetAdvertisementContentListRepositoryDto.Res> getAdvertisementContentList(
            final GetAdvertisementContentListRepositoryDto.Req repositoryReq
    ) {

        return queryFactory
                .select(
                        constructor(
                                GetAdvertisementContentListRepositoryDto.Res.class,
                                advertisementContentEntity.id,
                                advertisementContentEntity.advChannel,
                                advertisementContentEntity.startAt,
                                advertisementContentEntity.endAt,
                                advertisementContentEntity.publisherId,
                                userEntity.name,
                                advertisementContentEntity.memo
                        )
                )
                .from(advertisementContentEntity)
                .innerJoin(userEntity)
                .on(userEntity.id.eq(advertisementContentEntity.publisherId))
                .where(
                        resolveIsOnAir(repositoryReq.isOnAir()).and(
                                advertisementContentEntity.startAt.isNotNull()
                        ),
                        resolveAdvChannel(repositoryReq.advChannel()),
                        resolveAdvIdList(repositoryReq.advIdList())
                )
                .limit(repositoryReq.limit())
                .offset(repositoryReq.offset())
                .fetch();
    }

    private BooleanExpression resolveIsOnAir(final Boolean isOnAir) {
        if (isOnAir == null || !isOnAir) {
            return advertisementContentEntity.endAt.isNotNull();
        }
        return advertisementContentEntity.endAt.isNull();
    }

    private BooleanExpression resolveAdvChannel(final String advChannel) {
        if (advChannel == null) {
            return null;
        }

        return advertisementContentEntity.advChannel.eq(advChannel);
    }

    private BooleanExpression resolveAdvIdList(final List<String> advIdList) {
        if (advIdList == null || advIdList.isEmpty()) {
            return null;
        }

        return advertisementContentEntity.advId.in(advIdList);
    }

    @Override
    public List<GetAdvertisementTrafficListRepositoryDto.Res> getAdvertisementTrafficList(
            final GetAdvertisementTrafficListRepositoryDto.Req repositoryReq) {

        return queryFactory.select(
                        constructor(
                                GetAdvertisementTrafficListRepositoryDto.Res.class,
                                advertisementContentEntity.advId,
                                advertisementContentEntity.advChannel,
                                userTrafficSourceEntity.entryPageType,
                                userTrafficSourceEntity.referrer,
                                userTrafficSourceEntity.browser,
                                userTrafficSourceEntity.stayDuration,
                                userTrafficSourceEntity.visitedAt
                        )
                )
                .from(userTrafficSourceEntity)
                .innerJoin(advertisementContentEntity)
                .on(userTrafficSourceEntity.advertisementContentId.eq(
                        advertisementContentEntity.id))
                .where(
                        resolveIsOnAir(repositoryReq.isOnAir()).and(
                                advertisementContentEntity.startAt.isNotNull()
                        ),
                        resolveAdvChannel(repositoryReq.advChannel()),
                        resolveAdvIdList(repositoryReq.advIdList())
                )
                .fetch();
    }
}
