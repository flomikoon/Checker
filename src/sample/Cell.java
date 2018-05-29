package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell extends Rectangle {

    private Checker checker;

    public boolean hasChecker() {
        return checker != null;
    }

    public Checker getChecker() {
        return checker;
    }

    public void setChecker(Checker checker) {
        this.checker = checker;
    }

    Cell(boolean white, int x, int y) {
        setWidth(Main.CELL_SIZE);
        setHeight(Main.CELL_SIZE);

        relocate(x * Main.CELL_SIZE, y * Main.CELL_SIZE);

        if (white){
            setFill(Color.WHITE);
        } else {
            setFill(Color.BLACK);
        }
    }
}
