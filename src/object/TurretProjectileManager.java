package object;

import entities.Turret;
import entities.Unit;
import gamestates.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static utilz.Constants.GetSpeed;


public class TurretProjectileManager {

    private Playing playing;
    private ArrayList<TurretProjectile> projectiles = new ArrayList<>();
    private Map<Integer,BufferedImage> turretProject = new HashMap<>();

    public TurretProjectileManager(Playing playing) {
        this.playing = playing;
        preLoadProject();

    }

    public void preLoadProject(){
        int[] ids = {11,12,13,21,22,23,31,32,33,41,42,43,51,52,53};
        for (int id : ids) {
            BufferedImage img = LoadSave.GetSpriteAtlas("units/turret_" + id + ".png");
            turretProject.put(id, img);
        }
    }


    public void newProjectile(Turret t, Unit u) {
        playing.getGame().getAudioPlayer().playEffect(3);
        int xStart = (int) (t.getHitbox().getX() + t.getHitbox().getWidth()/2);
        int xEnd = (int) (u.getHitbox().getX() + u.getHitbox().getWidth()/2);
        int yStart = (int) (t.getHitbox().getY() + t.getHitbox().getHeight()/2);
        int yEnd = (int) (u.getHitbox().getY() + u.getHitbox().getHeight()/2);


        int xDist = xEnd - xStart ;
        int yDist = yEnd - yStart ;
        int totDist = Math.abs(xDist) + Math.abs(yDist);

        float xPer = (float) Math.abs(xDist) / totDist;

        float xSpeed = xPer * GetSpeed(t.getID());
        float ySpeed = GetSpeed(t.getID()) - xSpeed;

        if (xStart > xEnd)
            xSpeed *= -1;
        if (yStart > yEnd)
            ySpeed *= -1;

        float rotate = 0;
//        float arcValue = (float) Math.atan(yDist / (float) xDist);
//        rotate = (float) Math.toDegrees(arcValue);
//        if (xDist < 0) rotate += 180;
        rotate = (float) Math.toDegrees(Math.atan2(yDist, xDist));
//
        boolean isAlly = !u.isAlly();
        if(!u.isAlly()) {
            projectiles.add(new TurretProjectile(xStart, yStart, xSpeed, ySpeed, t.getAttackDamage(), rotate, t.getID(), turretProject.get(t.getID()), isAlly));
            System.out.println(rotate);

        }else{
            projectiles.add(new TurretProjectile(xStart-24, yStart, xSpeed, ySpeed, t.getAttackDamage(), rotate, t.getID(), turretProject.get(t.getID()), isAlly));
            System.out.println(rotate);

        }
    }

    public void update() {
        for (TurretProjectile p : projectiles)
            if (p.isActive()) {
                p.move();
                if (isProjHittingEnemy(p)) {
                    p.setActive(false);
                }
                else if (isProjOutsideBounds(p)) {
                    p.setActive(false);
                }
            }

    }



    private boolean isProjHittingEnemy(TurretProjectile p) {
        for (Unit u : playing.getMangager().getUnits()) {
            if (u.isAlive() ) {
                boolean sameFation = p.isAlly() == u.isAlly();
//                contains(p.getPos().x + p.getWidth()/2,p.getPos().y+p.getHeight()/2)
                if (u.getHitbox().intersects(p.getHitbox()) && !sameFation) {
                    u.takeDamage(p.getDmg());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isProjOutsideBounds(TurretProjectile p) {
        if(p.getPos().y > 350) return true;
        else return false;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (TurretProjectile p : projectiles)
            if (p.isActive()) {
                    int centerX = (int) (p.getPos().x + p.getWidth()/2);
                    int centerY = (int)(p.getPos().y + p.getHeight()/2);
                    g2d.translate(centerX, centerY);
                    g2d.rotate(Math.toRadians(p.getRotation()));
                    g2d.drawImage(p.getImage(), -p.getWidth()/2, -p.getHeight()/2, null);
                    g2d.rotate(-Math.toRadians(p.getRotation()));
                    g2d.translate(-centerX, -centerY);

//                Rectangle hitbox = p.getHitbox();
//                g2d.setColor(Color.RED);
//                g2d.drawRect(hitbox.x , hitbox.y, hitbox.width, hitbox.height);

            }
    }





    public void reset() {
        projectiles.clear();

    }

}
