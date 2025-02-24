package kr.hoppang.adapter.inbound.user.customer.webdto;

public class LoadUserWebDtoV1 {

    public record Res(
            Long id,
            String email,
            String tel,
            String name,
            String role
    ) {

    }
}
