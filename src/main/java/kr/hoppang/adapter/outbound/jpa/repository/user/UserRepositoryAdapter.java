package kr.hoppang.adapter.outbound.jpa.repository.user;

import static kr.hoppang.adapter.common.exception.ErrorType.INVALID_SIGNUP_REQUEST_DUPLICATE_EMAIL;
import static kr.hoppang.adapter.common.exception.ErrorType.NOT_EXIST_USER;
import static kr.hoppang.adapter.common.util.CheckUtil.check;
import static kr.hoppang.util.converter.user.UserEntityConverter.userToEntity;

import kr.hoppang.adapter.outbound.jpa.entity.user.UserEntity;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(final String email) {
        UserEntity entity = userJpaRepository.findByEmail(email);

        check(entity == null, NOT_EXIST_USER);

        return entity.toPojo();
    }

    @Override
    @Transactional(readOnly = true)
    public void checkIfExistUserByEmail(final String email) {

        check(userJpaRepository.existsByEmail(email), INVALID_SIGNUP_REQUEST_DUPLICATE_EMAIL);
    }

    @Override
    @Transactional
    public User save(final User user) {
        UserEntity entity = userJpaRepository.save(userToEntity(user));
        return entity.toPojoWithRelations();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(final Long id) {
        UserEntity entity = userJpaRepository.findById(id).orElse(null);

        check(entity == null, NOT_EXIST_USER);

        return entity.toPojo();
    }

    @Override
    @Transactional(readOnly = true)
    public User findByPhoneNumber(final String phoneNumber) {
        UserEntity entity = userJpaRepository.findByTel(phoneNumber);

        check(entity == null, NOT_EXIST_USER);

        return entity.toPojo();
    }
}
