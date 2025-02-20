package kr.hoppang.application.command.user.handlers;

import static kr.hoppang.adapter.common.util.CheckUtil.duplicatedSsoLoginCheck;

import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.abstraction.serviceutill.IThirdPartyValidationCheckSender;
import kr.hoppang.adapter.inbound.user.webdto.ValidationType;
import kr.hoppang.adapter.outbound.cache.dto.TearDownBucketByKey;
import kr.hoppang.application.command.user.commands.SendPhoneValidationSmsCommand;
import kr.hoppang.domain.user.OauthType;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SendPhoneValidationSmsCommandHandler implements
        ICommandHandler<SendPhoneValidationSmsCommand, Void> {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final IThirdPartyValidationCheckSender smsThirdPartyValidationCheckSender;

    public SendPhoneValidationSmsCommandHandler(
            final UserRepository userRepository,
            final ApplicationEventPublisher eventPublisher,
            @Qualifier("SmsUtilService") final IThirdPartyValidationCheckSender smsUtilService
    ) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.smsThirdPartyValidationCheckSender = smsUtilService;
    }

    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Void handle(final SendPhoneValidationSmsCommand event) throws Exception {
        log.info("execute SendPhoneValidationSmsCommand = {}", event);

        if (ValidationType.SIGN_UP.equals(event.validationType())) {
            // 존재하는 휴대폰 번호 인지 검증
            User user = userRepository.findByEmail(event.email());
            if (user != null && !user.getEmail().equals(event.email())) {
                // 이미 회원이 존재하면 갓 만들어진 회원 제거 start
                userRepository.deleteUser(event.email());
                eventPublisher.publishEvent(new TearDownBucketByKey("users::" + event.email()));
                log.info("중복으로 인한 유저 제거 완료 = {}", event.email());
                // 이미 회원이 존재하면 갓 만들어진 회원 제거 end

                // 이미 회원이 존재하면 에러를 던진다.
                duplicatedSsoLoginCheck(!OauthType.NON.equals(user.getOauthType()), user.getEmail(),
                        user.getOauthType());
            }
        }

        smsThirdPartyValidationCheckSender.sendValidationCheck(event.targetPhoneNumber());

        log.info("SendPhoneValidationSmsCommand executed successfully");

        return null;
    }
}
