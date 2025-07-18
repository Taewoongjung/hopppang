package kr.hoppang.adapter.outbound.jpa.repository.boards;

import kr.hoppang.adapter.outbound.jpa.entity.board.bookmark.PostsBookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsBookmarkJpaRepository extends JpaRepository<PostsBookmarkEntity, Long> {

}
