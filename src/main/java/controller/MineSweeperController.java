package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Data;
import model.DataDao;
import model.DataDaoImpl;
import model.Game;
import model.Games;
import model.MineInfo;

// CHECKSTYLE:OFF
public class MineSweeperController {

    private static final Logger logger = LoggerFactory.getLogger(MineSweeperController.class);
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int TILESIZE = 30;
    private int xTiles;
    private int yTiles;
    private Data board[][];
    private Button save;
    private Button back;
    private Button exit;

    public MineSweeperController() {
    }

    public void initMineSweeperController(int xTiles, int yTiles) {
        this.xTiles = xTiles;
        this.yTiles = yTiles;
        this.board = new Data[xTiles][yTiles];
        logger.debug("INFO - A fő képernyő inicializálva.");
    }

    public void exitGame(ActionEvent actionEvent) {
        System.exit(0);
    }

    public BorderPane createContent(int x, int y) {
        BorderPane root = new BorderPane();
        DataDao data = new DataDaoImpl();
        root.setPrefSize(WIDTH, HEIGHT);
        initMineSweeperController(x, y);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setMaxWidth(WIDTH);
        root.setMaxHeight(HEIGHT);
        root.setCenter(populate());
        HBox hbButtons = new HBox();
        createButtons(data, hbButtons);
        hbButtons.getChildren().addAll(back, save, exit);
        root.setBottom(hbButtons);
        return root;
    }

    public Parent loadContent(Games games, String id, int xSize, int ySize) {
        BorderPane root = new BorderPane();
        root.setPrefSize(WIDTH, HEIGHT);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setMaxWidth(WIDTH);
        root.setMaxHeight(HEIGHT);
        DataDao data = new DataDaoImpl();
        initMineSweeperController(xSize, ySize);
        for (Game game : games.getGames()) {
            if (id.equals(game.getId())) {
                root.setCenter(fillBoard(game));
            }
        }
        HBox hbButtons = new HBox();
        createButtons(data, hbButtons);
        hbButtons.getChildren().addAll(back, save, exit);
        root.setBottom(hbButtons);
        logger.info("Játékállás sikeresen betöltve.");
        return root;
    }

    private void createButtons(DataDao data, HBox hbButtons) {
        hbButtons.setAlignment(Pos.CENTER);
        hbButtons.setSpacing(10.0);
        save = new Button("Mentés");
        save.setOnMouseClicked(b -> {
            data.saveMines(board);
        });
        back = new Button("Vissza");
        back.setOnMouseClicked(b -> {
            Stage dialog = new Stage();
            FXMLLoader newGamefXMLLoader = new FXMLLoader(getClass().getResource("NewGameDialog.fxml"));
            try {
                Parent backRoot = newGamefXMLLoader.load();
                Scene dialogScene = new Scene(backRoot);
                dialog.setScene(dialogScene);
                dialog.show();
                Stage actualStage = (Stage) back.getScene().getWindow();
                actualStage.close();
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Váratlan hiba történt: " + e.getMessage() + "\nKérlek próbáld újra!");
                logger.error("{}", alert.getContentText());
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    alert.close();
                }
            }
        });
        exit = new Button("Kilépés");
        exit.setOnMouseClicked(b -> {
            logger.info("KILÉPÉS");
            System.exit(0);
        });
    }

    public Pane populate() {
        Pane root = new Pane();
        double randomToGenerate = (xTiles+yTiles)/100.0;
        // sorfolytonosan tölti
        for (int y = 0; y < yTiles; y++) {
            for (int x = 0; x < xTiles; x++) {
                Data filled = new Data();
                setFieldProperties(y, x, Math.random() < randomToGenerate, filled);
                board[x][y] = filled;
                root.getChildren().add(filled);
            }
        }
        root.setTranslateX((WIDTH-xTiles*TILESIZE)/2-5);
        fillText();
        return root;
    }

    private Pane fillBoard(Game game) {
        Pane root = new Pane();
        for (MineInfo mine : game.getMines()) {
            Data filled = new Data();
            setFieldProperties(mine.getY(), mine.getX(), mine.isMine(), filled);
            filled.setRevealed(mine.isRevealed());
            if (filled.isRevealed()) {
                openLoaded(filled);
            }
            filled.setFlagged(mine.isFlagged());
            if (filled.isFlagged()) {
                markLoaded(filled);
            }
            board[mine.getX()][mine.getY()] = filled;
            root.getChildren().add(filled);
        }
        root.setTranslateX((WIDTH-xTiles*TILESIZE)/2-5);
        fillText();
        return root;
    }

    private void setFieldProperties(int y, int x, boolean isMine, Data filled) {
        filled.setX(x);
        filled.setY(y);
        filled.setMine(isMine);
        filled.setTile(new Rectangle(TILESIZE - 2, TILESIZE - 2));
        filled.getTile().setStroke(Color.BLUE);
        filled.getTile().setFill(Color.GREY);
        filled.setText(new Text());
        filled.getText().setFont(Font.font(18));
        filled.getText().setText(filled.isMine() ? "X" : "");
        filled.getText().setVisible(false);
        if (filled.isMine()) {
            filled.setPicture(new ImageView(new Image(getClass().getResourceAsStream("/images/imgBomb.png"))));
            filled.getPicture().setFitWidth(filled.getTile().getWidth());
            filled.getPicture().setFitHeight(filled.getTile().getHeight());
        } else {
            filled.setPicture(null);
        }
        filled.getChildren().addAll(filled.getTile(), filled.getText());
        filled.setTranslateX(x * TILESIZE);
        filled.setTranslateY(y * TILESIZE);
        filled.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                open(filled);
            } else if (!filled.isFlagged() && e.getButton().equals(MouseButton.SECONDARY)) {
                mark(filled);
            } else if (filled.isFlagged() && e.getButton().equals(MouseButton.SECONDARY)) {
                removeMark(filled);
            }
        });
    }

    private void fillText() {
        DataDao data = new DataDaoImpl();
        for (int y = 0; y < yTiles; y++) {
            for (int x = 0; x < xTiles; x++) {
                Data filled = board[x][y];

                if (filled.isMine()) {
                    continue;
                }

                long neighboursWithMines = data.getNeighbours(filled, board).stream().filter(s -> s.isMine()).count();
                if (neighboursWithMines > 0) {
                    filled.getText().setText(String.valueOf(neighboursWithMines));
                    filled.setMinesNear((int) neighboursWithMines);
                }
            }
        }
    }

    public void revealBombs() {
        DataDao data = new DataDaoImpl();
        for (int i = 0; i < xTiles; i++) {
            for (int j = 0; j < yTiles; j++) {
                if (board[i][j].isFlagged()) {
                    board[i][j].getChildren().remove(board[i][j].getPicture());
                    board[i][j].getPicture().setImage(new Image(getClass().getResourceAsStream("/images/imgBomb.png")));
                }
                if (board[i][j].isMine()) {
                    board[i][j].getPicture().setVisible(true);
                    board[i][j].getChildren().add(board[i][j].getPicture());
                }
            }
        }
        data.saveMines(board);
        gameOver();
    }

    private void gameOver() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Vége");
        alert.setHeaderText("Sikertelen!");
        alert.setContentText(
                "Egy bombára kattintottál! Vége a játéknak. Ha a OK gombra kattintasz, újat indíthatsz!");
        alert.showAndWait();
        logger.info("VÉGE, eredmény: {}", alert.getHeaderText());
        if (alert.getResult() == ButtonType.OK) {
            Stage dialog = new Stage();
            FXMLLoader newGamefXMLLoader = new FXMLLoader(getClass().getResource("NewGameDialog.fxml"));
            try {
                Parent backRoot = newGamefXMLLoader.load();
                Scene dialogScene = new Scene(backRoot);
                dialog.setScene(dialogScene);
                dialog.show();
                Stage actualStage = (Stage) back.getScene().getWindow();
                actualStage.close();
            } catch (Exception e) {
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setTitle("Error");
                alert.setContentText("Váratlan hiba történt: " + e.getMessage() + "\nKérlek próbáld újra!");
                logger.error("{}", errorAlert.getContentText());
                errorAlert.showAndWait();
                if (errorAlert.getResult() == ButtonType.OK) {
                    errorAlert.close();
                }
            }
            alert.close();
        } else if (alert.getResult() == ButtonType.CANCEL) {
            alert.close();
            System.exit(0);
        }
    }

    public void open(Data data) {
        DataDao dataDao = new DataDaoImpl();
        if (data.isRevealed()) {
            return;
        }

        if (data.isMine()) {
            if (data.isFlagged()) {
                data.getPicture().setImage(null);
            }
            revealBombs();
        } else {
            data.setRevealed(true);
            data.getText().setVisible(true);
            data.getTile().setFill(null);
            data.getChildren().remove(data.getPicture());

            if (data.getText().getText().isEmpty()) {
                dataDao.getNeighbours(data, board).forEach(this::open);
            }

            showEndDialog(data);
        }

    }

    private void showEndDialog(Data data) {
        DataDao dataDao = new DataDaoImpl();
        if (board[data.getX()][data.getY()] != null && dataDao.isDone(board)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Gratulálok");
            alert.setHeaderText("Siker!");
            alert.setContentText("Jól jelölted meg az összes aknát! Vége a játéknak.");
            alert.showAndWait();
            logger.info("VÉGE, eredmény: {}", alert.getHeaderText());
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
    }

    public void openLoaded(Data data) {
        if (data.isRevealed()) {
            data.getText().setVisible(true);
            data.getTile().setFill(null);
            data.getChildren().remove(data.getPicture());
        }

        showEndDialog(data);
    }

    public void mark(Data data) {
        if (data.isRevealed()) {
            return;
        }

        data.setFlagged(true);
        data.setPicture(new ImageView(new Image(getClass().getResourceAsStream("/images/imgRedFlag.png"))));
        data.getPicture().setFitWidth(data.getTile().getWidth());
        data.getPicture().setFitHeight(data.getTile().getHeight());
        data.getPicture().setVisible(true);
        data.getChildren().add(data.getPicture());
        showEndDialog(data);
    }

    public void markLoaded(Data data) {
        if (data.isFlagged()) {
            data.setPicture(new ImageView(new Image(getClass().getResourceAsStream("/images/imgRedFlag.png"))));
            data.getPicture().setFitWidth(data.getTile().getWidth());
            data.getPicture().setFitHeight(data.getTile().getHeight());
            data.getPicture().setVisible(true);
            data.getChildren().add(data.getPicture());
        }

        showEndDialog(data);
    }

    public void removeMark(Data data) {
        data.setFlagged(false);
        data.getChildren().remove(data.getPicture());
    }

    public int getxTiles() {
        return xTiles;
    }

    public void setxTiles(int xTiles) {
        this.xTiles = xTiles;
    }

    public int getyTiles() {
        return yTiles;
    }

    public void setyTiles(int yTiles) {
        this.yTiles = yTiles;
    }

    public Data[][] getBoard() {
        return board;
    }

    public void setBoard(Data[][] board) {
        this.board = board;
    }

}