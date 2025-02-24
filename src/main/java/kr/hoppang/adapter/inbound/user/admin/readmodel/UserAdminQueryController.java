package kr.hoppang.adapter.inbound.user.admin.readmodel;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import kr.hoppang.adapter.inbound.user.admin.webdto.SearchAllAvailableUsersWebDtoV1;
import kr.hoppang.application.readmodel.user.handlers.GetAllUsersQueryHandler;
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


    @GetMapping(
            value = "",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SearchAllAvailableUsersWebDtoV1.Response> searchAllAvailableUsers() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        SearchAllAvailableUsersWebDtoV1.Response.of(
                                getAllUsersQueryHandler.handle(GetAllUsersQuery.Request
                                        .builder()
                                        .build()
                                ).userList()
                        )
                );
    }
}
