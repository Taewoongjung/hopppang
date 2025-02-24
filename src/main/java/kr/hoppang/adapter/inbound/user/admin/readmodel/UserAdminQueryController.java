package kr.hoppang.adapter.inbound.user.admin.readmodel;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.validation.Valid;
import kr.hoppang.adapter.inbound.user.admin.webdto.SearchAllAvailableUsersWebDtoV1;
import kr.hoppang.adapter.inbound.user.admin.webdto.SearchUserStatisticsWebDtoV1;
import kr.hoppang.application.readmodel.user.handlers.GetAllUsersQueryHandler;
import kr.hoppang.application.readmodel.user.handlers.GetStatisticsOfUserQueryHandler;
import kr.hoppang.application.readmodel.user.queries.GetAllUsersQuery;
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

    private final GetAllUsersQueryHandler getAllUsersQueryHandler;
    private final GetStatisticsOfUserQueryHandler getStatisticsOfUserQueryHandler;

    @GetMapping(
            value = "",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SearchAllAvailableUsersWebDtoV1.Res> searchAllAvailableUsers(
            @Valid SearchAllAvailableUsersWebDtoV1.Req request
    ) {

        GetAllUsersQuery.Response response = getAllUsersQueryHandler.handle(
                GetAllUsersQuery.Request.builder()
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
            value = "/statistics",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SearchUserStatisticsWebDtoV1.Response> searchUserStatistics(
            @Valid SearchUserStatisticsWebDtoV1.Request request
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        null
                );
    }
}
