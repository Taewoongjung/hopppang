package kr.hoppang.adapter.outbound.jpa.repository.statistics;

import kr.hoppang.adapter.outbound.jpa.entity.statistics.UserTrafficSourceEntity;
import kr.hoppang.domain.statistics.repository.UserTrafficSourceRepository;
import kr.hoppang.domain.statistics.repository.dto.CreateUserTrafficSourceInfoRepositoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserTrafficSourceRepositoryAdapter implements UserTrafficSourceRepository {

    private final UserTrafficSourceJpaRepository userTrafficSourceJpaRepository;


    @Override
    public void createUserTrafficSourceInfo(CreateUserTrafficSourceInfoRepositoryDto dto) {

        userTrafficSourceJpaRepository.save(
                UserTrafficSourceEntity.of(
                        dto.advertisementContentId(),
                        dto.entryPageType(),
                        dto.referrer(),
                        dto.browser(),
                        dto.stayDuration(),
                        dto.visitedAt()
                )
        );
    }
}
