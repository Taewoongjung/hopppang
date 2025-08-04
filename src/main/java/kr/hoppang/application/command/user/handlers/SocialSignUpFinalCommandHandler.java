package kr.hoppang.application.command.user.handlers;

import static kr.hoppang.adapter.common.exception.ErrorType.FAIL_WHILE_LOGIN;
import static kr.hoppang.adapter.common.exception.ErrorType.NOT_VERIFIED_PHONE;
import static kr.hoppang.adapter.common.util.CheckUtil.check;
import static kr.hoppang.domain.alimtalk.AlimTalkTemplateType.COMPLETE_SIGNUP;

import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.adapter.common.exception.custom.HoppangLoginException;
import kr.hoppang.adapter.outbound.cache.dto.TearDownBucketByKey;
import kr.hoppang.adapter.outbound.cache.sms.CacheSmsValidationRedisRepository;
import kr.hoppang.adapter.outbound.cache.user.CacheUserRedisRepository;
import kr.hoppang.application.command.alimtalk.event.events.SignUpWelcomeAlimTalkSendEvent;
import kr.hoppang.application.command.user.commands.SocialSignUpFinalCommand;
import kr.hoppang.domain.user.User;
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
    private final CacheUserRedisRepository cacheUserRedisRepository; // @TODO 해당 핸들러가 성공 하기 전에 리프레시 명목으로 해당 email 유저의 캐시를 제거한다.
    private final CacheSmsValidationRedisRepository cacheSmsValidationRedisRepository;


    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String handle(final SocialSignUpFinalCommand command) {

        User updatedUser = userRepository.userConfiguration(command.userEmail(),
                command.userPhoneNumber(), command.isPushOn(),

                // @TODO 임시로 해놓고 프론트 완전히 v2 로 변경 되면 command.isAlimTalkOn() 로 다시 수정
                command.isAlimTalkOn() != null && command.isAlimTalkOn()
        );

        if (updatedUser != null) {
            // 휴대폰 검증이 되었는지 확인
            check(!cacheSmsValidationRedisRepository.getIsVerifiedByKey(updatedUser.getTel()),
                    NOT_VERIFIED_PHONE);

            // 검증 후 해당 버킷 삭제
            eventPublisher.publishEvent(new TearDownBucketByKey(updatedUser.getTel()));

            // 마지막 회원가입 프로세스 완료 후 새로고침 하기 위한 기존 버킷 삭제
            cacheUserRedisRepository.deleteBucketByKey(updatedUser.getEmail());

            // 회원가입 알림톡 발송
            eventPublisher.publishEvent(
                    SignUpWelcomeAlimTalkSendEvent.builder()
                            .receiverPhoneNumber(command.userPhoneNumber())
                            .templateCode(COMPLETE_SIGNUP)
                            .build()
            );

            return updatedUser.getName();
        }

        throw new HoppangLoginException(FAIL_WHILE_LOGIN);
    }
}
