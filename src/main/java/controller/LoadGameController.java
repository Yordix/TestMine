package controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.DataDao;
import model.DataDaoImpl;
import model.Games;

// CHECKSTYLE:OFF
public class LoadGameController {

    @FXML
    private Button loadSavedGame;

    @FXML
    private Button back;

    @FXML
    private ComboBox<String> loadItem;

    private String id;

    private static final Logger logger = LoggerFactory.getLogger(LoadGameController.class);

    @FXML
    public void initialize() {
        DataDao data = new DataDaoImpl();
        ObservableList<String> list = FXCollections.observableArrayList(data.getSavedGameIds());
        loadItem.setItems(list);
        logger.debug("Mentett ID-k sikeresen felvéve.");
    }

    @FXML
    public void onLoadGameAction(ActionEvent actionEvent) {
        Stage stage;
        Parent root;
        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("Minesweeper.fxml"));
        try {
            fXMLLoader.load();
            logger.info(" A loadDialog betöltve.");
            setId(loadItem.getSelectionModel().getSelectedItem());
            stage = new Stage();
            if (getId() != null) {
                DataDao data = new DataDaoImpl();
                Games games = data.loadMines();
                int xSize = games.getGames().stream().filter(f -> f.getId().equals(getId()))
                        .flatMap(m -> m.getMines().stream()).map(x -> x.getX())
                        .max((m3, m4) -> Integer.compare(m3, m4))
                        .get();
                int ySize = games.getGames().stream().filter(f -> f.getId().equals(getId()))
                        .flatMap(m -> m.getMines().stream()).map(x -> x.getY())
                        .max((m3, m4) -> Integer.compare(m3, m4))
                        .get();
                DataDaoImpl dataDao = new DataDaoImpl();
                dataDao.setxTiles(xSize + 1);
                dataDao.setyTiles(ySize + 1);
                root = fXMLLoader.<MineSweeperController> getController().loadContent(games, getId(), xSize + 1,
                        ySize + 1);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setTitle("Aknakereső");
                stage.show();
                Stage actualStage = (Stage) loadSavedGame.getScene().getWindow();
                actualStage.close();
            }
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Váratlan hiba történt: " + e.getMessage() + "\nKérlek próbáld újra!");
            logger.error("{}", alert.getContentText());
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
    }

    @FXML
    public void onBackButtonAction(ActionEvent actionEvent) {
        Stage dialog = new Stage();
        FXMLLoader newGamefXMLLoader = new FXMLLoader(getClass().getResource("StartScreen.fxml"));
        try {
            Parent backRoot = newGamefXMLLoader.load();
            Scene dialogScene = new Scene(backRoot);
            dialog.setScene(dialogScene);
            dialog.show();
            dialog.setTitle("Aknakereső");
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
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

}