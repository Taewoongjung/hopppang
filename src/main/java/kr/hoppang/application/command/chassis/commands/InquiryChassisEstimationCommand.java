package kr.hoppang.application.command.chassis.commands;

import kr.hoppang.abstraction.domain.ICommand;

public record InquiryChassisEstimationCommand(long estimationId, long userId, String strategy) implements ICommand {

}
