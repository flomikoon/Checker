package sample;

import java.util.LinkedList;

public enum CheckerType {
    BLACK(1), WHITE(-1), BLACKQUEEN(1), WHITEQUEEN(-1);

    final int checkerType;

    CheckerType(int checkerType) {
        this.checkerType = checkerType;
    }

}