package kr.hoppang.adapter.outbound.jpa.repository.alimtalk.adapter;

import static kr.hoppang.util.converter.alimtalk.AlimTalkResultConverter.toEntity;

import kr.hoppang.adapter.outbound.jpa.repository.alimtalk.AlimTalkResultJpaRepository;
import kr.hoppang.domain.alimtalk.AlimTalkResult;
import kr.hoppang.domain.alimtalk.repository.AlimTalkResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Repository
@RequiredArgsConstructor
public class AlimTalkResultRepositoryJpaAdapter implements AlimTalkResultRepository {

    private final AlimTalkResultJpaRepository alimTalkResultJpaRepository;


    @Override
    @Transactional
    public void save(final AlimTalkResult alimTalkResult) {

        alimTalkResultJpaRepository.save(toEntity(alimTalkResult));
    }
}
