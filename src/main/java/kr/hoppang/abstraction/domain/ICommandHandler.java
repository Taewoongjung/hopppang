package kr.hoppang.abstraction.domain;

public interface ICommandHandler<Command extends ICommand, CommandResult> {

    boolean isCommandHandler();

    CommandResult handle(final Command command);

}
