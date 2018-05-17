package sample;

public enum CheckerType {
    RED(1), WHITE(-1) , REDQUEEN(7) , WHITEQUEEN(-7);

    final int checkerType;

    CheckerType(int checkerType) {
        this.checkerType = checkerType;
    }
}