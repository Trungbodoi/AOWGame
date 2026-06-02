package ui;



import gamestates.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;

import static main.Game.*;


public class BottomBar extends Bar {

	private Playing playing;
	private TopBar topBar;
	private MainStage mainStage;

	private MyButton settings;



	public BottomBar(int x, int y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;

		initButtons();
	}

	public void resetEverything() {


	}

	private void initButtons() {
		settings = new MyButton("Options",0,(int)(GAME_HEIGHT-32*SCALE),(int)(128*SCALE),(int)(GAME_HEIGHT-32*SCALE));



	}
	public void draw(Graphics g) {


        g.setColor(new Color(100, 88, 66));
		g.fillRect(x, y, width, height);

		drawButtons(g);
		drawText(g);


	}

	private void drawText(Graphics g) {
		g.setFont(new Font("Consolas", Font.PLAIN, 14));
		g.setColor(Color.CYAN);
		g.drawString(playing.getPop() +"/10 Pop",GAME_WIDTH/3,  (int)(GAME_HEIGHT-18*SCALE));
		g.setColor(Color.GREEN);
		g.drawString(playing.getXp() +" Xp",(int)(GAME_WIDTH/3+ 128*SCALE ),  (int)(GAME_HEIGHT-18*SCALE));
		g.setColor(Color.yellow);
		g.drawString(playing.getGold() +" Gold",(int)(GAME_WIDTH/3+ 256*SCALE ),  (int)(GAME_HEIGHT-18*SCALE));



	}

	private void drawButtons(Graphics g) {
		settings.draw(g);
	}

	public void mousePressed(MouseEvent e) {
		if (isInOp(e, settings) ){
			playing.setPause(true);



		}

	}

	private boolean isInOp(MouseEvent e, MyButton settings) {
		return settings.getBounds().contains(e.getX(), e.getY());
	}

	public void setMainStage(MainStage mainStage){
		this.mainStage = mainStage;
	}
	public void setTopBar(TopBar topBar){
		this.topBar = topBar;
	}


}
