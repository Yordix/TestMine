package model;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class DataDaoImpl implements DataDao {

    private static int xTiles;
    private static int yTiles;

    private static final Logger logger = LoggerFactory.getLogger(DataDaoImpl.class);

    public DataDaoImpl() {
    }

    @Override
    public boolean isValidTile(int x, int y) {
        if (x >= 0 && x < xTiles && y >= 0 && y < yTiles) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isDone(Data[][] board) {

        int revealed = 0;
        int mines = 0;
        for (int i = 0; i < xTiles; i++) {
            for (int j = 0; j < yTiles; j++) {
                if (board[i][j].isRevealed()) {
                    revealed++;
                }
                if (board[i][j].isMine() && board[i][j].isFlagged()) {
                    mines++;
                }
            }
        }
        return xTiles * yTiles - revealed == mines;

    }

    @Override
    public List<String> getSavedGameIds() {
        Games games;
        List<String> savedGameIds = null;
        games = loadMines();
        if (games == null) {
            games = new Games();
            logger.info("Nem volt még elmentett játékállás.");
        }
        savedGameIds = new ArrayList<>(games.getGames().size());
        for (Game game : games.getGames()) {
            logger.debug("Hozzáadva a return listához: {}", game.getId());
            savedGameIds.add(game.getId());
        }
        return savedGameIds;
    }

    @Override
    public Games loadMines() {
        File theDir = new File(System.getProperty("user.home"), "mineSweeperApp");
        theDir.mkdir();
        File file = new File(theDir, "db.xml");
        JAXBContext jaxbContext;
        Games readGames = null;
        try {
            jaxbContext = JAXBContext.newInstance(Games.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            readGames = (Games) jaxbUnmarshaller.unmarshal(file);
            logger.info("Sikeresen betöltve az adatbázis.");
        } catch (JAXBException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Nincs még mentett játékállás!\nKérlek próbáld újra mentés után!");
            logger.error("{}", alert.getContentText());
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
        return readGames;
    }

    @Override
    public void saveMines(Data board[][]) {

        File file = null;
        JAXBContext jaxbContext = null;
        Marshaller jaxbMarshaller = null;
        boolean isFirstSave = false;
        try {
            File theDir = new File(System.getProperty("user.home"), "mineSweeperApp");
            theDir.mkdir();
            file = new File(theDir, "db.xml");
            if(!file.exists()){
                file.createNewFile();
                isFirstSave = true;
            }
            jaxbContext = JAXBContext.newInstance(Games.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            Game game = new Game();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            game = fillToBeSaved(board);

            if (!isFirstSave) {
                Games games = this.loadMines();
                games.getGames().add(game);
                jaxbMarshaller.marshal(games, file);
                logger.trace("XML-hez hozzáadva: {}", game);
            } else {
                Games games = new Games();
                logger.debug("Új adatbázis XML létrehozva: {} - elérési útvonala: {}", games,
                        file.getAbsolutePath());
                games.getGames().add(game);
                jaxbMarshaller.marshal(games, file);
                logger.trace("XML-hez hozzáadva: {}", game);
            }
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText("Sikeres mentés");
            alert.setContentText("A játékállás elmentve.");
            logger.error("{}", alert.getContentText());
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        } catch (JAXBException | IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("A következő hiba történt: " + e.getMessage() + "\nKérlek próbáld újra!");
            logger.error("{}", alert.getContentText());
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
    }

    @Override
    public Game fillToBeSaved(Data[][] board) {
        Game game = new Game();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy.MM.d HH.mm.ss");
        String uniqueID = LocalDateTime.now().format(format).toString();
        game.setMines(new ArrayList<>());
        game.setId(uniqueID);
        for (int y = 0; y < yTiles; y++) {
            for (int x = 0; x < xTiles; x++) {
                MineInfo field = new MineInfo();
                field.setX(board[x][y].getX());
                field.setY(board[x][y].getY());
                field.setMine(board[x][y].isMine());
                field.setRevealed(board[x][y].isRevealed());
                field.setMinesNear(board[x][y].getMinesNear());
                field.setFlagged(board[x][y].isFlagged());
                game.getMines().add(field);
                logger.debug("A következő mező {} hozzáadva a játékhoz.", field);
            }
        }
        return game;
    }

    @Override
    public List<Data> getNeighbours(Data tile, Data[][] board) {
        List<Data> neighbours = new ArrayList<>();

        // 8 darab szomszédja van
        int[] points = new int[] { -1, -1, // BF
                -1, 0, // B
                -1, 1, // BL
                0, -1, // F
                0, 1, // L
                1, -1, // JF
                1, 0, // J
                1, 1 // JL
        };

        for (int i = 0; i < points.length; i++) {
            int dx = points[i];
            int dy = points[++i];

            int newX = tile.getX() + dx;
            int newY = tile.getY() + dy;

            if (isValidTile(newX, newY)) {
                neighbours.add(board[newX][newY]);
            }
        }

        return neighbours;
    }

    public int getxTiles() {
        return xTiles;
    }

    public void setxTiles(int xTiles) {
        DataDaoImpl.xTiles = xTiles;
    }

    public int getyTiles() {
        return yTiles;
    }

    public void setyTiles(int yTiles) {
        DataDaoImpl.yTiles = yTiles;
    }

}