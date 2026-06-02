package gamestates;

import entities.Base;
import entities.UnitManager;
import main.Game;
import entities.TurretManager;
import ui.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static main.Game.*;

public class Playing extends State implements Statemethods{
    private TopBar topBar;
    private BottomBar bottomBar;
    private MainStage mainStage;

    private UnitManager mangager;
    private TurretManager turretManager;
    private Base myBase;
    private Base enemyBase;



    private PauseOverlay pauseOverlay;
    private boolean paused = false;
    private boolean gameCompleted;
    private boolean gameOver;

    private GameOverOverlay gameOverOverlay;
    private GameCompletedOverlay gameCompletedOverlay;



    private int pop = 0,xp = 100000,gold = 150000;
    private int age = 1;
    private int enemyAge = 1;
    private boolean evolve = false;
    private final int  MAXPOP = 10;




    public Playing(Game game) {
        super(game);
        initClasses();


    }
    private void initClasses() {
        mangager = new UnitManager(this);
        turretManager = new TurretManager(this);

        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        gameCompletedOverlay = new GameCompletedOverlay(this);



        bottomBar = new BottomBar(0,(int)(GAME_HEIGHT-32*SCALE),GAME_WIDTH,(int)(GAME_HEIGHT-32*SCALE),this);
        mainStage = new MainStage(0,(int) (GAME_HEIGHT/5.6),(int) (GAME_HEIGHT - 32 * SCALE) - (int) (GAME_HEIGHT / 5.6),this);
        topBar = new TopBar(0,0,GAME_WIDTH, (int) (GAME_HEIGHT/5.6),this);



        topBar.setBottomBar(bottomBar);
        topBar.setMainStage(mainStage);

        mainStage.setTopBar(topBar);
        mainStage.setBottomBar(bottomBar);
        mainStage.setTurretManager(turretManager);

        bottomBar.setTopBar(topBar);
        bottomBar.setMainStage(mainStage);

        myBase = mainStage.getBase();
        enemyBase = mainStage.getEnemyBase();
        turretManager.setBase(myBase);
        turretManager.setEnemyBase(enemyBase);

        mainStage.getEnemyAi().settingAi();
    }
    @Override
    public void update() {
        if (paused)
            pauseOverlay.update();
        else if (gameCompleted)
            gameCompletedOverlay.update();
        else if (gameOver)
            gameOverOverlay.update();
        else{
            topBar.update();
            mangager.update();
            mainStage.update();
            if(evolve) {
                topBar.evo();
                evolve = false;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        bottomBar.draw(g);
        mainStage.draw(g);
        topBar.draw(g);

        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        }else if (gameOver)
            gameOverOverlay.draw(g);
        else if (gameCompleted)
            gameCompletedOverlay.draw(g);


    }


    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void mouseDragged(MouseEvent e) {
            if (paused)
                pauseOverlay.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (gameOver)
            gameOverOverlay.mousePressed(e);
        else
            if (paused)
            pauseOverlay.mousePressed(e);

        else if (gameCompleted)
            gameCompletedOverlay.mousePressed(e);
        else{
                topBar.mousePressed(e);
                mainStage.mousePressed(e);
                bottomBar.mousePressed(e);
            }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mouseReleased(e);
        else
            if (paused)
            pauseOverlay.mouseReleased(e);
        else if (gameCompleted)
            gameCompletedOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameOver)
            gameOverOverlay.mouseMoved(e);
        else
            if (paused)
            pauseOverlay.mouseMoved(e);
        else if (gameCompleted)
            gameCompletedOverlay.mouseMoved(e);
        else {
                topBar.mouseMoved(e);
                mainStage.mouseMoved(e);
            }

    }
    public void unpauseGame() {
        paused = false;
    }

    public int getAge() {
        return age;
    }

    public void evo() {
        age++;
        evolve = true;
    }
    public void setPause(boolean paused){
        this.paused = paused;
    }

    public int getMAXPOP() {
        return MAXPOP;
    }
    public BottomBar getBottomBar(){
        return bottomBar;
    }
    public UnitManager getMangager(){
        return mangager;
    }
    public TurretManager getTurretManager(){ return turretManager;}

    public MainStage getMainStage() {
        return mainStage;
    }
    public Base getMyBase(){
        return myBase;
    }
    public Base getEnemyBase(){
        return enemyBase;
    }
    public int getPop(){
        return pop;
    }
    public int getXp(){
        return xp;
    }
    public int getGold(){
        return gold;
    }
    public void setGameCompleted() {
        gameCompleted = true;
    }

    public void resetGameCompleted() {
        gameCompleted = false;
    }

    public void resetAll() {
        gameOver = false;
        paused = false;
        age = 1;
        enemyAge = 1;
        gold = 1500;
        xp = 1000;
        pop = 0;
        mangager.resetAll();
        topBar.resetEverything();
        mainStage.resetAll();
        turretManager.resetAll();

    }

    public void increaseGold(double cost){
        gold+=cost;
    }
    public void increaseXp(double cost){
        xp+=cost;
    }


    public void increasePop() {
        pop++;
    }

    public void decreasePop() {
        pop--;
    }

    public int getEnemyAge() {
        return enemyAge;
    }

    public void setEnemyAge(int enemyAge) {
        this.enemyAge = enemyAge;
    }

    public void decreaseGold(int cost) {
        gold -= cost;
    }

    public void decreaseXp(int xpNeed) {
        this.xp-=xpNeed;
    }

    public void setGameOver(boolean b) {
        this.gameOver = b;
    }

}
