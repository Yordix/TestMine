package controller;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

// CHECKSTYLE:OFF
public class StartScreenController {

    @FXML
    private Button newGame;

    @FXML
    private Button loadGame;

    @FXML
    private Button exit;

    @FXML
    private ComboBox<Integer> xSize;

    @FXML
    private ComboBox<Integer> ySize;

    @FXML
    private Button startGame;

    private String id;

    private static final Logger logger = LoggerFactory.getLogger(StartScreenController.class);

    @FXML
    public void startNewGameDialog(ActionEvent actionEvent) throws IOException {
        Stage dialog = new Stage();
        Parent root;
        FXMLLoader newGamefXMLLoader = new FXMLLoader(getClass().getResource("NewGameDialog.fxml"));
        root = newGamefXMLLoader.load();
        xSize = new ComboBox<>();
        ySize = new ComboBox<>();
        Scene dialogScene = new Scene(root);
        dialog.setScene(dialogScene);
        dialog.show();
        dialog.setTitle("Új játék");
        logger.info("Új játék indítás kezdeményezve.");
        Stage actualStage = (Stage) newGame.getScene().getWindow();
        actualStage.close();
    }

    @FXML
    public void loadGameDialog(ActionEvent actionEvent) throws IOException, JAXBException {
        Stage dialog = new Stage();
        Parent root;

        FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("LoadGameDialog.fxml"));
        root = fXMLLoader.load();

        Scene dialogScene = new Scene(root);
        dialog.setScene(dialogScene);
        dialog.show();
        dialog.setTitle("Mentett állás betöltése");
        logger.info("Játék betöltés kezdeményezve.");
        Stage actualStage = (Stage) loadGame.getScene().getWindow();
        actualStage.close();
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