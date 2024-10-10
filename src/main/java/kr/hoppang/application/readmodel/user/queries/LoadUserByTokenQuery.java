package kr.hoppang.application.readmodel.user.queries;

import kr.hoppang.abstraction.domain.IQuery;

public record LoadUserByTokenQuery(String authToken, boolean isAdminPage) implements IQuery {

}
