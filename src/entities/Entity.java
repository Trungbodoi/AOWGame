package entities;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public abstract class Entity {

    protected float x, y;
    protected int width, height;

    protected static final int IDLE = 0, RUNNING = 1, ATTACK = 2;

    protected Rectangle2D.Float hitbox;
    protected Rectangle2D.Float attackBox;

    protected int maxHealth;
    protected int currentHealth;
    protected int attackDamage;
    protected float attackSpeed;
    protected float attackRange;
    protected float walkSpeed;

    protected float buildTime;
    protected int cost;
    protected String name;
    protected boolean Alive = true;




    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
    public Rectangle2D.Float getAttackBox() {
        return attackBox;
    }
    public void drawHitbox(Graphics g){
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }
    public void drawAttackBox(Graphics g){
        g.drawRect((int) attackBox.x, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }



    public boolean isAlive() {
        return Alive;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }
    public int getAttackDamage() {
        return attackDamage;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public int getCost() {
        return cost;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

}