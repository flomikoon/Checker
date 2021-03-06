package sample;

import javafx.application.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;


public class Main extends Application {

    public static final int CELL_SIZE = 100;
    private static final int WIDTH = 8;
    private static final int HEIGHT = 8;

    private Cell[][] board = new Cell[WIDTH][HEIGHT];

    private Group cellGroup = new Group();
    private Group checkerGroup = new Group();

    private int xod = 0;


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
                    checker = makeCheker(CheckerType.BLACK, x, y);
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

        int x0 = toBoard(checker.getOldX());
        int y0 = toBoard(checker.getOldY());

        boolean sc = false;
        int d = 0;
        for (int h = 0; h < HEIGHT; h++) {
            for (int w = 0; w < WIDTH; w++) {
                //Проверяем есть ли такой ход ,что шашка может срубить
                if (board[h][w].hasChecker() && (board[h][w].getChecker().getType() == CheckerType.WHITE ||
                        board[h][w].getChecker().getType() == CheckerType.BLACK)) {
                    boolean a1 = h + 1 < 8 && w + 1 < 8 && h + 2 < 8 && w + 2 < 8 && moveChek(1, 1, h, w);

                    boolean a2 = h + 1 < 8 && w - 1 >= 0 && h + 2 < 8 && w - 2 >= 0 && moveChek(1, -1, h, w);

                    boolean a3 = h - 1 >= 0 && w + 1 < 8 && h - 2 >= 0 && w + 2 < 8 && moveChek(-1, 1, h, w);

                    boolean a4 = h - 1 >= 0 && w - 1 >= 0 && h - 2 >= 0 && w - 2 >= 0 && moveChek(-1, -1, h, w);

                    if (a1 || a2 || a3 || a4) {
                        if (newX == h + 2 && newY == w + 2 && h + 2 < 8 && w + 2 < 8 && x0 == h && y0 == w) {
                            sc = true;
                            d = 0;
                        } else if (newX == h + 2 && newY == w - 2 && h + 2 < 8 && w - 2 >= 0 && x0 == h && y0 == w) {
                            sc = true;
                            d = 0;
                        } else if (newX == h - 2 && newY == w + 2 && h - 2 >= 0 && w + 2 < 8 && x0 == h && y0 == w) {
                            sc = true;
                            d = 0;
                        } else if (newX == h - 2 && newY == w - 2 && h - 2 >= 0 && w - 2 >= 0 && x0 == h && y0 == w) {
                            sc = true;
                            d = 0;
                        }
                        d++;
                    }
                    if (sc) {
                        break;
                    }
                }

                //Проверяем есть ли такой ход ,что дамка может срубить
                int one = 1;
                int two = 2;
                boolean a1;
                boolean a2;
                boolean a3;
                boolean a4;
                while (one < 7 && two < 6) {
                    if (board[h][w].hasChecker() && (board[h][w].getChecker().getType() == CheckerType.WHITEQUEEN ||
                            board[h][w].getChecker().getType() == CheckerType.BLACKQUEEN)) {

                        a1 = h + one < 8 && w + one < 8 && h + two < 8 && w + two < 8 && moveQueen(1, 1, h, w);
                        a2 = h + one < 8 && w - one >= 0 && h + two < 8 && w - two >= 0 && moveQueen(1, -1, h, w);
                        a3 = h - one >= 0 && w + one < 8 && h - two >= 0 && w + two < 8 && moveQueen(-1, 1, h, w);
                        a4 = h - one >= 0 && w - one >= 0 && h - two >= 0 && w - two >= 0 && moveQueen(-1, -1, h, w);

                        int twocopy = two;
                        while (twocopy < 6 && !sc) {
                            if (a1 || a2 || a3 || a4) {
                                if (newX == h + twocopy && newY == w + twocopy && h + twocopy < 8 && w + twocopy < 8 && x0 == h && y0 == w) {
                                    int t = 1;
                                    int cou = 0;
                                    while (t < 7 && newX >= h + t && newY >= w + t) {
                                        if (board[h + t][w + t].hasChecker()) {
                                            cou++;
                                        }
                                        t++;
                                    }
                                    if (cou <= 1) {
                                        sc = true;
                                        d = 0;
                                    }
                                } else if (newX == h + twocopy && newY == w - twocopy && h + twocopy < 8 && w - twocopy >= 0 && x0 == h && y0 == w) {
                                    int t = 1;
                                    int cou = 0;
                                    while (t < 7 && newX > h + t - 1 && newY < w - t + 1) {
                                        if (board[h + t][w - t].hasChecker()) {
                                            cou++;
                                        }
                                        t++;
                                    }
                                    if (cou <= 1) {
                                        sc = true;
                                        d = 0;
                                    }
                                } else if (newX == h - twocopy && newY == w + twocopy && h - twocopy >= 0 && w + twocopy < 8 && x0 == h && y0 == w) {
                                    int t = 1;
                                    int cou = 0;
                                    while (t < 7 && newX < h - t + 1 && newY > w + t - 1) {
                                        if (board[h - t][w + t].hasChecker()) {
                                            cou++;
                                        }
                                        t++;
                                    }
                                    if (cou <= 1) {
                                        sc = true;
                                        d = 0;
                                    }
                                } else if (newX == h - twocopy && newY == w - twocopy && h - twocopy >= 0 && w - twocopy >= 0 && x0 == h && y0 == w &&
                                        !board[newX][newY].hasChecker()) {
                                    int t = two;
                                    int cou = 0;
                                    while (t < 7 && newX < h - t + 1 && newY < w - t + 1) {
                                        if (board[h - t][w - t].hasChecker()) {
                                            cou++;
                                        }
                                        t++;
                                    }
                                    if (cou <= 1) {
                                        sc = true;
                                        d = 0;
                                    }
                                }
                                d++;
                            }
                            twocopy++;
                        }
                        if (sc) {
                            break;
                        }
                    }
                    one++;
                    two++;
                }
            }
            if (sc) {
                break;
            }
        }
        if (board[newX][newY].hasChecker() || (newX + newY) % 2 == 0 || (!sc && d != 0)) {
            return new MoveResult(MoveType.NO);
        }

        int l = newX;
        int p = newY;
        int count = 0;
        int countcr = 0;

        boolean dam = (board[x0][y0].getChecker().getType() == CheckerType.BLACKQUEEN
                || board[x0][y0].getChecker().getType() == CheckerType.WHITEQUEEN)
                && (Math.abs(x0 - newX) == Math.abs(y0 - newY));

        if (Math.abs(newX - x0) == 1 && newY - y0 == checker.getType().checkerType &&
                (checker.getType() == CheckerType.WHITE && xod == 0 ||
                        checker.getType() == CheckerType.BLACK && xod == 1)) {
            return new MoveResult(MoveType.YES);
        } else if (Math.abs(newX - x0) == 2 && Math.abs(newY - y0) == Math.abs(checker.getType().checkerType * 2) &&
                (checker.getType() == CheckerType.WHITE && xod == 0 ||
                        checker.getType() == CheckerType.BLACK && xod == 1)) {

            int x1 = x0 + (newX - x0) / 2;
            int y1 = y0 + (newY - y0) / 2;

            if (board[x1][y1].hasChecker() && board[x1][y1].getChecker().getType() != checker.getType()) {
                return new MoveResult(MoveType.KILL, board[x1][y1].getChecker());
            }
        } else if (dam &&
                (checker.getType() == CheckerType.WHITEQUEEN && xod == 0 ||
                        checker.getType() == CheckerType.BLACKQUEEN && xod == 1)) {

            while (p != toBoard(checker.getOldY()) && l != toBoard(checker.getOldX())) {
                if (board[l][p].hasChecker() && board[l][p].getChecker().getType() != checker.getType() && (Math.abs(l - newX) == Math.abs(p - newY))) {
                    count++;
                    x0 = l;
                    y0 = p;
                }

                if ((checker.getType() == CheckerType.BLACKQUEEN && board[l][p].hasChecker() &&
                        (board[l][p].getChecker().getType() == CheckerType.BLACK ||
                                board[l][p].getChecker().getType() == CheckerType.BLACKQUEEN)) ||
                        (checker.getType() == CheckerType.WHITEQUEEN && board[l][p].hasChecker() &&
                                (board[l][p].getChecker().getType() == CheckerType.WHITE ||
                                        board[l][p].getChecker().getType() == CheckerType.WHITEQUEEN))) {
                    countcr++;
                }

                if (newX < toBoard(checker.getOldX()) && newY < toBoard(checker.getOldY())) {
                    p++;
                    l++;
                } else if (newX < toBoard(checker.getOldX()) && newY > toBoard(checker.getOldY())) {
                    l++;
                    p--;
                } else if (newX > toBoard(checker.getOldX()) && newY > toBoard(checker.getOldY())) {
                    l--;
                    p--;
                } else {
                    l--;
                    p++;
                }
            }

            if (count > 1 || countcr > 0) {
                return new MoveResult(MoveType.NO);
            } else if (count == 1) {
                return new MoveResult(MoveType.KILLQUEEN, board[x0][y0].getChecker());
            }
            return new MoveResult(MoveType.YESQUEEN);
        }
        return new MoveResult(MoveType.NO);
    }

    private boolean moveChek(int one, int two, int h, int w) {
        return board[h + one][w + two].hasChecker() &&
                ((board[h][w].getChecker().getType() == CheckerType.BLACK && xod != 0 ||
                        board[h][w].getChecker().getType() == CheckerType.WHITE && xod != 1)) &&
                board[h][w].hasChecker() && (board[h][w].getChecker().getType() == CheckerType.WHITE && (
                board[h + one][w + two].getChecker().getType() == CheckerType.BLACK ||
                        board[h + one][w + two].getChecker().getType() == CheckerType.BLACKQUEEN) ||
                board[h][w].getChecker().getType() == CheckerType.BLACK && (
                        board[h + one][w + two].getChecker().getType() == CheckerType.WHITE ||
                                board[h + one][w + two].getChecker().getType() == CheckerType.WHITEQUEEN)) &&
                !board[h + one * 2][w + two * 2].hasChecker();
    }

    private boolean moveQueen(int one, int two, int h, int w) {
        return board[h + one][w + two].hasChecker() &&
                ((board[h][w].getChecker().getType() == CheckerType.BLACKQUEEN && xod != 0 ||
                        board[h][w].getChecker().getType() == CheckerType.WHITEQUEEN && xod != 1)) &&
                board[h][w].hasChecker() &&
                (board[h][w].getChecker().getType() == CheckerType.WHITEQUEEN && (
                        board[h + one][w + two].getChecker().getType() == CheckerType.BLACK ||
                                board[h + one][w + two].getChecker().getType() == CheckerType.BLACKQUEEN) ||
                        board[h][w].getChecker().getType() == CheckerType.BLACKQUEEN && (
                                board[h + one][w + two].getChecker().getType() == CheckerType.WHITE ||
                                        board[h + one][w + two].getChecker().getType() == CheckerType.WHITEQUEEN)) &&
                !board[h + one * 2][w + two * 2].hasChecker();
    }

    private boolean killQueen(Checker checker, int newX, int newY, int one, int two) {
        return board[newX + one][newY + two].hasChecker()
                && ((board[newX + one][newY + two].getChecker().getType() == CheckerType.WHITE
                && checker.getType() == CheckerType.BLACKQUEEN || board[newX + one][newY + two].getChecker().getType()
                == CheckerType.WHITEQUEEN && checker.getType() == CheckerType.BLACKQUEEN) ||
                (board[newX + one][newY + two].getChecker().getType() == CheckerType.BLACK
                        && checker.getType() == CheckerType.WHITEQUEEN || board[newX + one][newY + two].getChecker().getType()
                        == CheckerType.BLACKQUEEN && checker.getType() == CheckerType.WHITEQUEEN)) &&
                !board[newX + one * 2][newY + two * 2].hasChecker();
    }

    private int toBoard(double pixel) {
        return (int) (pixel + CELL_SIZE / 2) / CELL_SIZE;
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
                    if (newY == HEIGHT - 1 && type == CheckerType.BLACK) {
                        checker.removeTypeR();
                    }
                    if (newY == 0 && type == CheckerType.WHITE) {
                        checker.removeTypeW();
                    }
                    if (type == CheckerType.WHITE) {
                        xod++;
                    } else {
                        xod--;
                    }
                    checker.go(newX, newY);
                    board[x0][y0].setChecker(null);
                    board[newX][newY].setChecker(checker);
                    break;
                case YESQUEEN:
                    if (checker.getType() == CheckerType.WHITEQUEEN) {
                        xod++;
                    } else {
                        xod--;
                    }
                    checker.go(newX, newY);
                    board[x0][y0].setChecker(null);
                    board[newX][newY].setChecker(checker);
                    break;
                case KILL:
                    if (newY == HEIGHT - 1 && checker.getType() == CheckerType.BLACK) {
                        checker.removeTypeR();
                    }
                    if (newY == 0 && checker.getType() == CheckerType.WHITE) {
                        checker.removeTypeW();
                    }

                    //Проверяем возможен ли еще один ход после рубки,для обычной шашки
                    boolean a1 = newX - 1 >= 0 && newY - 1 >= 0 && board[newX - 1][newY - 1].hasChecker()
                            && board[newX - 1][newY - 1].getChecker().getType() != checker.getType() &&
                            newX - 2 >= 0 && newY - 2 >= 0 && !board[newX - 2][newY - 2].hasChecker();

                    boolean a2 = newX - 1 >= 0 && newY + 1 < 8 && board[newX - 1][newY + 1].hasChecker()
                            && board[newX - 1][newY + 1].getChecker().getType() != checker.getType() &&
                            newX - 2 >= 0 && newY + 2 < 8 && !board[newX - 2][newY + 2].hasChecker();

                    boolean a3 = newX + 1 < 8 && newY - 1 > 0 && board[newX + 1][newY - 1].hasChecker()
                            && board[newX + 1][newY - 1].getChecker().getType() != checker.getType() &&
                            newX + 2 < 8 && newY - 2 >= 0 && !board[newX + 2][newY - 2].hasChecker();

                    boolean a4 = newX + 1 < 8 && newY + 1 < 8 && board[newX + 1][newY + 1].hasChecker()
                            && board[newX + 1][newY + 1].getChecker().getType() != checker.getType() &&
                            newX + 2 < 8 && newY + 2 < 8 && !board[newX + 2][newY + 2].hasChecker();

                    if ((checker.getType() == CheckerType.WHITE || checker.getType() == CheckerType.WHITEQUEEN) && !a4 && !a1 && !a2 && !a3) {
                        xod++;
                    } else if (!a4 && !a1 && !a2 && !a3) {
                        xod--;
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

                    //Проверяем возможен ли еще один ход после рубки,для дамки
                    int one = 1;
                    int two = 2;
                    boolean co = false;
                    boolean b1;
                    boolean b2;
                    boolean b3;
                    boolean b4;
                    while (one < 7 && two < 6) {
                        b1 = newX - one >= 0 && newY - one >= 0 && newX - two >= 0 && newY - two >= 0
                                && killQueen(checker, newX, newY, -1, -1);

                        b2 = newX - one >= 0 && newY + one < 8 && newX - two >= 0 && newY + two < 8
                                && killQueen(checker, newX, newY, -1, 1);

                        b3 = newX + one < 8 && newY - one > 0 && newX + two < 8 && newY - two >= 0
                                && killQueen(checker, newX, newY, 1, -1);

                        b4 = newX + one < 8 && newY + one < 8 && newX + two < 8 && newY + two < 8
                                && killQueen(checker, newX, newY, 1, 1);

                        if (b1 || b2 || b3 || b4) {
                            co = true;
                        }
                        one++;
                        two++;
                    }
                    if (checker.getType() == CheckerType.BLACKQUEEN && !co) {
                        xod--;
                    } else if (!co) {
                        xod++;
                    }
                    break;
            }
        });
        return checker;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Checkers");
        FlowPane root = new FlowPane(10, 10);
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        Button btn = new Button("Играть");
        root.setAlignment(Pos.CENTER);
        BackgroundFill dk = new BackgroundFill(Color.BLACK, new CornerRadii(1), Insets.EMPTY);
        root.setBackground(new Background(dk));
        Button btn1 = new Button("Выход");
        btn.setOnAction(event -> primaryStage.setScene(new Scene(createContent())));
        btn1.setOnAction(event -> primaryStage.close());
        root.getChildren().addAll(btn, btn1);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}