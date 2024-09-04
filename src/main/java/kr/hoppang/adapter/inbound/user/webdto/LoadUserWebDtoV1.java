package kr.hoppang.adapter.inbound.user.webdto;

public class LoadUserWebDtoV1 {

    public record Res(
            String email,
            String tel,
            String name,
            String role
    ) {

    }
}
