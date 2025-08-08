package kr.hoppang.domain.boards;

import kr.hoppang.domain.user.User;
import kr.hoppang.util.common.BoolType;

public class BoardUtil {

    public static String getAuthorName(final User author, final BoolType isAnonymous) {
        if (author == null) {
            return "알수없음";
        }

        if (author.isDeleted()) {
            return "탈퇴유저";
        }

        if (BoolType.T.equals(isAnonymous)) {
            return "익명";
        }

        return author.getName();
    }
}
