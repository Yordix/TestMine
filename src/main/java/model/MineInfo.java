package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "field")
@XmlType(propOrder = { "x", "y", "mine", "revealed", "minesNear", "flagged" })
public class MineInfo {

    private int x;
    private int y;
    private boolean isMine;
    private boolean isRevealed;
    private int minesNear;

    private boolean isFlagged;

    public MineInfo() {
    }

    public MineInfo(int x, int y, boolean isMine, boolean isRevealed, int minesNear, boolean isFlagged) {
        super();
        this.x = x;
        this.y = y;
        this.isMine = isMine;
        this.isRevealed = isRevealed;
        this.minesNear = minesNear;
        this.isFlagged = isFlagged;
    }

    public int getX() {
        return x;
    }

    @XmlElement
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    @XmlElement
    public void setY(int y) {
        this.y = y;
    }

    public boolean isMine() {
        return isMine;
    }

    @XmlElement
    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    @XmlElement
    public void setRevealed(boolean isRevealed) {
        this.isRevealed = isRevealed;
    }

    public int getMinesNear() {
        return minesNear;
    }

    @XmlElement
    public void setMinesNear(int minesNear) {
        this.minesNear = minesNear;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    @XmlElement
    public void setFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
    }

    @Override
    public String toString() {
        return "MineInfo [x=" + x + ", y=" + y + ", isMine=" + isMine + ", isRevealed=" + isRevealed + ", minesNear="
                + minesNear + ", isFlagged=" + isFlagged + "]";
    }

}