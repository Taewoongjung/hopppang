package kr.hoppang.adapter.outbound.jpa.repository.boards.adapter;

import static kr.hoppang.adapter.outbound.jpa.entity.board.QPostsReplyEntity.postsReplyEntity;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    public List<PostsReply> findPostsReplyByPostId(final Long postId) {
        List<PostsReplyEntity> replyEntityList = queryFactory.selectFrom(postsReplyEntity)
                .where(
                        resolvePostIdEquals(postId),
                        resolveNotDeleted()
                )
                .orderBy(postsReplyEntity.createdAt.desc())
                .fetch();

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
