package kr.hoppang.application.command.user.handlers;

import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.user.commands.ReviseUserConfigurationCommand;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviseUserConfigurationCommandHandler implements ICommandHandler<ReviseUserConfigurationCommand, Boolean> {

    private final UserRepository userRepository;


    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional
    public Boolean handle(final ReviseUserConfigurationCommand command) {

        userRepository.updateUserConfiguration(command.userId(), command.isPushOn());

        return true;
    }
}
