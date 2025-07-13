package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter;

import static kr.hoppang.adapter.outbound.jpa.entity.board.QPostsReplyEntity.postsReplyEntity;
import static kr.hoppang.adapter.outbound.jpa.entity.board.like.QPostsReplyLikeEntity.postsReplyLikeEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.board.PostsReplyEntity;
import kr.hoppang.domain.boards.PostsReply;
import kr.hoppang.domain.boards.repository.PostsReplyQueryRepository;
import kr.hoppang.util.common.BoolType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsReplyQueryRepositoryJpaAdapter implements PostsReplyQueryRepository{

    private final JPAQueryFactory queryFactory;


    @Override
    public List<PostsReply> findPostsReplyByPostId(final long postId, final Long loggedInUserId) {
        List<PostsReplyEntity> replyEntityList = queryFactory.selectFrom(postsReplyEntity)
                .where(
                        resolvePostIdEquals(postId),
                        resolveNotDeleted()
                )
                .orderBy(postsReplyEntity.createdAt.desc())
                .fetch();

        if (replyEntityList.isEmpty()) {
            return Collections.emptyList();
        }

        if (loggedInUserId != null) {
            // 댓글 ID 목록 추출
            List<Long> replyIds = replyEntityList.stream()
                    .map(PostsReplyEntity::getId)
                    .toList();

            // 좋아요 테이블에서 유저가 누른 댓글 ID 조회
            List<Long> likedReplyIds = queryFactory
                    .select(postsReplyLikeEntity.id.postReplyId)
                    .from(postsReplyLikeEntity)
                    .where(
                            postsReplyLikeEntity.id.userId.eq(loggedInUserId),
                            postsReplyLikeEntity.id.postReplyId.in(replyIds)
                    )
                    .fetch();

            replyEntityList.stream()
                    .filter(f -> likedReplyIds.contains(f.getId()))
                    .forEach(PostsReplyEntity::iLiked);
        }

        return replyEntityList.stream()
                .map(PostsReplyEntity::toPojo)
                .toList();
    }

    private BooleanExpression resolvePostIdEquals(final Long postId) {
        if (postId == null) {
            return null;
        }

        return postsReplyEntity.postId.eq(postId);
    }

    private BooleanExpression resolveNotDeleted() {
        return postsReplyEntity.isDeleted.eq(BoolType.F);
    }
}
