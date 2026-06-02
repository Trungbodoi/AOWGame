package entities;

import audio.AudioPlayer;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Unit extends Entity {

    private BufferedImage[] idleAni, runningAni, attackAni;
    private int aniTick, aniIndex, action = IDLE;
    private boolean moving, attacking;
    private boolean isAlly = true;
    private int aniSpeed = 25;
    private int attackAniSpeed;
    private int attackFrame ;
    public int attackFrameCounter = 0;
    private int index;
    private int xOffset,yOffset;
    private float trueX,trueY;

    private boolean tookDamage = false;
    private int damageEffectTick = 0;
    private final int damageEffectDuration = 20; // số tick hiệu ứng kéo dài
    private boolean dying = false;
    private float alpha = 1.0f; // độ trong suốt từ 1.0 (rõ) đến 0 (mờ hẳn)
    private float floatY = 0;// khoảng bay lên
    private boolean completed = false;



    public Unit(float x, float y,  UnitData data, int index,
                Map<Integer, BufferedImage[]> idleCache, Map<Integer, BufferedImage[]> runCache, Map<Integer, BufferedImage[]> atkCache
                ,boolean isAlly) {

        this.isAlly = isAlly;
        this.trueX = x;
        this.trueY = y;

        this.index = index;

        // Gán stats
        maxHealth = data.maxHp;
        currentHealth = data.maxHp;
        attackDamage = data.atkDmg;
        attackSpeed = data.atkSpeed;
        attackRange = data.atkRange;
        walkSpeed = data.walkSpeed;
        buildTime = data.buildTime;
        cost = data.cost;
        attackFrame = (int)(200/attackSpeed);

        idleAni = idleCache.get(data.id);
        runningAni = runCache.get(data.id);
        attackAni = atkCache.get(data.id);

        attackAniSpeed = (int)Math.min(aniSpeed,attackFrame/attackAni.length);

        BufferedImage firstFrame = idleAni[0];
        width = idleAni[0].getWidth();
        height = idleAni[0].getHeight();
        Rectangle bounds = getTightBounds(firstFrame);
        xOffset = bounds.x;
        yOffset = bounds.y;
        if(isAlly) {
            this.x = trueX - xOffset;
            this.y = trueY - yOffset - bounds.height;
            hitbox = new Rectangle2D.Float(trueX, this.y + bounds.y, bounds.width, bounds.height);
            attackBox = new Rectangle2D.Float(hitbox.x , hitbox.y, attackRange * 32 + hitbox.width, hitbox.height);
        }else{
            this.x = trueX + xOffset;
            this.y = trueY - yOffset -bounds.height;
            hitbox = new Rectangle2D.Float(trueX - bounds.width, trueY - bounds.height, bounds.width, bounds.height);
            attackBox = new Rectangle2D.Float( hitbox.x -attackRange*32 ,  hitbox.y, attackRange*32+hitbox.width , hitbox.height);

        }
    }

    public void update() {
        if(dying){
            alpha -= 0.02f;     // giảm độ trong suốt
            floatY -= 1.0f;     // bay lên (giảm y vì toạ độ y hướng xuống)
            if (alpha <= 0) {
                completed = true; // xoá khi hiệu ứng xong
            }
            return;
        }
        updateAnimationTick();
        setAnimation();

        if (moving) {
            float dx = (isAlly ? 1 : -1) * walkSpeed/2;
            x += dx;
            hitbox.x += dx;
            attackBox.x += dx;
        }
        if (tookDamage) {
            damageEffectTick--;
            if (damageEffectTick <= 0) {
                tookDamage = false;
            }
        }

    }

    private void updateAnimationTick() {
        attackFrameCounter--;
        if(action!= ATTACK){
            aniTick++;
            if (aniTick >= aniSpeed) {
                aniTick = 0;
                aniIndex++;
                if (aniIndex >= getCurrentAni().length) {
                    aniIndex = 0;

                }
            }
        }else{
            aniTick++;
            if (aniTick >= attackAniSpeed) {
                aniTick = 0;
                aniIndex++;
                if (aniIndex >= attackAni.length) {
                    aniIndex = 0;
                    attacking = false;

                }
            }

        }
    }

    private void setAnimation() {
        int startAni = action;
        if (attacking) {
            action = ATTACK;
        }
        else if (moving) {
            action = RUNNING;

        }
        else
            action = IDLE;
        if(startAni != action)
            resetAniTick();

    }
    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }



    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (dying) {
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(alpha, 0));
            g2d.setComposite(ac);
        }

        int drawX = (int) x;
        int drawY = (int) (y + floatY); // áp dụng hiệu ứng bay lên
        BufferedImage img = getCurrentAni()[aniIndex];

        if (isAlly) {
            g2d.drawImage(img, drawX, drawY, width, height, null);
        } else {
            g2d.drawImage(img, drawX, drawY, -width, height, null);
        }
        if (dying) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
        if (!dying) {

//            g.setColor(Color.RED);
//            drawHitbox(g);
//            drawAttackBox(g);
            drawHealthBar(g);
            if (tookDamage) {
                BufferedImage damageOverlay = createDamageOverlay(getCurrentAni()[aniIndex]);
                if (isAlly) {
                    g.drawImage(damageOverlay, (int) x, (int) y, width, height, null);
                } else {
                    g2d.drawImage(damageOverlay, (int) x, (int) y, -width, height, null);
                }
            }
        }
    }

    private void drawHealthBar(Graphics g) {
        int barWidth = (int) hitbox.width;
        int barHeight = 5;
        int x = (int) hitbox.x;
        int y = (int) hitbox.y - barHeight - 2; // Vẽ ngay trên hitbox

        float healthPercent = (float) currentHealth / maxHealth;
        int healthWidth = (int) (barWidth * healthPercent);

        // Viền
        g.setColor(Color.BLACK);
        g.drawRect(x, y, barWidth, barHeight);

        // Máu còn lại
        g.setColor(Color.GREEN);
        g.fillRect(x + 1, y + 1, healthWidth - 2, barHeight - 2);
    }
    private BufferedImage createDamageOverlay(BufferedImage frame) {
        BufferedImage redOverlay = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < frame.getHeight(); y++) {
            for (int x = 0; x < frame.getWidth(); x++) {
                int argb = frame.getRGB(x, y);
                int alpha = (argb >> 24) & 0xff;
                if (alpha > 0) {
                    redOverlay.setRGB(x, y, (100 << 24) | (255 << 16)); // alpha 100, đỏ (255, 0, 0)
                }
            }
        }
        return redOverlay;
    }



    private BufferedImage[] getCurrentAni() {
        return switch (action) {
            case RUNNING -> runningAni;
            case ATTACK -> attackAni;
            default -> idleAni;
        };
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setAttacking(boolean attacking) {
        if(action != ATTACK) {
            this.attacking = attacking;
            attackFrameCounter = attackFrame;
        }
    }
    public int getAction(){
        return action;
    }

    public boolean isAlly() {
        return isAlly;
    }



    private Rectangle getTightBounds(BufferedImage image) {
        int minX = image.getWidth(), minY = image.getHeight(), maxX = 0, maxY = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int alpha = (image.getRGB(x, y) >> 24) & 0xff;
                if (alpha > 0) {
                    minX = Math.min(minX, x);
                    maxX = Math.max(maxX, x);
                    minY = Math.min(minY, y);
                    maxY = Math.max(maxY, y);
                }
            }
        }
        return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }
    public void takeDamage(int dmg) {
        currentHealth -= dmg;
        if (currentHealth <= 0){
            currentHealth = 0;
            die();
        }
        tookDamage = true;
        damageEffectTick = damageEffectDuration;
    }

    private void die() {
        dying = true;
        moving =false;
        attacking = false;
        resetAniTick();
    }

    public boolean isAttacking() {
        return attacking;
    }

    public boolean isDead() {
        return currentHealth <=0 && completed;
    }
    public int attackFrameLength(){
        return attackAni.length;
    }
    public int remainingFrame(){
        int totalAnimationTime = attackFrameLength() * attackAniSpeed;
        return Math.max(0, attackFrame - totalAnimationTime);
    }
    public boolean canDamageNow(){
        return attackFrameCounter == remainingFrame();
    }

    public boolean getFaction() {
        return isAlly;
    }
    public int getIndex(){
        return index;
    }
}
