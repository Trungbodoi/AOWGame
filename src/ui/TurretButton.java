package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.Game.SCALE;


public class TurretButton extends PauseButton{

    private BufferedImage[] imgs;
    private int rowIndex,index;



    public TurretButton(int x, int y, int width, int height,int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }


    private void loadImgs() {
        if(rowIndex <=2) {
            imgs = new BufferedImage[5];
            for (int i = 0; i < imgs.length; i++)
                imgs[i] = LoadSave.GetSpriteAtlas("units/tower_" + ((i + 1) * 10 + rowIndex + 1) + ".png");
        }else
        if(rowIndex == 3){
            imgs = new BufferedImage[1];
            imgs[0] =  LoadSave.GetSpriteAtlas("units/tower_buy.png");}
        else
        if(rowIndex == 4){
            imgs = new BufferedImage[1];
            imgs[0] =  LoadSave.GetSpriteAtlas("units/tower_sell.png");}



    }
    public void draw(Graphics g) {
        if(rowIndex<=2){
            g.drawImage(imgs[index-1], x, y,width ,height ,null);
        }else
            g.drawImage(imgs[0], x, y,width ,height ,null);

    }

    public void setIndex(int index){
        this.index = index;
    }

    public int getTurretIndex() {
        return index*10+rowIndex+1;
    }
}
