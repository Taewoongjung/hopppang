package kr.hoppang.application.command.user.handlers;

import java.time.LocalDateTime;
import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.user.commands.SignUpCommand;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpCommandHandler implements ICommandHandler<SignUpCommand, String> {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String handle(final SignUpCommand event) {
        log.info("execute SignUpCommand = {}", event);

        userRepository.checkIfExistUserByEmail(event.email());

        User user = User.of(
                event.name(), event.email(), bCryptPasswordEncoder.encode(event.password()),
                event.tel(),
                event.role(),
                LocalDateTime.now(), LocalDateTime.now());

        User newUser = userRepository.save(user);

        if (newUser != null) {

//            check(!cacheSmsValidationRedisRepository.getIsVerifiedByKey(event.tel()),
//                    NOT_VERIFIED_PHONE);

            // 새로운 유저 회원가입 시 해당 유저에게 알림톡 발송
//            eventPublisher.publishEvent(
//                    new SendAlimTalk(null, null, KakaoMsgTemplateType.COMPLETE_SIGNUP, event.tel(),
//                            event.name(), null, null, null));

            log.info("SignUpCommand executed successfully");

            return newUser.getName();
        }

        return null;
    }
}
