package entities;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.Game.*;

public class Base {

    private float x, y;
    private int width, height;
    private int maxHp = 1000, currentHp;
    private int age ;
    private boolean isAlly ;
    private int freeSpace = 0;

    private ArrayList<HutBlock> huts = new ArrayList<>();
    private BufferedImage img = LoadSave.GetSpriteAtlas("units/fortress.png");


    public Base(float x, float y, int width, int height,boolean isAlly) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.currentHp = maxHp;
        this.isAlly = isAlly;
        age = 1;
    }

    public  int getCurrentHeight() {
        return height ;
    }

    public void render(Graphics g) {
        // Vẽ thân base (hình chữ nhật đơn giản)
//        g.setColor(isAlly ? Color.BLUE : Color.RED);
//        g.fillRect((int) x, (int) y, width, height);
        g.drawImage(img, (int) x, (int) y,width,height,null);

        renderHuts(g);
//        for (Turret t : turrets) {
//            t.render(g);
//        }


        // Vẽ thanh máu

    }

    public void drawHealthBar(Graphics g) {
        int barWidth = 200;
        int barHeight = 20;
        int barX = isAlly ? 10 : GAME_WIDTH-barWidth-10;
        int barY = (int) (GAME_HEIGHT - 60 * SCALE);



        // Nền màu xám
        g.setColor(Color.GRAY);
        g.fillRect(barX, barY, barWidth, barHeight);

        // Màu xanh hoặc đỏ tùy máu
        float hpPercent = currentHp / (float) maxHp;
        g.setColor(hpPercent > 0.5f ? Color.GREEN : Color.ORANGE);
        if (hpPercent < 0.2f) g.setColor(Color.RED);

        g.fillRect(barX, barY, (int) (barWidth * hpPercent), barHeight);

        // Vẽ số HP
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        String hpText = currentHp + " / " + maxHp;
        if(isAlly) {
            g.drawString(hpText, barX + barWidth + 2, barY + barHeight - 1);
        }else
            g.drawString(hpText, GAME_WIDTH-barWidth-100, barY + barHeight - 1);
    }

    // Getter / Setter nếu cần
    public void takeDamage(int dmg) {
        currentHp -= dmg;
        if (currentHp < 0) currentHp = 0;
    }

    public boolean isDestroyed() {
        return currentHp <= 0;
    }

    public int getAge() {
        return age;
    }


    public void setAge(int newAge) {
        this.age = newAge;
    }

    public void heal(int amount) {
        currentHp += amount;
        if (currentHp > maxHp) currentHp = maxHp;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
    public void resetAll(){
        age = 1;
        maxHp = 1000;
        currentHp = maxHp;
        freeSpace = 0;
        huts.clear();
    }
    public void renderHuts(Graphics g) {
        for (HutBlock hut : huts) {
            hut.render(g);
        }
    }


    public void addHut(){
        if (huts.size() >= 3) return;

        int blockSize = 48;
        int hutX = isAlly ? (int)(x + width - blockSize) : (int)(GAME_WIDTH*2.5-width) ;

        int hutY = (int)(y - blockSize - huts.size() * blockSize);

        HutBlock newHut = new HutBlock(hutX, hutY);
        huts.add(newHut);
        freeSpace++;

    }
    public ArrayList<HutBlock> getHuts() {
        return huts;
    }
    public HutBlock getHutsId(int index){
        return huts.get(index);
    }


    public int getFreeSpace() {
        return freeSpace;
    }
    public void placeTurret(){
        freeSpace--;
    }
    public void freeTurret(){
        freeSpace++;
    }
    public boolean isAlly(){
        return isAlly;
    }
    public void evo(){
        if(age == 1){
            maxHp += 500;
            currentHp+= 500;
            age ++;
        }else if(age ==2){
            maxHp += 1500;
            currentHp+= 1500;
            age++;
        }else if(age == 3){
            maxHp += 1000;
            currentHp+= 1000;
            age++;

        }else if(age == 4){
            maxHp += 1000;
            currentHp+= 1000;
            age++;

        }
    }

    public int getCurrentHealth() {
        return currentHp;
    }
}
