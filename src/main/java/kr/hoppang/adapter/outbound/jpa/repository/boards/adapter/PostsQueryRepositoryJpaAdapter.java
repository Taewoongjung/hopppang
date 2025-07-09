package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter;

import static kr.hoppang.adapter.outbound.jpa.entity.board.QPostsEntity.postsEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kr.hoppang.adapter.outbound.jpa.entity.board.PostsEntity;
import kr.hoppang.domain.boards.Posts;
import kr.hoppang.domain.boards.repository.PostsQueryRepository;
import kr.hoppang.domain.boards.repository.dto.ConditionOfFindPosts;
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
    public List<Posts> findAllPostsByCondition(final ConditionOfFindPosts condition) {

        List<PostsEntity> contents = queryFactory.selectFrom(postsEntity)
                .where(
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
                        resolveBoardIn(condition.boardIds())
                )
                .fetchOne();

        return count == null ? 0 : count;
    }

    @Override
    public Posts findPostsByPostId(final long postId) {
        PostsEntity post = queryFactory.selectFrom(postsEntity)
                .where(
                        resolvePostsIdEquals(postId)
                )
                .fetchOne();

        return post == null ? null : post.toPojo();
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
}
