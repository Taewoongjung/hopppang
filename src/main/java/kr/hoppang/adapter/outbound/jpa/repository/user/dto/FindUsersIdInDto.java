package kr.hoppang.adapter.outbound.jpa.repository.user.dto;

import java.time.LocalDateTime;

public record FindUsersIdInDto(
        Long id,
        String name,
        LocalDateTime deletedAt
) { }
