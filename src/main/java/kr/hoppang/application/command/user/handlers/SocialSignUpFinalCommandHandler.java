package kr.hoppang.application.command.user.handlers;

import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.user.commands.SocialSignUpFinalCommand;
import kr.hoppang.domain.user.UserAddress;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialSignUpFinalCommandHandler implements ICommandHandler<SocialSignUpFinalCommand, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    @Transactional
    public String handle(final SocialSignUpFinalCommand command) {

        UserAddress userAddress = UserAddress.of(command.address(), command.subAddress(),
                command.buildingNumber());

        return userRepository.updatePhoneNumberAndAddressAndPush(command.userEmail(),
                command.userPhoneNumber(), userAddress, command.isPushOn());
    }
}
