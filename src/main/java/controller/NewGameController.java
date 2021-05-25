package controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.DataDaoImpl;

// CHECKSTYLE:OFF
public class NewGameController {

    @FXML
    private ComboBox<Integer> xSize;

    @FXML
    private ComboBox<Integer> ySize;

    @FXML
    private Button startGame;

    @FXML
    private Button back;

    private String id;

    private static final Logger logger = LoggerFactory.getLogger(NewGameController.class);

    // navigálás a játékra
    @FXML
    public void onStartGameAction(ActionEvent actionEvent) throws IOException {
        logger.info("A newGameDialog betöltve.");
        Stage stage;
        Parent root;
        FXMLLoader startGamefXMLLoader = new FXMLLoader(getClass().getResource("MineSweeper.fxml"));
        startGamefXMLLoader.load();
        if (xSize.getValue() != null && ySize.getValue() != null) {
            DataDaoImpl dataDao = new DataDaoImpl();
            dataDao.setxTiles(xSize.getValue());
            dataDao.setyTiles(ySize.getValue());
            root = startGamefXMLLoader.<MineSweeperController> getController().createContent(xSize.getValue(),
                    ySize.getValue());
            stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
            stage.setTitle("Aknakereső");
            Stage actualStage = (Stage) startGame.getScene().getWindow();
            actualStage.close();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Szélességet és hosszúságot is meg kell adni.");
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

    // kilépés
    public void exitApp(ActionEvent actionEvent) {
        System.exit(0);
    }

}