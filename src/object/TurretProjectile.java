package object;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class TurretProjectile {

    private Point2D.Float pos;
//    private float centerX,centerY;
    private float vx, vy; // vận tốc
    private float rotation;
    private BufferedImage sprite;

    private boolean isAlly;
    private int id,damage;
    private int width, height;
    private boolean active = true;

    public TurretProjectile(float x,float y, float vx,float vy,int damage,float rotation,int id, BufferedImage sprite , boolean isAlly) {
        pos = new Point2D.Float(x, y);
        this.vx = vx;
        this.vy = vy;
        this.damage = damage;
        this.rotation = rotation;
        this.id = id;
        this.sprite = sprite;
        this.width = sprite.getWidth();
        this.height = sprite.getHeight();
        this.isAlly = isAlly;
//        centerX = x+this.width/2;
//        centerY = y+this.height/2;

    }

    public void move() {
        pos.x += vx;
        pos.y += vy;
    }
    public Point2D.Float getPos() {
        return pos;
    }

    public void setPos(Point2D.Float pos) {
        this.pos = pos;
    }

    public int getId() {
        return id;
    }
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getDmg() {
        return damage;
    }

    public float getRotation() {
        return rotation;
    }


    public BufferedImage getImage() {
        return sprite;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight(){
        return height;
    }
    public Rectangle getHitbox() {
        return new Rectangle((int) pos.x, (int) pos.y , width, height);
    }

    public boolean isAlly() {
        return isAlly;
    }
//    public float getCenterX(){
//        return centerX;
//    }
//    public float getCenterY(){
//        return centerY;
//    }
}
