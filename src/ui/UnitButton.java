package ui;

import utilz.Constants;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static main.Game.SCALE;


public class UnitButton extends PauseButton{

    private BufferedImage[] imgs;
    private int rowIndex,index;
    private int spawnFrame;
    private int remainingFrame;
    private boolean spawning = false;




    public UnitButton(int x, int y, int width, int height,int rowIndex) {

        super(x, y, width, height);
        this.rowIndex = rowIndex;
        this.index = 1;
        loadImgs();
        spawnFrame = (int)(Constants.loadSpawntime(getUnitIndex())*200);
    }
    private void loadImgs() {
        imgs = new BufferedImage[5];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = LoadSave.GetSpriteAtlas("units/unit_"+((i+1)*10+rowIndex+1)+".png");

    }
    public boolean canSpawn(){
        return remainingFrame <= 0;
    }
    public void Spawn(){
        remainingFrame = spawnFrame;
        setSpawning(true);
    }
    public void decreaseCooldown(){
        remainingFrame--;
    }



    public void draw(Graphics g) {
        g.drawImage(imgs[index-1], x, y,width ,height ,null);
    }
    public void setIndex(int index){
        this.index = index;
    }
    public int getRowIndex(){ return rowIndex;}
    public int getIndex(){ return index;}
    public int getUnitIndex(){ return index*10+rowIndex+1;}
    public float getPercent(){
        return (float) remainingFrame /spawnFrame;
    }
    public int getRemainingFrame(){
        return remainingFrame;
    }
    public int getSpawnFrame(){
        return spawnFrame;
    }



    public boolean isSpawning() {
        return spawning;
    }

    public void setSpawning(boolean spawning) {
        this.spawning = spawning;
    }
}
