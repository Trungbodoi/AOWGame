package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.Game.*;


public class SkillButton extends PauseButton{

    private BufferedImage[] imgs;
    private int index;



    public SkillButton(int x, int y, int width, int height) {

        super(x, y, width, height);
        loadImgs();
    }
    private void loadImgs() {
        imgs = new BufferedImage[5];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = LoadSave.GetSpriteAtlas("units/skill_"+(i+1)+".png");

    }
    public void draw(Graphics g) {
        g.drawImage(imgs[index-1], x, y,width ,height ,null);
    }
    public void setIndex(int index){
        this.index = index;
    }

}
