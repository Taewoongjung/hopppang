package kr.hoppang.application.command.user.commands;

import kr.hoppang.abstraction.domain.ICommand;
import kr.hoppang.adapter.inbound.user.webdto.ValidationType;

public record SendPhoneValidationSmsCommand(String targetPhoneNumber,
                                            ValidationType validationType)
        implements ICommand {

}
