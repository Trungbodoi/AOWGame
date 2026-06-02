package entities;

import gamestates.Playing;
import object.TurretProjectile;
import utilz.Constants;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TurretManager {
    private List<Turret> turrets = new ArrayList<>();
    private Map<Integer,TurretData>  turretDataMap = new HashMap<>();
    private Map<Integer,BufferedImage> turretIcon = new HashMap<>();
    private Map<Integer,BufferedImage> turretProject = new HashMap<>();
    private List<TurretProjectile> projectiles = new ArrayList<>();

    private Playing playing;
    private Base myBase;
    private Base enemyBase;

    public TurretManager(Playing playing) {
        this.playing = playing;
        loadData();
        preLoadIcon();
        preLoadProject();

    }
    public void preLoadIcon(){
        int[] ids = {11,12,13,21,22,23,31,32,33,41,42,43,51,52,53};

        for (int id : ids) {
            BufferedImage img = LoadSave.GetSpriteAtlas("units/tower_" + id + ".png");
            turretIcon.put(id, img);
        }
    }
    public void preLoadProject(){
        int[] ids = {11,12,13,21,22,23,31,32,33,41,42,43,51,52,53};
        for (int id : ids) {
            BufferedImage img = LoadSave.GetSpriteAtlas("units/turret_" + id + ".png");
            turretProject.put(id, img);
        }
    }

    public void addTurret(int hutIndex,int turretIndex) {
        TurretData data = turretDataMap.get(turretIndex);
        int x = myBase.getHutsId(hutIndex).getBounds().x;
        int y = myBase.getHutsId(hutIndex).getBounds().y;

        Turret turret = new Turret( x,y,data,turretIcon,myBase,hutIndex);
        playing.getGame().getAudioPlayer().playEffect(5);
        turrets.add(turret);
    }
    public void addEnemyTurret(int hutIndex,int turretIndex) {
        TurretData data = turretDataMap.get(turretIndex);
        int x = enemyBase.getHutsId(hutIndex).getBounds().x;
        int y = enemyBase.getHutsId(hutIndex).getBounds().y;

        Turret turret = new Turret( x,y,data,turretIcon,enemyBase,hutIndex);
        System.out.println(turret.isAlly());
        turrets.add(turret);
    }
    private void attackEnemyIfClose(Turret t) {
        for (Unit u : playing.getMangager().getUnits()) {
            boolean sameFation = t.isAlly() == u.isAlly();
            if (u.isAlive() && !sameFation)
                if (isEnemyInRange(t, u)) {
                    if (t.canShoot()) {
                        playing.getMainStage().shootEnemy(t, u);
                        t.resetCooldown();
                    }
                }
        }
    }
    private boolean isEnemyInRange(Turret t, Unit u) {
        return t.getAttackBox().intersects(u.getHitbox());
    }
    public void resetAll(){
        turrets.clear();
        projectiles.clear();
    }

    public void update() {
        for (Turret turret : turrets) {
            turret.update();
            attackEnemyIfClose(turret);

        }

    }

    private void loadData(){
        turretDataMap.put(11,new TurretData(11,8,0.71f,6,Constants.getCost(11)));
        turretDataMap.put(12,new TurretData(12,5,2.6f,6,Constants.getCost(12)));
        turretDataMap.put(13,new TurretData(13,15,2.1f,6,Constants.getCost(13)));
        turretDataMap.put(21,new TurretData(21,18,1.5f,6,Constants.getCost(21)));
        turretDataMap.put(22,new TurretData(22,26,1.2f,6,Constants.getCost(22)));
        turretDataMap.put(23,new TurretData(23,30,0.8f,6,Constants.getCost(23)));
        turretDataMap.put(31,new TurretData(31,15,1.4f,6,Constants.getCost(31)));
        turretDataMap.put(32,new TurretData(32,25,1.2f,6,Constants.getCost(32)));
        turretDataMap.put(33,new TurretData(33,30,1.2f,6,Constants.getCost(33)));
        turretDataMap.put(41,new TurretData(41,25,1.2f,6,Constants.getCost(41)));
        turretDataMap.put(42,new TurretData(42,45,0.74f,6,Constants.getCost(42)));
        turretDataMap.put(43,new TurretData(43,50,0.85f,6,Constants.getCost(43)));
        turretDataMap.put(51,new TurretData(51,55,0.75f,6,Constants.getCost(51)));
        turretDataMap.put(52,new TurretData(52,8,7f,6,Constants.getCost(52)));
        turretDataMap.put(53,new TurretData(53,65,0.72f,6,Constants.getCost(53)));
    }

    public void render(Graphics g) {
        for (Turret turret : turrets) {
            turret.render(g);
        }

    }

    public void setBase(Base base) {
        this.myBase = base;
    }
    public TurretData getPreset(int id) {
        return turretDataMap.get(id);
    }
    public List<Turret> getTurretsList(){
        return turrets;
    }

    public void removeAt(int hutIndex) {
        turrets.removeIf(t -> t.getHutIndex() == hutIndex && t.getMyBase().isAlly());
        playing.getGame().getAudioPlayer().playEffect(5);

//        System.out.println("Removing turret at hutIndex: " + hutIndex);
    }
    public void removeEnemyAt(int hutIndex) {
        turrets.removeIf(t -> t.getHutIndex() == hutIndex && !t.getMyBase().isAlly());
//        System.out.println("Removing turret at hutIndex: " + hutIndex);
    }

    public void setEnemyBase(Base enemyBase) {
        this.enemyBase = enemyBase;
    }
}
