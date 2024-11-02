package kr.hoppang.application.readmodel.user.queries;

import kr.hoppang.abstraction.domain.IQuery;

public record GetUserConfigurationInfoQuery(long userId) implements IQuery {

}
