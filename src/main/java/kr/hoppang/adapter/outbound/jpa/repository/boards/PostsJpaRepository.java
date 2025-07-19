package kr.hoppang.adapter.outbound.jpa.repository.boards;

import io.lettuce.core.dynamic.annotation.Param;
import kr.hoppang.adapter.outbound.jpa.entity.board.PostsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostsJpaRepository extends JpaRepository<PostsEntity, Long> {

    @Modifying
    @Query("UPDATE PostsEntity p SET p.isDeleted = 'T' WHERE p.id = :postId")
    void reviseAsDeletedById(@Param("postId") final long postId);
}
