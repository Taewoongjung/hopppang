package kr.hoppang.adapter.inbound.user.webdto;

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
