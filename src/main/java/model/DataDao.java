package model;

import java.util.List;

public interface DataDao {

    boolean isValidTile(int x, int y);

    boolean isDone(Data[][] board);

    public List<String> getSavedGameIds();

    public Games loadMines();

    public void saveMines(Data board[][]);

    List<Data> getNeighbours(Data tile, Data[][] board);

    Game fillToBeSaved(Data[][] board);

}