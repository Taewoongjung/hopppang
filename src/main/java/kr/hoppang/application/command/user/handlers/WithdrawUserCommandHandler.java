package kr.hoppang.application.command.user.handlers;

import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.adapter.outbound.cache.dto.TearDownBucketByKey;
import kr.hoppang.application.command.user.commands.WithdrawUserCommand;
import kr.hoppang.application.command.user.oauth.OAuthServiceAdapter;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawUserCommandHandler implements ICommandHandler<WithdrawUserCommand, Boolean> {

    private final UserRepository userRepository;
    private final OAuthServiceAdapter oAuthServiceAdapter;
    private final ApplicationEventPublisher eventPublisher;


    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional
    public Boolean handle(final WithdrawUserCommand command) {

        boolean isWithdrawSuccessFromSocialService = oAuthServiceAdapter.withdrawUser(
                command.oauthType(),
                command.userId());

        if (!isWithdrawSuccessFromSocialService) {
            return false;
        }

        String deletedUserEmail = userRepository.softDeleteUser(command.userId());

        // 탈퇴 한 회원은 유저 캐시에서 삭제
        eventPublisher.publishEvent(new TearDownBucketByKey("users::" + deletedUserEmail));

        return true;
    }
}
