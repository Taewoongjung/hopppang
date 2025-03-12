package kr.hoppang.domain.statistics.repository;

import kr.hoppang.domain.statistics.repository.dto.CreateUserTrafficSourceInfoRepositoryDto;

public interface UserTrafficSourceRepository {

    void createUserTrafficSourceInfo(final CreateUserTrafficSourceInfoRepositoryDto dto);

}
