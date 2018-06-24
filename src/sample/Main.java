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
        boolean hq = false;
        boolean sc1 = true;
        boolean g1 = false;
        boolean g2 = false;
        boolean g3 = false;
        boolean g4 = false;
        boolean sc = false;
        int d = 0;
        int count1 = 0;
        for (int h = 0; h < HEIGHT; h++) {
            for (int w = 0; w < WIDTH; w++) {
                if (board[h][w].hasChecker() && (board[h][w].getChecker().getType() == CheckerType.WHITE ||
                        board[h][w].getChecker().getType() == CheckerType.BLACK)) {
                    boolean a1 = h + 1 < 8 && w + 1 < 8 && board[h + 1][w + 1].hasChecker() &&
                            ((board[h][w].getChecker().getType() == CheckerType.BLACK && xod != 0 ||
                                    board[h][w].getChecker().getType() == CheckerType.WHITE && xod != 1)) &&
                            board[h][w].hasChecker() && h + 2 < 8 && w + 2 < 8 &&
                            board[h][w].getChecker().getType() != board[h + 1][w + 1].getChecker().getType() &&
                            !board[h + 2][w + 2].hasChecker();

                    boolean a2 = h + 1 < 8 && w - 1 >= 0 && board[h + 1][w - 1].hasChecker() &&
                            ((board[h][w].getChecker().getType() == CheckerType.BLACK && xod != 0 ||
                                    board[h][w].getChecker().getType() == CheckerType.WHITE && xod != 1)) &&
                            board[h][w].hasChecker() && h + 2 < 8 && w - 2 >= 0 &&
                            board[h][w].getChecker().getType() != board[h + 1][w - 1].getChecker().getType() &&
                            !board[h + 2][w - 2].hasChecker();

                    boolean a3 = h - 1 >= 0 && w + 1 < 8 && board[h - 1][w + 1].hasChecker() &&
                            ((board[h][w].getChecker().getType() == CheckerType.BLACK && xod != 0 ||
                                    board[h][w].getChecker().getType() == CheckerType.WHITE && xod != 1)) &&
                            board[h][w].hasChecker() && h - 2 >= 0 && w + 2 < 8 &&
                            board[h][w].getChecker().getType() != board[h - 1][w + 1].getChecker().getType() &&
                            !board[h - 2][w + 2].hasChecker();

                    boolean a4 = h - 1 >= 0 && w - 1 >= 0 && board[h - 1][w - 1].hasChecker() &&
                            ((board[h][w].getChecker().getType() == CheckerType.BLACK && xod != 0 ||
                                    board[h][w].getChecker().getType() == CheckerType.WHITE && xod != 1)) &&
                            board[h][w].hasChecker() && h - 2 >= 0 && w - 2 >= 0 &&
                            board[h][w].getChecker().getType() != board[h - 1][w - 1].getChecker().getType() &&
                            !board[h - 2][w - 2].hasChecker();

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

                if (board[h][w].hasChecker() && (board[h][w].getChecker().getType() == CheckerType.WHITEQUEEN ||
                        board[h][w].getChecker().getType() == CheckerType.BLACKQUEEN)) {
                    int hcopy = h;
                    int wcopy = w;
                    int b1 = 0;
                    int hcopy2 = 0;
                    int wcopy2 = 0;
                    int newh = 0;
                    int neww = 0;
                    boolean o1 = false;
                    boolean o2 = false;
                    boolean o3 = false;
                    boolean o4 = false;
                    count1 = 0;
                    while (hcopy > 0 && wcopy > 0) {
                        hcopy--;
                        wcopy--;
                        if (board[hcopy][wcopy].hasChecker()) {
                            count1 = 1;
                            hcopy2 = hcopy - 1;
                            wcopy2 = wcopy - 1;
                        }
                        if (board[hcopy][wcopy].hasChecker() && board[h][w].getChecker().getType() == CheckerType.WHITEQUEEN &&
                                (board[hcopy][wcopy].getChecker().getType() == CheckerType.BLACK ||
                                        board[hcopy][wcopy].getChecker().getType() == CheckerType.BLACKQUEEN) && xod == 0) {
                            b1 = 1;
                        } else if (board[hcopy][wcopy].hasChecker() && board[h][w].getChecker().getType() == CheckerType.BLACKQUEEN &&
                                (board[hcopy][wcopy].getChecker().getType() == CheckerType.WHITE ||
                                        board[h][w].getChecker().getType() == CheckerType.WHITEQUEEN) && xod == 1) {
                            b1 = 1;
                        }
                        if (b1 == 1 && !(board[hcopy2][wcopy2].hasChecker())) {
                            newh = hcopy2;
                            neww = wcopy2;
                            o1 = true;
                        }
                        if (o1 && x0 == h && y0 == w && newX == newh && newY == neww) {
                            g1 = true;
                        } else {
                            g1 = false;
                        }
                        if (count1 == 1) {
                            break;
                        }
                    }

                    hcopy = h;
                    wcopy = w;
                    count1 = 0;
                    while (hcopy > 0 && wcopy < 7) {
                        b1 = 0;
                        if (count1 == 1 || (board[x0][y0].getChecker().getType() == CheckerType.WHITEQUEEN && xod == 1)) {
                            break;
                        }
                        hcopy--;
                        wcopy++;
                        if (board[hcopy][wcopy].hasChecker() && (Math.abs(h - hcopy) == Math.abs(w - wcopy))) {
                            count1 = 1;
                            hcopy2 = hcopy - 1;
                            wcopy2 = wcopy + 1;
                        }
                        if (board[hcopy][wcopy].hasChecker() && board[h][w].getChecker().getType() == CheckerType.WHITEQUEEN &&
                                (board[hcopy][wcopy].getChecker().getType() == CheckerType.BLACK ||
                                        board[hcopy][wcopy].getChecker().getType() == CheckerType.BLACKQUEEN) && xod == 0) {
                            b1 = 1;
                        } else if (board[hcopy][wcopy].hasChecker() && board[h][w].getChecker().getType() == CheckerType.BLACKQUEEN &&
                                (board[hcopy][wcopy].getChecker().getType() == CheckerType.WHITE ||
                                        board[h][w].getChecker().getType() == CheckerType.WHITEQUEEN) && xod == 1) {
                            b1 = 1;
                        }
                        if (b1 == 1 && !(board[hcopy2][wcopy2].hasChecker())) {
                            newh = hcopy2;
                            neww = wcopy2;
                            o2 = true;
                        }
                        if (o2 && x0 == h && y0 == w && newX == newh && newY == neww) {
                            g2 = true;
                        } else {
                            g2 = false;
                        }
                        if (count1 == 1) {
                            break;
                        }
                    }

                    hcopy = h;
                    wcopy = w;
                    count1 = 0;
                    while (hcopy < 7 && wcopy > 0) {
                        b1 = 0;
                        if (count1 == 1 || (board[x0][y0].getChecker().getType() == CheckerType.WHITEQUEEN && xod == 1)) {
                            break;
                        }
                        hcopy++;
                        wcopy--;
                        if (board[hcopy][wcopy].hasChecker() && (Math.abs(h - hcopy) == Math.abs(w - wcopy))) {
                            count1 = 1;
                            hcopy2 = hcopy + 1;
                            wcopy2 = wcopy - 1;
                        }
                        if (board[hcopy][wcopy].hasChecker() && board[h][w].getChecker().getType() == CheckerType.WHITEQUEEN &&
                                (board[hcopy][wcopy].getChecker().getType() == CheckerType.BLACK ||
                                        board[hcopy][wcopy].getChecker().getType() == CheckerType.BLACKQUEEN) && xod == 0) {
                            b1 = 1;
                        } else if (board[hcopy][wcopy].hasChecker() && board[h][w].getChecker().getType() == CheckerType.BLACKQUEEN &&
                                (board[hcopy][wcopy].getChecker().getType() == CheckerType.WHITE ||
                                        board[h][w].getChecker().getType() == CheckerType.WHITEQUEEN) && xod == 1) {
                            b1 = 1;
                        }
                        if (b1 == 1 && !(board[hcopy2][wcopy2].hasChecker())) {
                            newh = hcopy2;
                            neww = wcopy2;
                            o3 = true;
                        }
                        if (o3 && x0 == h && y0 == w && newX == newh && newY == neww) {
                            g3 = true;
                        } else {
                            g3 = false;
                        }
                        if (count1 == 1) {
                            break;
                        }
                    }

                    hcopy = h;
                    wcopy = w;
                    count1 = 0;
                    while (hcopy < 7 && wcopy < 7) {
                        b1 = 0;
                        if (count1 == 1 || (board[x0][y0].getChecker().getType() == CheckerType.WHITEQUEEN && xod == 1)) {
                            break;
                        }
                        hcopy++;
                        wcopy++;
                        if (board[hcopy][wcopy].hasChecker() && (Math.abs(h - hcopy) == Math.abs(w - wcopy))) {
                            count1 = 1;
                            hcopy2 = hcopy + 1;
                            wcopy2 = wcopy + 1;
                        }
                        if (board[hcopy][wcopy].hasChecker() && board[h][w].getChecker().getType() == CheckerType.WHITEQUEEN &&
                                (board[hcopy][wcopy].getChecker().getType() == CheckerType.BLACK ||
                                        board[hcopy][wcopy].getChecker().getType() == CheckerType.BLACKQUEEN) && xod == 0) {
                            b1 = 1;
                        } else if (board[hcopy][wcopy].hasChecker() && board[h][w].getChecker().getType() == CheckerType.BLACKQUEEN &&
                                (board[hcopy][wcopy].getChecker().getType() == CheckerType.WHITE ||
                                        board[h][w].getChecker().getType() == CheckerType.WHITEQUEEN) && xod == 1) {
                            b1 = 1;
                        }
                        if (b1 == 1 && !(board[hcopy2][wcopy2].hasChecker())) {
                            newh = hcopy2;
                            neww = wcopy2;
                            o4 = true;
                        }
                        if (o4 && x0 == h && y0 == w && newX == newh && newY == neww) {
                            g4 = true;
                        } else {
                            g4 = false;
                        }
                        if (count1 == 1) {
                            break;
                        }
                    }
                    if (o1 && g1 || o2 && g2 || o3 && g3 || o4 && g4) {
                        sc1 = true;
                        hq = true;
                    } else if (!o1 && !o2 && !o3 && !o4) {
                        sc1 = true;
                        hq = true;
                    } else {
                        sc1 = false;
                    }
                }
                if (hq) {
                    break;
                }
            }

            if (sc || hq) {
                break;
            }

        }

        if (board[newX][newY].hasChecker() || (newX + newY) % 2 == 0 || (!sc && d != 0) || !sc1) {
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
                        checker.getType() == CheckerType.BLACK && xod == 1 )) {
            return new MoveResult(MoveType.YES);
        } else if (Math.abs(newX - x0) == 2 && Math.abs(newY - y0) == Math.abs(checker.getType().checkerType * 2) &&
                (checker.getType() == CheckerType.WHITE && xod == 0 ||
                        checker.getType() == CheckerType.BLACK && xod == 1 )) {

            int x1 = x0 + (newX - x0) / 2;
            int y1 = y0 + (newY - y0) / 2;

            if (board[x1][y1].hasChecker() && board[x1][y1].getChecker().getType() != checker.getType()) {
                return new MoveResult(MoveType.KILL, board[x1][y1].getChecker());
            }
        } else if (dam &&
                (checker.getType() == CheckerType.WHITEQUEEN && xod == 0 ||
                        checker.getType() == CheckerType.BLACKQUEEN && xod == 1 )){

            while (p != toBoard(checker.getOldY()) && l != toBoard(checker.getOldX())){
                if (board[l][p].hasChecker() && board[l][p].getChecker().getType() != checker.getType() && (Math.abs(l - newX) == Math.abs(p - newY))) {
                    count++;
                    x0 = l;
                    y0 = p;
                }

                if ((checker.getType() == CheckerType.BLACKQUEEN && board[l][p].hasChecker() &&
                        (board[l][p].getChecker().getType() == CheckerType.BLACK ||
                                board[l][p].getChecker().getType() == CheckerType.BLACKQUEEN))||
                       (checker.getType() == CheckerType.WHITEQUEEN && board[l][p].hasChecker() &&
                               (board[l][p].getChecker().getType() == CheckerType.WHITE ||
                                       board[l][p].getChecker().getType() == CheckerType.WHITEQUEEN))){
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
                    if (newY == HEIGHT -1 && type == CheckerType.BLACK ){
                        checker.removeTypeR();
                    }
                    if (newY == 0 && type == CheckerType.WHITE){
                        checker.removeTypeW();
                    }
                    if (type == CheckerType.WHITE){
                        xod++;
                    } else {
                        xod--;
                    }
                    checker.go(newX, newY);
                    board[x0][y0].setChecker(null);
                    board[newX][newY].setChecker(checker);
                    System.out.print(xod);
                    break;
                case YESQUEEN:
                    if (type == CheckerType.WHITEQUEEN){
                        xod++;
                    } else {
                        xod--;
                    }
                    checker.go(newX, newY);
                    board[x0][y0].setChecker(null);
                    board[newX][newY].setChecker(checker);
                    break;
                case KILL:
                    if (newY == HEIGHT -1 && type == CheckerType.BLACK ){
                        checker.removeTypeR();
                    }
                    if (newY == 0 && type == CheckerType.WHITE){
                        checker.removeTypeW();
                    }

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

                    if (type == CheckerType.WHITE && !a4 && !a1 && !a2 && !a3) {
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

                    if (type == CheckerType.WHITEQUEEN) {
                        xod--;
                    } else {
                        xod++;
                    }

                    break;
            }
        });

        return checker;
    }


    public void start(Stage primaryStage) {
        primaryStage.setTitle("Checkers");
        FlowPane root = new FlowPane( 10 , 10);
        Scene scene = new Scene(root , 400 , 400);
        primaryStage.setScene(scene);
        Button btn = new Button("Играть");
        root.setAlignment(Pos.CENTER);
        BackgroundFill dk = new BackgroundFill(Color.BLACK , new CornerRadii(1) , Insets.EMPTY);
        root.setBackground(new Background(dk));
        Button btn1 = new Button("Выход");
        btn.setOnAction(event -> primaryStage.setScene(new Scene(createContent())));
        btn1.setOnAction(event -> primaryStage.close());
        root.getChildren().addAll(btn , btn1);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
