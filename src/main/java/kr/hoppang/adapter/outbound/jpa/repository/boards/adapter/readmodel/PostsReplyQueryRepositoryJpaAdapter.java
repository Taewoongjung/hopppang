package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter.readmodel;

import static kr.hoppang.adapter.outbound.jpa.entity.board.QPostsReplyEntity.postsReplyEntity;
import static kr.hoppang.adapter.outbound.jpa.entity.board.like.QPostsReplyLikeEntity.postsReplyLikeEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.hoppang.adapter.outbound.jpa.entity.board.PostsReplyEntity;
import kr.hoppang.adapter.outbound.jpa.repository.boards.PostsReplyJpaRepository;
import kr.hoppang.adapter.outbound.jpa.repository.boards.dto.PostReplyCountDto;
import kr.hoppang.domain.boards.PostsReply;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
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
    private final PostsReplyJpaRepository postsReplyJpaRepository;


    @Override
    public BoardsRepositoryStrategy strategy() {
        return BoardsRepositoryStrategy.RDB;
    }

    @Override
    public List<PostsReply> findPostsReplyByPostId(final long postId) {
        List<PostsReplyEntity> replyEntityList = queryFactory.selectFrom(postsReplyEntity)
                .where(
                        resolvePostIdEquals(postId)
                )
                .orderBy(postsReplyEntity.createdAt.desc())
                .fetch();

        if (replyEntityList.isEmpty()) {
            return Collections.emptyList();
        }

        return replyEntityList.stream()
                .map(PostsReplyEntity::toPojo)
                .toList();
    }

    @Override
    public List<Long> findAllLikedReplyIdsByUserId(
            final List<Long> replyIds,
            final long userId
    ) {

        return queryFactory
                .select(postsReplyLikeEntity.id.postReplyId)
                .from(postsReplyLikeEntity)
                .where(
                        postsReplyLikeEntity.id.userId.eq(userId),
                        postsReplyLikeEntity.id.postReplyId.in(replyIds)
                )
                .fetch();
    }

    @Override
    public Map<Long, Long> findCountOfLikesByPostId(final List<Long> postIds) {
        List<PostReplyCountDto> countOfRepliesByPost = postsReplyJpaRepository.countRepliesGroupedByPostId(
                postIds);

        return countOfRepliesByPost.stream()
                .collect(
                        Collectors.toMap(
                                PostReplyCountDto::postId,
                                PostReplyCountDto::replyCount
                        )
                );
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
