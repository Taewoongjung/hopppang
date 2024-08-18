package kr.hoppang.domain.user.repository;

import kr.hoppang.domain.user.User;

public interface UserRepository {

    User findByEmail(final String email);

    void checkIfExistUserByEmail(final String email);

    User save(final User user);

    User findById(final Long id);

    User findByPhoneNumber(final String phoneNumber);
}
