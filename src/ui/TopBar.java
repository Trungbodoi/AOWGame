package ui;

import entities.UnitData;
import gamestates.Playing;
import entities.TurretData;
import utilz.Constants;

import java.awt.*;
import java.awt.event.MouseEvent;

import static main.Game.GAME_WIDTH;
import static main.Game.SCALE;


public class TopBar extends Bar {

	private Playing playing;
	private BottomBar bottomBar;
	private MainStage mainStage;

	private UnitButton[] unitButtons;
	private TurretButton[] turretButtons;
	private SkillButton skillButton;
	private EvoButton evoButton;
	private TurretButton buyHut;


	private boolean isBuying = false;
	private boolean isSelling = false;
	private Point mousePos = null;
	private TurretButton hoveredTurret = null;
	private UnitButton hoveredUnit = null;
	private boolean mouseInBuyHut = false;
	private boolean mouseInEvo = false;






	public TopBar(int x, int y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;


		initButtons();
	}

	public void resetEverything() {
		for(UnitButton u : unitButtons){
			u.setIndex(1);
		}
		for(TurretButton t : turretButtons){
			t.setIndex(1);
		}
		skillButton.setIndex(1);


	}
	public void update(){
		for(UnitButton u : unitButtons){
			if(!u.canSpawn()){
				u.decreaseCooldown();
			}else if(u.canSpawn() && u.isSpawning()){
				u.setSpawning(false);
				mainStage.spawn(u.getUnitIndex());
			}
		}
	}

	private void initButtons() {
		unitButtons = new UnitButton[4];
		turretButtons = new TurretButton[5];
		int w = (int) (48*SCALE);
		int h = (int) (48*SCALE);
		int xStart = (int) (12*SCALE) ;
		int yStart = (int) (24*SCALE);
		int xOffset = (int) (w*1.05f);

		for (int i = 0; i < unitButtons.length; i++)
			unitButtons[i] = new UnitButton(xStart + xOffset * i, yStart, w, h, i);
		for (int i = 0; i < turretButtons.length; i++)
			turretButtons[i] = new TurretButton(GAME_WIDTH/3+xStart + xOffset * i, yStart, w, h, i);

		evoButton = new EvoButton(2*GAME_WIDTH/3+xStart , yStart, w, h);
		skillButton = new SkillButton(2*GAME_WIDTH/3+xStart + xOffset, yStart, w, h);

		for(UnitButton unitButton : unitButtons){
			unitButton.setIndex(playing.getAge());
		}
		for(TurretButton turretButton : turretButtons){
			turretButton.setIndex(playing.getAge());
		}
		evoButton.setIndex(playing.getAge());
		skillButton.setIndex(playing.getAge());
		buyHut = turretButtons[3];

	}
	public void evo(){
		playing.decreaseXp(Constants.getXpNeed(playing.getAge()-1));
		for(UnitButton unitButton : unitButtons){
			unitButton.setIndex(playing.getAge());
		}
		for(TurretButton turretButton : turretButtons){
			turretButton.setIndex(playing.getAge());
		}
		evoButton.setIndex(playing.getAge());
		skillButton.setIndex(playing.getAge());
		mainStage.getBase().evo();
	}
	public void draw(Graphics g) {

		g.setColor(new Color(100, 88, 66));
		g.fillRect(x, y, width, height);
		drawButtons(g);
		drawText(g);
		drawUnitProgressOverlay(g);
		drawTurretOverlay(g);
		drawBuyHutOverLay(g);
		drawEvoOverLay(g);

		if (hoveredTurret != null && mousePos != null) {
			drawTurretTooltip(g, mousePos.x, mousePos.y, hoveredTurret);
		}
		if(hoveredUnit!= null && mousePos!= null){
			drawUnitTooltip(g,mousePos.x,mousePos.y,hoveredUnit);
		}
		if(mouseInBuyHut && mousePos!= null)
			drawBuyHutTooltip(g,mousePos.x,mousePos.y);
		if(mouseInEvo && mousePos!= null)
			drawEvoTooltip(g,mousePos.x,mousePos.y);



	}



	private void drawText(Graphics g) {
		g.setFont(new Font("Consolas", Font.PLAIN, 14));
		g.setColor(Color.WHITE);
		g.drawString("Units",(int) (12*SCALE),  (int) (18*SCALE));
		g.drawString("Turrets",(int) (GAME_WIDTH/3 +12*SCALE),  (int) (18*SCALE));
		g.drawString("Specials",(int) (2*GAME_WIDTH/3 +12*SCALE),  (int) (18*SCALE));


	}

	private void drawButtons(Graphics g) {
		for (int i = 0; i < unitButtons.length; i++){
			unitButtons[i].draw(g);
		}
		for (int i = 0; i < turretButtons.length; i++) {
			turretButtons[i].draw(g);

		}
		skillButton.draw(g);

		evoButton.draw(g);
	}
	public void mouseMoved(MouseEvent e) {
		mousePos = e.getPoint();
		hoveredTurret = null;
		hoveredUnit = null;
		mouseInEvo = false;
		mouseInBuyHut = false;
		for (int i=0;i<3;i++) {
			if (isInTurretButton(e, turretButtons[i])) {
				hoveredTurret = turretButtons[i];
				break;
			}
		}
		for(UnitButton u : unitButtons){
			if(isInUnit(e,u)){
				hoveredUnit = u;
				break;
			}
		}
		if(isInBuyHut(e)){
			mouseInBuyHut = true;
		}
		if(isInEvo(e)){
			mouseInEvo = true;
		}

	}

	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3){
			if(isBuying ){
				setBuying(false);
			}
			if(isSelling){
				setSelling(false);
			}
		}
		if (isInEvo(e) && playing.getAge() < 5 && playing.getXp() >= Constants.getXpNeed(playing.getAge())){
			playing.evo();
		}
		if(playing.getPop()<playing.getMAXPOP()  ) {
			for (UnitButton u : unitButtons) {
				if (isInUnit(e, u) && u.canSpawn() && playing.getGold() >= Constants.getUnitCost(u.getUnitIndex()))  {
					playing.increasePop();
					playing.decreaseGold(Constants.getUnitCost(u.getUnitIndex()));
					u.Spawn();
				}
			}
		}
		if(isInBuyHut(e) && playing.getMyBase().getHuts().size() <3 && playing.getGold() >= Constants.getGoldNeeded(playing.getMyBase().getHuts().size()+1)){
			playing.decreaseGold(Constants.getGoldNeeded(playing.getMyBase().getHuts().size()+1));
			playing.getMyBase().addHut();
		}
		if(isInSellTurret(e) && mainStage.getTurretManager().getTurretsList().size() > 0){
			setSelling(true);
			setBuying(false);
		}

		for(int i =0 ;i< 3;i++){
			if(isInTurretButton(e,turretButtons[i]) && mainStage.getBase().getFreeSpace() >0 && playing.getGold() >= Constants.getCost(turretButtons[i].getTurretIndex())){
				setBuying(true);
				setSelling(false);
				mainStage.setSelectTurret(turretButtons[i].getTurretIndex());
			}
		}


	}

	private boolean isInEvo(MouseEvent e) {
		return evoButton.getBounds().contains(e.getX(), e.getY());
	}
	private boolean isInUnit(MouseEvent e,UnitButton unitButton) {
		return unitButton.getBounds().contains(e.getX(), e.getY());
	}
	private boolean isInBuyHut(MouseEvent e) {
		return buyHut.getBounds().contains(e.getX(), e.getY());
	}
	private boolean isInSellTurret(MouseEvent e) {
		return turretButtons[4].getBounds().contains(e.getX(), e.getY());
	}
	private boolean isInTurretButton(MouseEvent e,TurretButton turretButton) {
		return turretButton.getBounds().contains(e.getX(), e.getY());
	}


	public void setBottomBar(BottomBar bottomBar){
		this.bottomBar = bottomBar;
	}
	public void setMainStage(MainStage mainStage){
		this.mainStage = mainStage;
	}


    public boolean isBuying() {
        return isBuying;
    }

    public void setBuying(boolean buying) {
        isBuying = buying;
    }

    public boolean isSelling() {
        return isSelling;
    }

    public void setSelling(boolean selling) {
        isSelling = selling;
    }
	private void drawTurretTooltip(Graphics g, int x, int y, TurretButton b) {
		TurretData data = playing.getTurretManager().getPreset(b.getTurretIndex());

		g.setColor(new Color(0, 0, 0, 200));
		g.fillRoundRect(x, y, 120, 70, 10, 10);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 12));
		g.drawString("Id: " + data.id, x + 10, y + 20);
		g.drawString("DMG: " + data.damage, x + 10, y + 35);
		g.drawString("Range: " + data.range, x + 10, y + 50);
		g.drawString("Cost: " + data.cost, x + 10, y + 65);
	}

	private void drawUnitTooltip(Graphics g, int x, int y, UnitButton b) {
		UnitData data = playing.getMangager().getPreset(b.getUnitIndex());

		g.setColor(new Color(0, 0, 0, 200));
		g.fillRoundRect(x, y, 220, 70, 10, 10);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 12));
		g.drawString("Id: " + data.id, x + 10, y + 20);
		g.drawString("Name: " + data.name, x + 100, y + 20);
		g.drawString("DMG: " + data.atkDmg, x + 10, y + 35);
		g.drawString("Hp: " + data.maxHp, x + 100, y + 35);
		g.drawString("Range: " + data.atkRange, x + 10, y + 50);
		g.drawString("AtkSpeed: " + data.atkSpeed, x + 100, y + 50);
		g.drawString("Cost: " + data.cost, x + 10, y + 65);
		g.drawString("Build: " + data.buildTime, x + 100, y + 65);

	}
	private void drawUnitProgressOverlay(Graphics g) {
		for (UnitButton u : unitButtons) {
			if(!u.canSpawn()) {
				g.setColor(new Color(0, 0, 0, 126));
				float percent = (float) u.getRemainingFrame() /u.getSpawnFrame();
				g.fillRect(u.x, u.y, u.width, (int) (u.height * percent));
			}
		}
	}
	private void drawTurretOverlay(Graphics g){
		for(int i = 0;i<3;i++){
			if( playing.getGold() < Constants.getCost(turretButtons[i].getTurretIndex()) || playing.getMyBase().getFreeSpace() == 0){
				g.setColor(new Color(0, 0, 0, 126));
				TurretButton t = turretButtons[i];
				g.fillRect(t.x,t.y,t.width,t.height);
			}
		}
	}
	private void drawEvoOverLay(Graphics g ){
		if(playing.getXp() < Constants.getXpNeed(playing.getAge()) || playing.getAge() == 5){
			g.setColor(new Color(0, 0, 0, 126));
			g.fillRect(evoButton.x,evoButton.y,evoButton.width,evoButton.height);
		}
	}
	private void drawEvoTooltip(Graphics g, int x, int y) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRoundRect(x, y, 120, 30, 10, 10);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 12));
		if(playing.getAge() <5){
			g.drawString("Xp needed: " + Constants.getXpNeed(playing.getAge()) , x + 10, y + 20);
		}else{
			g.drawString("Max age reached " , x + 10, y + 20);
		}

	}
	private void drawBuyHutOverLay(Graphics g ){
		if(playing.getGold() < Constants.getGoldNeeded(playing.getMyBase().getHuts().size()+1) || playing.getMyBase().getHuts().size() ==3){
			g.setColor(new Color(0, 0, 0, 126));
			g.fillRect(buyHut.x,buyHut.y,buyHut.width,buyHut.height);
		}
	}
	private void drawBuyHutTooltip(Graphics g, int x, int y) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRoundRect(x, y, 120, 30, 10, 10);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 12));
		if(playing.getMyBase().getHuts().size() <3){
			g.drawString("Gold needed: " + Constants.getGoldNeeded(playing.getMyBase().getHuts().size()+1) , x + 10, y + 20);
		}else{
			g.drawString("Max huts reached " , x + 10, y + 20);
		}

	}




}
