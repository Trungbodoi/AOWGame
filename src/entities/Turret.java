package entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Turret extends Entity {
    private Base myBase;
    private TurretData data;

    private int id;
    private int hutIndex;
    private boolean isAlly;
    private float attackCooldown = 0;
    private Rectangle2D.Float attackBox;
    private BufferedImage image;
    private int framePerAttack ;

    public Turret(float x, float y, TurretData data, Map<Integer,BufferedImage> turretIcon, Base myBase,int hutIndex) {
        this.x = x;
        this.y = y;
        this.data = data;
        this.image = turretIcon.get(data.id);
        this.width = 48;
        this.height = 48;
        this.attackSpeed = data.attackSpeed;
        this.attackDamage = data.damage;
        this.id =data.id;
        this.cost = data.cost;

        this.hitbox = new Rectangle2D.Float(x, y, width, height);
        this.myBase = myBase;
        this.isAlly = myBase.isAlly();
        this.hutIndex = hutIndex;
        this.framePerAttack =(int)(200/attackSpeed);
        updateAttackBox();
    }

    public void update() {
        if (attackCooldown > 0)
            attackCooldown --;
    }
    public boolean canShoot() {
        return attackCooldown <= 0;
    }
    public void resetCooldown() {
        attackCooldown = framePerAttack; // attackSpeed là số đòn đánh mỗi giây
    }
    public float getCenterX() {
        return x + width / 2f;
    }

    public float getCenterY() {
        return y + height / 2f;
    }


    private void updateAttackBox() {
        float ax = isAlly ? x+width : x - data.range*64;
        float ay = y;
        float aw =   data.range * 64;
        float ah = myBase.getCurrentHeight() + (hutIndex + 1) * 48 ;
        attackBox = new Rectangle2D.Float(ax, ay, aw, ah);
    }

    public void render(Graphics g) {
        if(isAlly) {
            g.drawImage(image, (int) x, (int) y, (int) width, (int) height, null);
        }else{
            g.drawImage(image, (int) x+width, (int) y, (int) -width, (int) height, null);

        }


//         g.setColor(Color.RED);
//         g.drawRect((int) attackBox.x, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }


    public int getHutIndex() {
        return hutIndex;
    }

    public TurretData getData() {
        return data;
    }
    public Rectangle2D.Float getAttackBox(){
        return attackBox;
    }
//    public Point getBarrelPos() {
//        float barrelX = x + width / 2;
//        float barrelY = y + height / 2;
//        return new Point((int)barrelX, (int)barrelY);
//    }

    public int getID() {
        return id;
    }

    public boolean isAlly() {
        return isAlly;
    }
    public Base getMyBase(){
        return myBase;
    }
}
