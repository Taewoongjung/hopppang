package kr.hoppang.adapter.inbound.boards.readmodel.webdto;

import java.util.List;
import kr.hoppang.application.readmodel.boards.queryresults.FindBoardsQueryResult;
import lombok.Builder;

public record GetAllBoardsWebDtoV1() {

    @Builder
    public record Res(
            long id,
            String name,
            List<Child> branchBoards
    ) {

        @Builder
        private record Child(
                long id,
                String name
        ) { }

        public static List<GetAllBoardsWebDtoV1.Res> of(
                final List<FindBoardsQueryResult> roots,
                final List<FindBoardsQueryResult> branches
        ) {
            return roots.stream()
                    .map(root ->
                            Res.builder()
                                    .id(root.id())
                                    .name(root.name())
                                    .branchBoards(
                                            branches.stream()
                                                    .filter(branch ->
                                                            branch.rootId().equals(root.id())
                                                    )
                                                    .map(branch ->
                                                            Child.builder()
                                                                    .id(branch.id())
                                                                    .name(branch.name())
                                                                    .build()
                                                    ).toList()
                                    )
                                    .build()
                    ).toList();
        }
    }
}
