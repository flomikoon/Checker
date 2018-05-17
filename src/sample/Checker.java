package sample;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Checker extends StackPane {

    private CheckerType type;
    private Circle circle = new Circle(25 , 25, Main.CELL_SIZE * 0.3);
    private double mouseX, mouseY;
    private double oldX, oldY;

    public CheckerType getType() {
        return type;
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public void removeTypeR(){
        type = CheckerType.REDQUEEN;
        circle.setFill(Color.valueOf("#8b0000"));
    }


    public void removeTypeW(){
        type = CheckerType.WHITEQUEEN;
        circle.setFill(Color.valueOf("#808080"));
    }


    public Checker(CheckerType type, int x, int y) {
        this.type = type;

        go(x, y);

        if (type == CheckerType.RED) {
            circle.setFill(Color.RED);
        } else if(type == CheckerType.WHITE){
            circle.setFill(Color.WHITE);
        }


        circle.setTranslateX((Main.CELL_SIZE - Main.CELL_SIZE * 0.3 * 2) / 2);
        circle.setTranslateY((Main.CELL_SIZE - Main.CELL_SIZE * 0.3 * 2) / 2);

        getChildren().addAll(circle);

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        setOnMouseDragged((MouseEvent e) -> {
            relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
        });
    }

    public void go(int x, int y) {
        oldX = x * Main.CELL_SIZE;
        oldY = y * Main.CELL_SIZE;
        relocate(oldX, oldY);
    }

    public void noMove() {
        relocate(oldX, oldY);
    }
}
