package kr.hoppang.adapter.inbound.user.customer.webdto;

public class UpdateUserConfigWebDtoV1 {

    public record Req(
            boolean isPushOn,
            Boolean isAlimTalkOn
    ) {


    }

}
