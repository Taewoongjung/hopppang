package kr.hoppang.application.command.user.handlers;

import java.time.LocalDateTime;
import kr.hoppang.application.command.user.commands.AddUserLoginHistoryCommand;
import kr.hoppang.domain.user.UserLoginHistory;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddUserLoginHistoryCommandHandler {

    private final UserRepository userRepository;


    @Async
    @EventListener
    public void handle(final AddUserLoginHistoryCommand command) {
        userRepository.createUserLoginHistory(
                UserLoginHistory.of(
                        command.userId(),
                        LocalDateTime.now()
                )
        );
    }
}
