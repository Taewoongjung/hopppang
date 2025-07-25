package kr.hoppang.adapter.inbound.user.admin.readmodel;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import kr.hoppang.adapter.inbound.chassis.admin.webdto.GetCountAllUsersWebDtoV1;
import kr.hoppang.adapter.inbound.user.admin.webdto.GetUserInboundStatistics;
import kr.hoppang.adapter.inbound.user.admin.webdto.SearchAllAvailableUsersWebDtoV1;
import kr.hoppang.adapter.inbound.user.admin.webdto.SearchUserStatisticsWebDtoV1;
import kr.hoppang.application.util.EmptyQuery;
import kr.hoppang.application.readmodel.user.handlers.FindAllUsersQueryHandler;
import kr.hoppang.application.readmodel.user.handlers.FindCountAllUsersQueryHandler;
import kr.hoppang.application.readmodel.user.handlers.FindStatisticsOfUserQueryHandler;
import kr.hoppang.application.readmodel.user.handlers.FindUserInboundStatisticsQueryHandler;
import kr.hoppang.application.readmodel.user.queries.FindAllUsersQuery;
import kr.hoppang.application.readmodel.user.queries.FindStatisticsOfUserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/admin/users")
public class UserAdminQueryController {

    private final FindAllUsersQueryHandler findAllUsersQueryHandler;
    private final FindCountAllUsersQueryHandler findCountAllUsersQueryHandler;
    private final FindStatisticsOfUserQueryHandler findStatisticsOfUserQueryHandler;
    private final FindUserInboundStatisticsQueryHandler findUserInboundStatisticsQueryHandler;

    @GetMapping(
            value = "",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SearchAllAvailableUsersWebDtoV1.Res> searchAllAvailableUsers(
            @Valid SearchAllAvailableUsersWebDtoV1.Req request
    ) {

        FindAllUsersQuery.Response response = findAllUsersQueryHandler.handle(
                FindAllUsersQuery.Request.builder()
                        .offset(request.offset())
                        .limit(request.limit())
                        .build()
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        SearchAllAvailableUsersWebDtoV1.Res.of(
                                response.userList(),
                                response.count()
                        )
                );
    }

    @GetMapping(
            value = "/all/count"
    )
    public ResponseEntity<GetCountAllUsersWebDtoV1.Res> getCountAllUsers() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        GetCountAllUsersWebDtoV1.Res.builder()
                                .count(
                                        findCountAllUsersQueryHandler.handle(
                                                EmptyQuery.builder().build()
                                        )
                                )
                                .build());
    }

    @GetMapping(
            value = "/statistics",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SearchUserStatisticsWebDtoV1.Response> getUserStatistics(
            @Valid SearchUserStatisticsWebDtoV1.Request request
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(SearchUserStatisticsWebDtoV1.Response.of(
                        findStatisticsOfUserQueryHandler.handle(
                                        FindStatisticsOfUserQuery.Request.builder()
                                                .searchPeriodType(request.searchPeriodType())
                                                .searchPeriodValue(request.searchPeriodValue())
                                                .now(LocalDateTime.now((ZoneId.of("Asia/Seoul"))))
                                                .build()
                                )
                        )
                );
    }

    @GetMapping(
            value = "/statistics/inbounds",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GetUserInboundStatistics.Res> getUserInboundStatistics() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        GetUserInboundStatistics.Res.of(
                                findUserInboundStatisticsQueryHandler.handle(
                                        EmptyQuery.builder().build())
                        )
                );
    }
}
