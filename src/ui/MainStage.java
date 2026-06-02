package ui;

import entities.Base;
import entities.HutBlock;
import entities.Unit;
import entities.UnitManager;
import gamestates.Playing;
import entities.Turret;
import entities.TurretManager;
import object.TurretProjectileManager;
import utilz.Constants;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import static main.Game.*;

public class MainStage {
    private Playing playing;
    private TopBar topBar;
    private BottomBar bottomBar;
    private UnitManager unitManager;
    private TurretManager turretManager;
    private TurretProjectileManager turretProjectileManager;
    private EnemyAi enemyAi;


    private Base myBase;
    private Base enemyBase;
    private int selectTurret = 0;


    private int x, y;
    private int height;
    private Image bgImg;
    private Image bgImg1 = LoadSave.GetSpriteAtlas("Background_1.png");
    private Image bgImg2 = LoadSave.GetSpriteAtlas("Background_2.png");
    private Image bgImg3 = LoadSave.GetSpriteAtlas("Background_3.png");
    private Image bgImg4 = LoadSave.GetSpriteAtlas("Background_4.png");
    private Image bgImg5 = LoadSave.GetSpriteAtlas("Background_5.png");
    private double levelWidth = GAME_WIDTH * 2.5;
    private int cameraX = 0;
    private int scrollDirection = 0;
    private final int scrollSpeed = 3;
    private final int scrollThreshold = 50;
    private int bgIndex = 1;


    public MainStage(int x, int y, int height, Playing playing) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.playing = playing;
        this.unitManager = playing.getMangager();
        this.turretManager = playing.getTurretManager();
        this.turretProjectileManager = new TurretProjectileManager(playing);
        myBase = new Base(0, (float) ((y + height) / 1.8), 300, 100,true);
        enemyBase = new Base((float)(levelWidth-300), (float) ((y + height) / 1.8), 300, 100,false);
        enemyAi = new EnemyAi(playing);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldTransform = g2d.getTransform();

        g2d.translate(-cameraX, 0);
        drawBg(g);
        drawRoad(g);
        myBase.render(g);
        enemyBase.render(g);
        unitManager.render(g);
        turretManager.render(g);
        turretProjectileManager.draw(g);
        if (topBar.isBuying() || topBar.isSelling()) {
            for (HutBlock h : myBase.getHuts()) {
                h.renderBuyandSell(g);
            }
        }


        g2d.setTransform(oldTransform);

        myBase.drawHealthBar(g);
        enemyBase.drawHealthBar(g);


    }

    public void update() {
        if (scrollDirection == 1 && cameraX < levelWidth - GAME_WIDTH) {
            cameraX += scrollSpeed;
        } else if (scrollDirection == -1 && cameraX > 0) {
            cameraX -= scrollSpeed;
        }
        if(myBase.getCurrentHealth() == 0 ){
            playing.setGameOver(true);
            playing.getGame().getAudioPlayer().stopSong();
        }else if(enemyBase.getCurrentHealth() == 0 ){
            playing.setGameCompleted();
            playing.resetAll();
            return;
        }
        turretManager.update();
        turretProjectileManager.update();
        enemyAi.updateAI();

    }

    public void drawBg(Graphics g) {
        int age =playing.getAge();
        int enemyAge = playing.getEnemyAge();
        int bgAge = Math.max(age, enemyAge);
        switch (bgAge){
            case 1 ->{
                bgImg = bgImg1;
            }
            case 2 ->{
                bgImg = bgImg2;
            }
            case 3 ->{
                bgImg = bgImg3;
            }
            case 4 ->{
                bgImg = bgImg4;
            }
            case 5 ->{
                bgImg = bgImg5;
            }
        }

        int w = bgImg.getWidth(null);
        int h = bgImg.getHeight(null);
        double scale = (double) height / h;
        int newWidth = (int) (w * scale);
        for (int x = 0; x < levelWidth; x += newWidth) {
            g.drawImage(bgImg, x, y, newWidth, height, null);

        }

    }

    public void drawRoad(Graphics g) {
        g.setColor(new Color(168, 147, 36));
        g.fillRect(x, height * 2 / 3 + y, (int) (levelWidth), (int) (TILES_DEFAULT_SIZE * SCALE));


    }


    public void mouseMoved(MouseEvent e) {
        int mouseX = e.getX();
        boolean isIn = e.getY() > y && e.getY() < y + height;
        if (isIn) {
            if (mouseX > GAME_WIDTH - scrollThreshold && cameraX < levelWidth - GAME_WIDTH) {
                scrollDirection = 1;
            } else if (mouseX < scrollThreshold && cameraX > 0) {
                scrollDirection = -1;
            } else {
                scrollDirection = 0;
            }
        }
    }

    public void spawn(int unitIndex) {
        unitManager.addUnit(myBase.getBounds().x + myBase.getBounds().width, myBase.getBounds().y + myBase.getBounds().height, unitIndex, true);

//        unitManager.addUnit(enemyBase.getBounds().x, enemyBase.getBounds().y +enemyBase.getBounds().height, unitIndex, false);
    }

    public void setBottomBar(BottomBar bottomBar) {
        this.bottomBar = bottomBar;
    }

    public void setTopBar(TopBar topBar) {
        this.topBar = topBar;
    }

    public Base getBase() {
        return myBase;
    }
    public Base getEnemyBase() {
        return enemyBase;
    }

    public void resetAll() {
        myBase.resetAll();
        enemyBase.resetAll();
        enemyAi.resetAi();
    }

    public TurretManager getTurretManager() {
        return turretManager;
    }

    public void setTurretManager(TurretManager turretManager) {
        this.turretManager = turretManager;
    }

    private boolean isInTurretButton(MouseEvent e, TurretButton turretButton) {
        return turretButton.getBounds().contains(e.getX(), e.getY());
    }

    private boolean isInHut(MouseEvent e, HutBlock h) {
        return h.getBounds().contains(e.getX() + cameraX, e.getY());
    }

    public void mousePressed(MouseEvent e) {
        if(selectTurret != 0 && topBar.isBuying()){
        ArrayList<HutBlock> huts = myBase.getHuts();
        for (int i = 0; i < huts.size(); i++) {
            if (isInHut(e, huts.get(i)) && huts.get(i).isFree()) {
                turretManager.addTurret(i, selectTurret);
                huts.get(i).setFree(false);
                playing.getMyBase().placeTurret();
                topBar.setBuying(false);
                playing.decreaseGold(Constants.getCost(selectTurret));
            }
        }
        }
        if(topBar.isSelling()){
            ArrayList<HutBlock> huts = myBase.getHuts();
            for(int i = 0; i < huts.size(); i++){
                if(!huts.get(i).isFree() && isInTurret(e,i)){
                    playing.getMyBase().freeTurret();
                    System.out.println("Selling turret at hut: " + i);
                    turretManager.removeAt(i);
                    huts.get(i).setFree(true);
                    topBar.setSelling(false);
                }
            }
        }
    }
    public void setSelectTurret(int index){
        this.selectTurret = index;
    }
    public boolean isInTurret(MouseEvent e, int hutIndex) {
        for (Turret t : turretManager.getTurretsList()) {

            // Không thay đổi hitbox
            if (t.getHutIndex() == hutIndex && t.getHitbox().contains(e.getX() + cameraX, e.getY())) {
                return true;
            }

        }
        return false;
    }
    public void shootEnemy(Turret t, Unit u) {
        turretProjectileManager.newProjectile(t,u);

    }

    public void decreasePop() {
        enemyAi.decreasePop();
    }
    public EnemyAi getEnemyAi(){
        return enemyAi;
    }
}





