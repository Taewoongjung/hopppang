package kr.hoppang.util.common;

public enum BoolType {
    T, F;

    public static BoolType convertBooleanToType(final boolean target) {

        if (!target) {
            return F;
        }

        return T;
    }
}
