package sample;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Checker extends StackPane {

    private CheckerType type;
    private Circle circle = new Circle(25, 25, Main.CELL_SIZE * 0.3);


    public CheckerType getType() {
        return type;
    }

    public void removeTypeR() {
        type = CheckerType.REDQUEEN;
        circle.setFill(Color.valueOf("#8b0000"));
    }


    public void removeTypeW() {
        type = CheckerType.WHITEQUEEN;
        circle.setFill(Color.valueOf("#808080"));
    }


    public Checker(CheckerType type, int x, int y) {
        this.type = type;

        relocate(x*Main.CELL_SIZE, y* Main.CELL_SIZE);

        if (type == CheckerType.RED) {
            circle.setFill(Color.RED);
        } else if (type == CheckerType.WHITE) {
            circle.setFill(Color.WHITE);
        }


        circle.setTranslateX((Main.CELL_SIZE - Main.CELL_SIZE * 0.3 * 2) / 2);
        circle.setTranslateY((Main.CELL_SIZE - Main.CELL_SIZE * 0.3 * 2) / 2);

        getChildren().addAll(circle);
    }

}
