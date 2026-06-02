package entities;

import java.awt.*;

public class HutBlock {
    private int x, y;
    private int width = 48, height = 48;
    private boolean isFree = true;

    public HutBlock(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g) {
        g.setColor(Color.DARK_GRAY); // có thể thay bằng sprite hoặc animation sau
        g.fillRect(x, y, width, height);

    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Getter nếu cần
    public int getX() { return x; }
    public int getY() { return y; }

    public boolean isFree() {
        return isFree;
    }
    public void renderBuyandSell(Graphics g){
        if(isFree){
            g.setColor(Color.GREEN);
            g.drawRect(x-1,y-1,width+2,height+2);
        }else{
            g.setColor(Color.RED);
            g.drawRect(x-1,y-1,width+2,height+2);
        }
    }

    public void setFree(boolean free) {
        isFree = free;
    }
}
