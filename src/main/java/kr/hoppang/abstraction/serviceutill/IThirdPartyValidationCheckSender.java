package kr.hoppang.abstraction.serviceutill;

public interface IThirdPartyValidationCheckSender {

    void sendValidationCheck(final String target) throws Exception;
}
