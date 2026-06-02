package gamestates;

import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.Game.GAME_HEIGHT;

public class Credits extends State implements Statemethods {
	private BufferedImage backgroundImg, creditsImg;
	private int bgX, bgY, bgW, bgH;
	private float bgYFloat;

	public Credits(Game game) {
		super(game);
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
		creditsImg = LoadSave.GetSpriteAtlas(LoadSave.CREDITS);
		bgW = (int) (creditsImg.getWidth() * Game.SCALE);
		bgH = (int) (creditsImg.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = GAME_HEIGHT;
	}





	@Override
	public void update() {
		bgYFloat -= 1f;
		if(bgYFloat <= -(GAME_HEIGHT+bgH)){
			setGamestate(Gamestate.MENU);
			bgYFloat = 0;
		}

	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, GAME_HEIGHT, null);
		g.drawImage(creditsImg, bgX, (int) (bgY + bgYFloat), bgW, bgH, null);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			bgYFloat = 0;
			setGamestate(Gamestate.MENU);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}



}
