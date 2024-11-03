package kr.hoppang.application.command.user.handlers;

import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.user.commands.WithdrawUserCommand;
import kr.hoppang.application.command.user.oauth.OAuthServiceAdapter;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawUserCommandHandler implements ICommandHandler<WithdrawUserCommand, Boolean> {

    private final UserRepository userRepository;
    private final OAuthServiceAdapter oAuthServiceAdapter;


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

        userRepository.softDeleteUser(command.userId());

        return true;
    }
}
