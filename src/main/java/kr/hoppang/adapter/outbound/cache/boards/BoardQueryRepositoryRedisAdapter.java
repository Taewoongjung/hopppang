package kr.hoppang.adapter.outbound.cache.boards;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import kr.hoppang.domain.boards.Boards;
import kr.hoppang.domain.boards.repository.BoardsQueryRepository;
import kr.hoppang.domain.boards.repository.BoardsQueryStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BoardQueryRepositoryRedisAdapter implements BoardsQueryRepository {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    private final String DEFAULT_BOARD_KEY = "boards:";

    @Override
    public BoardsQueryStrategy getBoardsQueryStrategy() {
        return BoardsQueryStrategy.CACHE;
    }

    @Override
    public List<Boards> getAllBoards() {
        String key = DEFAULT_BOARD_KEY + "all";
        Long listSize = redisTemplate.opsForList().size(key);

        if (listSize == null || listSize == 0) {
            return Collections.emptyList();
        }

        List<String> boardsJsonList = redisTemplate.opsForList().range(key, 0, -1); // 전체 리스트

        if (boardsJsonList == null || boardsJsonList.isEmpty()) {
            return Collections.emptyList();
        }

        return boardsJsonList.stream()
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, Boards.class);
                    } catch (JsonProcessingException e) {
                        log.error("Failed to deserialize 'Boards' from Redis", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
