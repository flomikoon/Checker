package sample;

import java.util.LinkedList;

public enum CheckerType {
    BLACK(1), WHITE(-1), BLACKQUEEN(7), WHITEQUEEN(-7);

    final int checkerType;

    CheckerType(int checkerType) {
        this.checkerType = checkerType;
    }

}