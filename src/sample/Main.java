package sample;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.stage.*;


public class Main extends Application {

    public static final int CELL_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private Cell[][] board = new Cell[WIDTH][HEIGHT];

    private Group cellGroup = new Group();
    private Group checkerGroup = new Group();

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE);
        root.getChildren().addAll(cellGroup, checkerGroup);

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Cell cell = new Cell((x + y) % 2 == 0, x, y);
                board[x][y] = cell;

                cellGroup.getChildren().add(cell);

                Checker checker = null;

                if (y <= 2 && (x + y) % 2 != 0) {
                    checker = makeCheker(CheckerType.RED, x, y);
                }

                if (y >= 5 && (x + y) % 2 != 0) {
                    checker = makeCheker(CheckerType.WHITE, x, y);
                }

                if (checker != null) {
                    cell.setChecker(checker);
                    checkerGroup.getChildren().add(checker);
                }
            }
        }

        return root;
    }

    private MoveResult tryMove(Checker checker, int newX, int newY) {
        if (board[newX][newY].hasChecker() || (newX + newY) % 2 == 0) {
            return new MoveResult(MoveType.NO);
        }

        int x0 = toBoard(checker.getOldX());
        int y0 = toBoard(checker.getOldY());

        int l = newX;
        int p = newY;
        int count = 0;
        int countcr = 0;

        boolean dam = (board[x0][y0].getChecker().getType() == CheckerType.REDQUEEN
                || board[x0][y0].getChecker().getType() == CheckerType.WHITEQUEEN)
                && (Math.abs(x0 - newX) == Math.abs(y0 - newY));

        if (Math.abs(newX - x0) == 1 && newY - y0 == checker.getType().checkerType) {
            return new MoveResult(MoveType.YES);
        } else if (Math.abs(newX - x0) == 2 && Math.abs(newY - y0) == Math.abs(checker.getType().checkerType * 2)) {

            int x1 = x0 + (newX - x0) / 2;
            int y1 = y0 + (newY - y0) / 2;

            if (board[x1][y1].hasChecker() && board[x1][y1].getChecker().getType() != checker.getType()) {
                return new MoveResult(MoveType.KILL, board[x1][y1].getChecker());
            }
        } else if (dam){

            while (p != toBoard(checker.getOldY()) && l != toBoard(checker.getOldX())){
                if (board[l][p].hasChecker() && board[l][p].getChecker().getType() != checker.getType() && (Math.abs(l - newX) == Math.abs(p - newY))) {
                    count++;
                    x0 = l;
                    y0 = p;
                }
                if ((checker.getType() == CheckerType.REDQUEEN && board[l][p].hasChecker() && board[l][p].getChecker().getType() == CheckerType.RED )||
                       (checker.getType() == CheckerType.WHITEQUEEN && board[l][p].hasChecker() && board[l][p].getChecker().getType() == CheckerType.WHITE)){
                    countcr++;
                }

                if (newX < toBoard(checker.getOldX()) && newY < toBoard(checker.getOldY())  ) {
                    p++;
                    l++;
                } else if(newX < toBoard(checker.getOldX()) && newY > toBoard(checker.getOldY())){
                    l++;
                    p--;
                } else if (newX > toBoard(checker.getOldX()) && newY > toBoard(checker.getOldY())){
                    l--;
                    p--;
                } else {
                    l--;
                    p++;
                }
            }

            if (count > 1 || countcr>0) {
                return new MoveResult(MoveType.NO);
            } else if (count == 1) {
                return new MoveResult(MoveType.KILLQUEEN, board[x0][y0].getChecker());
            }

            return new MoveResult(MoveType.YESQUEEN);
        }



        return new MoveResult(MoveType.NO);
    }

    private int toBoard(double pixel) {
        return (int)(pixel + CELL_SIZE / 2) / CELL_SIZE;
    }
    private Checker makeCheker(CheckerType type, int x, int y) {
        Checker checker = new Checker(type, x, y);

        checker.setOnMouseReleased(e -> {
            int newX = toBoard(checker.getLayoutX());
            int newY = toBoard(checker.getLayoutY());

            MoveResult result;

            if (newX < 0 || newY < 0 || newX >= WIDTH || newY >= HEIGHT) {
                result = new MoveResult(MoveType.NO);
            } else {
                result = tryMove(checker, newX, newY);
            }

            int x0 = toBoard(checker.getOldX());
            int y0 = toBoard(checker.getOldY());

            switch (result.getType()) {
                case NO:
                    checker.noMove();
                    break;
                case YES:
                    if (newY == HEIGHT -1 ){
                        checker.removeTypeR();
                    }
                    if (newY == 0){
                        checker.removeTypeW();
                    }
                    checker.go(newX, newY);
                    board[x0][y0].setChecker(null);
                    board[newX][newY].setChecker(checker);
                    break;
                case YESQUEEN:
                    checker.go(newX, newY);
                    board[x0][y0].setChecker(null);
                    board[newX][newY].setChecker(checker);
                    break;
                case KILL:
                    if (newY == HEIGHT -1 ){
                        checker.removeTypeR();
                    }
                    if (newY == 0){
                        checker.removeTypeW();
                    }
                    checker.go(newX, newY);
                    board[x0][y0].setChecker(null);
                    board[newX][newY].setChecker(checker);

                    Checker otherChecker = result.getChecker();
                    board[toBoard(otherChecker.getOldX())][toBoard(otherChecker.getOldY())].setChecker(null);
                    checkerGroup.getChildren().remove(otherChecker);
                    break;

                case KILLQUEEN:
                    checker.go(newX, newY);
                    board[x0][y0].setChecker(null);
                    board[newX][newY].setChecker(checker);

                    otherChecker = result.getChecker();
                    board[toBoard(otherChecker.getOldX())][toBoard(otherChecker.getOldY())].setChecker(null);
                    checkerGroup.getChildren().remove(otherChecker);
                    break;
            }
        });


        return checker;
    }

    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
