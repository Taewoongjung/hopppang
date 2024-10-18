package kr.hoppang.application.readmodel.user.queries;

import kr.hoppang.abstraction.domain.IQuery;

public record ValidationCheckOfPhoneNumberQuery(String targetPhoneNumber,
                                                String compTargetNumber)
        implements IQuery {

}