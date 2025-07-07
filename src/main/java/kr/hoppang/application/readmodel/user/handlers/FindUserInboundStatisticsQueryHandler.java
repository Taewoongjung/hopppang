package kr.hoppang.application.readmodel.user.handlers;

import java.util.List;
import kr.hoppang.abstraction.domain.IQueryHandler;
import kr.hoppang.application.util.EmptyQuery;
import kr.hoppang.application.readmodel.user.queries.FindUserInboundStatisticsQuery;
import kr.hoppang.application.readmodel.user.queries.FindUserInboundStatisticsQuery.Res;
import kr.hoppang.domain.user.User;
import kr.hoppang.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindUserInboundStatisticsQueryHandler implements IQueryHandler<EmptyQuery, FindUserInboundStatisticsQuery.Res> {

    private final UserRepository userRepository;


    @Override
    public boolean isQueryHandler() {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Res handle(EmptyQuery query) {

        List<User> androidUsers = userRepository.findAllUsersByDeviceType("android");
        List<User> iosUsers = userRepository.findAllUsersByDeviceType("ios");

        int totalCount = androidUsers.size() + iosUsers.size();

        return Res.builder()
                .androidPercentile(
                        getPercentile(androidUsers.size(), totalCount)
                )
                .iosPercentile(
                        getPercentile(iosUsers.size(), totalCount)
                )
                .build();
    }

    private static String getPercentile(final int count, final long totalCount) {
        if (totalCount == 0) {
            return "0.0%";
        }

        double percentage = ((double) count / totalCount) * 100;
        return String.format("%.2f%%", percentage);
    }
}
