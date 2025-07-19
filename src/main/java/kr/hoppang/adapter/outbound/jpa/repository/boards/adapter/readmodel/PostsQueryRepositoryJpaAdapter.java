package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter.readmodel;

import static kr.hoppang.adapter.outbound.jpa.entity.board.QPostsEntity.postsEntity;
import static kr.hoppang.adapter.outbound.jpa.entity.board.bookmark.QPostsBookmarkEntity.postsBookmarkEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.board.PostsEntity;
import kr.hoppang.domain.boards.Posts;
import kr.hoppang.domain.boards.repository.BoardsRepositoryStrategy;
import kr.hoppang.domain.boards.repository.PostsQueryRepository;
import kr.hoppang.domain.boards.repository.dto.ConditionOfFindPosts;
import kr.hoppang.util.common.BoolType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsQueryRepositoryJpaAdapter implements PostsQueryRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public BoardsRepositoryStrategy strategy() {
        return BoardsRepositoryStrategy.RDB;
    }

    @Override
    public List<Posts> findAllPostsByCondition(final ConditionOfFindPosts condition) {

        List<PostsEntity> contents = queryFactory.selectFrom(postsEntity)
                .where(
                        notDeleted(),
                        resolveOnlyBookmarked(condition.bookmarkOnly(), condition.userId()),
                        resolveUserIdEquals(condition.userId(), condition.bookmarkOnly()),
                        resolveSearchWordLike(condition.searchWord()),
                        resolveBoardIn(condition.boardIds())
                )
                .orderBy(postsEntity.createdAt.desc())
                .offset(condition.offset())
                .limit(condition.limit())
                .fetch();

        return contents.stream()
                .map(PostsEntity::toPojo)
                .toList();
    }

    @Override
    public long countAllPostsByCondition(final ConditionOfFindPosts condition) {
        Long count = queryFactory.select(postsEntity.count())
                .from(postsEntity)
                .where(
                        notDeleted(),
                        resolveBoardIn(condition.boardIds())
                )
                .fetchOne();

        return count == null ? 0 : count;
    }

    @Override
    public Posts findPostsByPostId(final long postId) {
        PostsEntity post = queryFactory.selectFrom(postsEntity)
                .where(
                        notDeleted(),
                        resolvePostsIdEquals(postId)
                )
                .fetchOne();

        return post == null ? null : post.toPojo();
    }

    @Override
    public List<Posts> findRecentFivePosts() {
        List<PostsEntity> posts = queryFactory.selectFrom(postsEntity)
                .where(
                        notDeleted()
                )
                .orderBy(postsEntity.createdAt.desc())
                .limit(5)
                .fetch();

        return posts.stream()
                .map(PostsEntity::toPojo)
                .toList();
    }

    private BooleanExpression resolveUserIdEquals(final Long userId, final Boolean bookmarkOnly) {
        if (userId == null) {
            return null;
        }

        // 북마크는 타인 게시글도 함께 조회 될 필요가 있음
        if (bookmarkOnly != null && bookmarkOnly) {
            return null;
        }

        return postsEntity.registerId.eq(userId);
    }

    private BooleanExpression resolveSearchWordLike(final String searchWord) {
        if (searchWord == null || searchWord.isBlank()) {
            return null;
        }

        return postsEntity.title.contains(searchWord);
    }

    private BooleanExpression resolveBoardIn(final List<Long> boardIds) {
        if (boardIds == null || boardIds.isEmpty()) {
            return null;
        }

        return postsEntity.boardId.in(boardIds);
    }

    private BooleanExpression resolvePostsIdEquals(final Long boardId) {
        if (boardId == null || boardId == 0) {
            return null;
        }

        return postsEntity.id.eq(boardId);
    }

    private BooleanExpression resolveOnlyBookmarked(
            final Boolean onlyBookmarked,
            final Long userId
    ) {
        if (onlyBookmarked == null || !onlyBookmarked || userId == null) {
            return null;
        }

        return postsEntity.id.in(
                com.querydsl.jpa.JPAExpressions.select(postsBookmarkEntity.id.postId)
                        .from(postsBookmarkEntity)
                        .where(postsBookmarkEntity.id.userId.eq(userId))
        );
    }

    private BooleanExpression notDeleted() {
        return postsEntity.isDeleted.eq(BoolType.F);
    }
}
