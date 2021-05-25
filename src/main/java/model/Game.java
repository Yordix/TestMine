package model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "game")
public class Game {

    private List<MineInfo> fields;

    private String id;

    public Game() {
        this.fields = new ArrayList<>();
    }

    public Game(List<MineInfo> mines, String id) {
        this.fields = mines;
        this.id = id;
    }

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MineInfo> getMines() {
        return fields;
    }

    @XmlElement(name = "mine")
    public void setMines(List<MineInfo> mines) {
        this.fields = mines;
    }

    @Override
    public String toString() {
        return "Game [fields=" + fields + ", id=" + id + "]";
    }

}