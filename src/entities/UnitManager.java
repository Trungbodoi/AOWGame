package entities;

import gamestates.Playing;
import object.ProjectileManager;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

import static utilz.Constants.UnitConstants.ATTACK;

public class UnitManager {

    private Map<Integer, UnitData> unitDataMap = new HashMap<>();
    private Map<Integer, BufferedImage[]> idleAniCache = new HashMap<>();
    private Map<Integer, BufferedImage[]> runningAniCache = new HashMap<>();
    private Map<Integer, BufferedImage[]> attackAniCache = new HashMap<>();
    private Playing playing;
    private ProjectileManager projectileManager;

    private static List<Unit> units = new ArrayList<>();

    public UnitManager(Playing playing) {
        this.playing = playing;
        loadUnitData();
        preloadAnimations();
        projectileManager = new ProjectileManager();
        projectileManager.setPlaying(this.playing);

    }


    public void addUnit(float x, float y,  int index , boolean isAlly) {
        UnitData data = unitDataMap.get(index);

        Unit unit = new Unit(x, y,  data, index,
                idleAniCache, runningAniCache, attackAniCache,isAlly);
        units.add(unit);
        if(isAlly){
            playing.getGame().getAudioPlayer().playEffect(4);

        }
    }

    public void update() {
        setAllUnitsMoving();                    // B1
        handleCollisionsAndCombat();           // B2
        updateAndRemoveDeadUnits();            // B3 + B4
        projectileManager.update(units);       // Cập nhật projectile
    }

    private void setAllUnitsMoving() {
        for (Unit unit : units) {
            unit.setMoving(true);
        }
    }
    private void handleCollisionsAndCombat() {
        for (int i = 0; i < units.size(); i++) {
            Unit u1 = units.get(i);
            for (int j = i + 1; j < units.size(); j++) {
                Unit u2 = units.get(j);
                handleUnitInteraction(u1, u2);
            }
        }
    }
    private void handleUnitInteraction(Unit u1, Unit u2) {
        Rectangle2D.Float hb1 = u1.getHitbox();
        Rectangle2D.Float ab1 = u1.getAttackBox();
        Rectangle2D.Float ab2 = u2.getAttackBox();
        Rectangle2D.Float hb2 = u2.getHitbox();

        boolean sameFaction = u1.isAlly() == u2.isAlly();
        boolean isInFront1 = u1.isAlly() ? hb2.x > hb1.x : hb1.x > hb2.x;
        boolean isInFront2 = u2.isAlly() ? hb1.x > hb2.x : hb2.x > hb1.x;

        // Unit 1 bắn unit 2
        if (u1.getAttackRange() > 0 && ab1.intersects(hb2) && !sameFaction)
            handleRangedAttack(u1, u2);
        // Unit 2 bắn unit 1
        if (u2.getAttackRange() > 0 && ab2.intersects(hb1) && !sameFaction)
            handleRangedAttack(u2, u1);

        // Nếu va chạm
        if (hb1.intersects(hb2)) {
            if (isInFront1) u1.setMoving(false);
            if (isInFront2) u2.setMoving(false);

            if (!sameFaction) {
                // Melee units attack if in contact
                if (u1.getAttackRange() == 0)
                    handleMeleeCombat(u1, u2);
                if (u2.getAttackRange() == 0)
                    handleMeleeCombat(u2, u1);
            }
        }
    }
    private void handleRangedAttack(Unit attacker, Unit target) {
        if (attacker.getAction() != ATTACK && attacker.attackFrameCounter <= 0) {
            attacker.setAttacking(true);
        } else if (attacker.getAction() == ATTACK && attacker.attackFrameCounter == attacker.remainingFrame()) {
            projectileManager.spawnProjectile(attacker, attacker.getIndex(), attacker.getAttackDamage());
        }
    }
    private void handleMeleeCombat(Unit u1, Unit u2) {
        if (u1.getAction() != ATTACK && u1.attackFrameCounter < 0)
            u1.setAttacking(true);
        else if (u1.getAction() == ATTACK && u1.attackFrameCounter == u1.remainingFrame()) {
            u2.takeDamage(u1.getAttackDamage());
        }
//        if (u2.getAction() != ATTACK && u2.attackFrameCounter < 0)
//            u2.setAttacking(true);
//        else if (u2.getAction() == ATTACK && u2.attackFrameCounter == u2.remainingFrame()) {
//            u1.takeDamage(u2.getAttackDamage());
//
//        }
    }
    private void updateAndRemoveDeadUnits() {
        Iterator<Unit> iter = units.iterator();
        while (iter.hasNext()) {
            Unit unit = iter.next();
            if (!unit.isDead()) {
                checkBaseCombat(unit);
            }
            unit.update();
            if (unit.isDead()) {
                if(!unit.isAlly()) {
                    playing.increaseGold(unit.getCost()*1.3);
                    playing.increaseXp(unit.getCost()*1.75);
                    playing.getMainStage().decreasePop();
                }else{
                    playing.decreasePop();
                }
                playing.getGame().getAudioPlayer().playEffect(1);
                iter.remove();
            }
        }
    }

    private void checkBaseCombat(Unit unit) {
        Base enemyBase = unit.isAlly() ? playing.getEnemyBase() : playing.getMyBase();
        Rectangle2D baseBox = enemyBase.getBounds();

        // Nếu unit đánh xa thì kiểm tra attackBox
        if (unit.getAttackRange() > 0) {
            if (unit.getAttackBox().intersects(baseBox)) {
                if(unit.getHitbox().intersects(baseBox))
                unit.setMoving(false);

                if (unit.getAction() != ATTACK && unit.attackFrameCounter <= 0) {
                    unit.setAttacking(true);
                } else if (unit.getAction() == ATTACK && unit.attackFrameCounter == unit.remainingFrame()) {
                    projectileManager.spawnProjectile(unit, unit.getIndex(), unit.getAttackDamage());
                }
            }
        }
        // Nếu unit đánh gần thì kiểm tra va chạm hitbox
        else {
            if (unit.getHitbox().intersects(baseBox)) {
                unit.setMoving(false);

                if (unit.getAction() != ATTACK && unit.attackFrameCounter <= 0) {
                    unit.setAttacking(true);
                } else if (unit.getAction() == ATTACK && unit.attackFrameCounter == unit.remainingFrame()) {
                    enemyBase.takeDamage(unit.getAttackDamage());
                }
            }
        }
    }




    public void render(Graphics g) {
        for (Unit unit : new ArrayList<>(units)) {
            unit.render(g);
        }
        projectileManager.render(g);
    }


    private void loadUnitData() {
        // Load các UnitData vào unitDataMap
        unitDataMap.put(11, new UnitData(11,24,0, 4, 5, 13, 18, 20, 140, 25, 0.66f, 0, 1f, 1, 100, "BloodFrog"));
        unitDataMap.put(12, new UnitData(12,11,0, 2, 0, 2, 3, 10, 100, 15, 0.51f, 4, 0.8f, 1.5f, 125, "FlyingSnail"));
        unitDataMap.put(13, new UnitData(13,21,0, 0, 2, 7, 14, 20, 175, 30, 0.94f, 0, 1.2f, 2.5f,200 , "Hemogoblin"));
        unitDataMap.put(14, new UnitData(14,21,0, 0, 1, 8, 9, 20, 600, 35, 0.4f, 0, 0.7f, 4f, 400, "StoneGolem"));
        unitDataMap.put(21, new UnitData(21,21,0, 0, 7, 20, 2, 2, 140, 15, 0.56f, 7 , 0.8f, 1.5f, 125,"Sniper"));
        unitDataMap.put(22, new UnitData(22,21,0, 0, 7, 20, 2, 2, 210, 20, 0.72f, 0, 1f, 1, 125, "Shotgun"));
        unitDataMap.put(23, new UnitData(23,20,1, 1, 6, 19, 3, 3, 300, 30, 0.9f, 5f, 0.7f, 2.5f, 200, "Commander"));
        unitDataMap.put(24, new UnitData(24,12,0, 0, 2, 8, 9, 11, 400, 35, 0.85f, 5f, 0.6f, 4, 400, "Paladin"));
        unitDataMap.put(31, new UnitData(31,18,0, 0, 4, 17, 1, 2, 230, 35, 0.51f, 0, 0.8f, 1, 100, "Psycho"));
        unitDataMap.put(32, new UnitData(32,20,0, 0, 2, 15, 16, 19, 200, 20, 0.69f, 7f, 1.6f, 1.5f, 125, "MadScientist"));
        unitDataMap.put(33, new UnitData(33,16,0, 0, 2, 15, 13, 14, 350, 4, 8, 0, 1.2f, 2.5f, 200, "Butcher"));
        unitDataMap.put(34, new UnitData(34,12,0, 11, 0, 11, 0, 11, 400, 35, 0.76f, 0f, 1.5f, 4, 400, "Pumpking"));
        unitDataMap.put(41, new UnitData(41,14,7, 7, 0, 7, 9, 13, 350, 23, 1f, 0f, 1.2f, 1f, 100, "Goblin"));
        unitDataMap.put(42, new UnitData(42,17,0, 0, 2, 9, 10, 13, 250, 25, 0.57f, 7f, 1.2f, 1.5f, 125, "Bomber"));
        unitDataMap.put(43, new UnitData(43,14,0, 0, 0, 7, 9, 13, 400, 45, 0.63f, 6f, 1f, 2.5f, 200, "Javelin"));
        unitDataMap.put(44, new UnitData(44,16,0, 0, 2, 9, 10, 15, 435, 50, 0.56f, 5f, 1.5f, 4f, 400, "Drakin"));
        unitDataMap.put(51, new UnitData(51,17,0, 0, 9,16 , 1, 8, 350, 50, 0.6f, 0f, 1f, 1, 100, "Selenian"));
        unitDataMap.put(52, new UnitData(52,13,0, 0 , 2, 9, 10, 12, 250, 3, 8, 7f, 1f, 1.5f,125, "Storm Diver"));
        unitDataMap.put(53, new UnitData(53,12,0, 0, 2, 9, 10, 11,280 , 45, 0.54f, 9f, 0.5f, 2.5f, 200, "Predictor"));
        unitDataMap.put(54, new UnitData(54,19,0, 7, 10, 11, 12, 18, 400, 60, 0.71f, 0, 1.2f, 4, 400, "Star Guardian"));
    }

    private void preloadAnimations() {
        for (UnitData data : unitDataMap.values()) {
            int id = data.id;
            if (idleAniCache.containsKey(id)) continue;

            // Load ảnh từ file: units/unit_<id>.png
            BufferedImage img = LoadSave.GetSpriteAtlas("units/sprites_" + id + ".png");

            int spriteHeight = img.getHeight() / data.numSprites;
            int spriteWidth = img.getWidth();

            BufferedImage[] idleAni = new BufferedImage[data.idleEnd - data.idleStart + 1];
            BufferedImage[] runningAni = new BufferedImage[data.runEnd - data.runStart + 1];
            BufferedImage[] attackAni = new BufferedImage[data.atkEnd - data.atkStart + 1];

            for (int i = 0; i < idleAni.length; i++)
                idleAni[i] = img.getSubimage(0, (data.idleStart + i) * spriteHeight, spriteWidth, spriteHeight);
            for (int i = 0; i < runningAni.length; i++)
                runningAni[i] = img.getSubimage(0, (data.runStart + i) * spriteHeight, spriteWidth, spriteHeight);
            for (int i = 0; i < attackAni.length; i++)
                attackAni[i] = img.getSubimage(0, (data.atkStart + i) * spriteHeight, spriteWidth, spriteHeight);

            idleAniCache.put(id, idleAni);
            runningAniCache.put(id, runningAni);
            attackAniCache.put(id, attackAni);
        }
    }

    public void resetAll() {
        units.clear();
        projectileManager.reset();
    }
    public UnitData getPreset(int id) {
        return unitDataMap.get(id);
    }


    public List<Unit> getUnits(){
        return units;
    }

}
