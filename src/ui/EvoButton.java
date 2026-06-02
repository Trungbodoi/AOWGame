package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.Game.SCALE;


public class EvoButton extends PauseButton{

    private Image img;
    private int index;


    public EvoButton(int x, int y, int width, int height) {

        super(x, y, width, height);
        loadImgs();
    }
    private void loadImgs() {

        img = LoadSave.GetSpriteAtlas("units/evo.png");

    }
    public void draw(Graphics g) {
        g.drawImage(img, x, y,width ,height ,null);
    }
//    public boolean isMousePressed() {
//        return mousePressed;
//    }
//
//    public void setMousePressed(boolean mousePressed) {
//        this.mousePressed = mousePressed;
//    }
    public void setIndex(int index){
        this.index = index;
    }

}
