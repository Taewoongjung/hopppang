package kr.hoppang.application.command.user.commandresults;

public record OAuthLoginCommandResult(boolean isTheFirstLogIn,
                                      String jwt,
                                      String userEmail) {

}
