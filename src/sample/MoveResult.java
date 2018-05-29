package sample;

public class MoveResult {
    private MoveType type;

    public MoveType getType() {
        return type;
    }

    private Checker checker;

    public Checker getChecker() {
        return checker;
    }

    MoveResult(MoveType type) {
        this(type, null);
    }

    MoveResult(MoveType type, Checker checker) {
        this.type = type;
        this.checker = checker;
    }
}
