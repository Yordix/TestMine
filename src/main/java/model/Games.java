package model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "games")
public class Games {

    private List<Game> games;

    public Games() {
        this.games = new ArrayList<>();
    }

    public Games(List<Game> games) {
        this.games = games;
    }

    public List<Game> getGames() {
        return games;
    }

    @XmlElement(name = "game")
    public void setGames(List<Game> games) {
        this.games = games;
    }

    @Override
    public String toString() {
        return "Games [games=" + games + "]";
    }

}