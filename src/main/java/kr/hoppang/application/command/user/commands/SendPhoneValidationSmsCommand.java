package kr.hoppang.application.command.user.commands;

import kr.hoppang.abstraction.domain.ICommand;
import kr.hoppang.adapter.inbound.user.customer.webdto.ValidationType;

public record SendPhoneValidationSmsCommand(String email,
                                            String targetPhoneNumber,
                                            ValidationType validationType)
        implements ICommand {

}
