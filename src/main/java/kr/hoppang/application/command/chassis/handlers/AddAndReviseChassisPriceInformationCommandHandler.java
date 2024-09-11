package kr.hoppang.application.command.chassis.handlers;

import kr.hoppang.abstraction.domain.ICommandHandler;
import kr.hoppang.application.command.chassis.commands.AddAndReviseChassisPriceInformationCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddAndReviseChassisPriceInformationCommandHandler implements ICommandHandler<AddAndReviseChassisPriceInformationCommand, Boolean> {

    @Override
    public boolean isCommandHandler() {
        return true;
    }

    @Override
    public Boolean handle(final AddAndReviseChassisPriceInformationCommand event) {

        return null;
    }
}
