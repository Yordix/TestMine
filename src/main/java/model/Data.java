package model;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Data extends StackPane {

    private int x;
    private int y;
    private boolean isMine;
    private boolean isRevealed;
    private int minesNear;
    private boolean isFlagged;
    private Rectangle tile;
    private Text text;
    private ImageView picture;

    public Data() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public int getMinesNear() {
        return minesNear;
    }

    public void setMinesNear(int minesNear) {
        this.minesNear = minesNear;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean isRevealed) {
        this.isRevealed = isRevealed;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
    }

    public Rectangle getTile() {
        return tile;
    }

    public void setTile(Rectangle tile) {
        this.tile = tile;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public ImageView getPicture() {
        return picture;
    }

    public void setPicture(ImageView picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Data [x=" + x + ", y=" + y + ", isMine=" + isMine + ", minesNear=" + minesNear + "]";
    }

}