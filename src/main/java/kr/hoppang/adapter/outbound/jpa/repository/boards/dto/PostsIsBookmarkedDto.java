package kr.hoppang.adapter.outbound.jpa.repository.boards.dto;

public record PostsIsBookmarkedDto(
        Long postId,
        Long bookmarkedCount //JPQL 한계 때문에 > 연산자 사용 못하기 때문에 일단 Long 으로 카운트를 받아서 0 이상이면 true 인걸로 판별함
) { }
