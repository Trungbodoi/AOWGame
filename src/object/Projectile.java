package object;

import entities.Unit;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Projectile {
    private float x, y, width,height ,vx, vy;
    private int damage;
    private boolean active = true;
    private boolean isAlly;
    private Rectangle2D.Float hitbox;
    private BufferedImage image;

    public Projectile(float x, float y, int damage, boolean isAlly, boolean facingRight, int id, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.isAlly = isAlly;
        this.image = image;

        width = image.getWidth();
        height = image.getHeight();
        this.hitbox = new Rectangle2D.Float((int)x, (int)y, width, height);

        float speed = getSpeedFromID(id);
        this.vx = facingRight ? speed : -speed;
        this.vy = 0f;
    }

    private float getSpeedFromID(int id) {
        return switch (id) {
            case 12 -> 2f;
            case 21 -> 2f;
            case 23 -> 2f;
            case 24 -> 2f;
            case 32 -> 2f;
            case 42 -> 2f;
            case 43 -> 2f;
            case 44 -> 2f;
            case 52 -> 2f;
            case 53 -> 2f;
            default -> 2f;

        };
    }

    public void update() {
        if (!active) return;
        x += vx ;
        y += vy ;
        hitbox.setRect(x, y,width,height);
        if (x < -64 || x > 2000) active = false;
    }

    public boolean checkCollision(Unit target) {
        if (target.isAlly() != this.isAlly && hitbox.intersects(target.getHitbox())) {
            target.takeDamage(damage);
            active = false;
            return true;
        }
        return false;
    }

    public void render(Graphics g) {
        if (active) {
//            drawHitbox(g);
            if(isAlly) g.drawImage(image,(int)x,(int)y,(int)width,(int)height,null);
            else       g.drawImage(image, (int)(x + width), (int)y,(int)-width,(int)height, null);

        }
    }
    public void drawHitbox(Graphics g){
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }
    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    public boolean isActive() {
        return active;
    }
    public boolean isAlly(){
        return isAlly;
    }
    public void deactive(){
        this.active = false;
    }

    public int getDamage() {
        return damage;
    }
}