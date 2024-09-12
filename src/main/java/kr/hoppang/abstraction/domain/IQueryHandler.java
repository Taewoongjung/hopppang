package kr.hoppang.abstraction.domain;

public interface IQueryHandler<Query extends IQuery, ResponseDto> {

    boolean isQueryHandler();

    ResponseDto handle(final Query query);

}
