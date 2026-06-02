package ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static main.Game.*;
import static main.Game.SCALE;

public class MyButton {

	public int x, y, width, height, id;
	private String text;
	private Rectangle bounds;
	private boolean mouseOver, mousePressed;


	// For normal Buttons
	public MyButton(String text, int x, int y, int width, int height) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.id = -1;
		createBounds();
	}

	// For tile buttons
	public MyButton(String text, int x, int y, int width, int height, int id) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.id = id;

		createBounds();
	}

	private void createBounds() {
		this.bounds = new Rectangle(x, y, width, height);
	}

	public void draw(Graphics g) {
		// Body
		drawBody(g);
		// Border
		drawBorder(g);
		// Text
		drawText(g);
	}

	private void drawBorder(Graphics g) {
		g.setColor(Color.CYAN);

		g.drawRect(x,y,width,height);

	}

	private void drawBody(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(x, y, width, height);
	}

	private void drawText(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString(text, (int) (x +16*SCALE), (int) (y + 16*SCALE));

	}

	public void resetBooleans() {
		this.mouseOver = false;
		this.mousePressed = false;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public int getId() {
		return id;
	}


}
