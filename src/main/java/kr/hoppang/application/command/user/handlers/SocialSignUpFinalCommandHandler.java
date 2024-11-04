package kr.hoppang.application.command.user.handlers;

import static kr.hoppang.adapter.common.exception.ErrorType.FAIL_WHILE_LOGIN;
import static kr.hoppang.adapter.common.exception.ErrorType.NOT_VERIFIED_PHONE;
import static kr.hoppang.adapter.common.util.CheckUtil.check;

import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.adapter.common.exception.custom.HoppangLoginException;
import kr.hoppang.adapter.outbound.alarm.dto.NewUser;
import kr.hoppang.adapter.outbound.cache.dto.TearDownBucketByKey;
import kr.hoppang.adapter.outbound.cache.sms.CacheSmsValidationRedisRepository;
import kr.hoppang.application.command.user.commands.SocialSignUpFinalCommand;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.UserAddress;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialSignUpFinalCommandHandler implements ICommandHandler<SocialSignUpFinalCommand, String> {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CacheSmsValidationRedisRepository cacheSmsValidationRedisRepository;


    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String handle(final SocialSignUpFinalCommand command) {

        UserAddress userAddress = UserAddress.of(command.address(), command.subAddress(),
                command.buildingNumber());

        User updatedUser = userRepository.updatePhoneNumberAndAddressAndPush(command.userEmail(),
                command.userPhoneNumber(), userAddress, command.isPushOn());

        if (updatedUser != null) {
            // 휴대폰 검증이 되었는지 확인
            check(!cacheSmsValidationRedisRepository.getIsVerifiedByKey(updatedUser.getTel()),
                    NOT_VERIFIED_PHONE);

            // 검증 후 해당 버킷 삭제
            eventPublisher.publishEvent(new TearDownBucketByKey(updatedUser.getTel()));

            // 새로운 유저 회원가입 시 알람 발송
            eventPublisher.publishEvent(
                    new NewUser(updatedUser.getName(), updatedUser.getEmail(), updatedUser.getTel(),
                            updatedUser.getOauthType(), updatedUser.getCreatedAt()));

            return updatedUser.getName();
        }

        throw new HoppangLoginException(FAIL_WHILE_LOGIN);
    }
}
