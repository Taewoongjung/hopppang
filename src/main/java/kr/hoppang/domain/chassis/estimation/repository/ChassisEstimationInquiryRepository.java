package kr.hoppang.domain.chassis.estimation.repository;

public interface ChassisEstimationInquiryRepository {

    void create(final long userId, final long estimationId, final String strategy);
}
