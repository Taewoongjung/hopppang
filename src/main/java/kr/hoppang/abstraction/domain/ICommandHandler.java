package kr.hoppang.abstraction.domain;

public interface ICommandHandler<Command, CommandResult> {

    boolean isCommandHandler();

    CommandResult handle(final Command command) throws Exception;

}
