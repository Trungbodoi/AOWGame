package object;
import entities.Base;
import entities.Unit;
import gamestates.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class ProjectileManager {
    private List<Projectile> projectiles = new ArrayList<>();
    private Map<Integer, BufferedImage> projectileImages = new HashMap<>();
    private Playing playing;
    public void setPlaying(Playing playing) {
        this.playing = playing;
    }
    public ProjectileManager() {
        preloadImages();
    }

    private void preloadImages() {
        int[] ids = {12, 21,23,24,32,42,43,44,52,53}; // danh sách id của các loại đạn
        for (int id : ids) {
            BufferedImage img = LoadSave.GetSpriteAtlas("units/projectile_" + id + ".png");
            projectileImages.put(id, img);
        }
    }

    public void spawnProjectile(Unit u, int id, int damage) {
        Rectangle2D.Float hb = u.getHitbox();
        boolean facingRight = u.isAlly();
        BufferedImage img = projectileImages.get(id);
        if (img == null) return;
        int projW = img.getWidth();
        int projH = img.getHeight();
        float x = facingRight ? hb.x + hb.width : hb.x - projW;
        float y = hb.y + hb.height / 2 - projH/2;
        Projectile p = new Projectile(x, y, damage, u.isAlly(), facingRight, id, img);
        projectiles.add(p);
    }

    public void update( List<Unit> units ) {
        Iterator<Projectile> it = projectiles.iterator();
        while (it.hasNext()) {
            Projectile p = it.next();
            p.update();
            boolean hitSomething = false;


            for (Unit u : units) {
                if (u.isAlive() && p.checkCollision(u))
                {
                    hitSomething = true;
                    break;
                }
            }
            if (!hitSomething && playing != null) {
                Base enemyBase = p.isAlly() ? playing.getEnemyBase() : playing.getMyBase();
                if (p.getHitbox().intersects(enemyBase.getBounds())) {
                    enemyBase.takeDamage(p.getDamage());
                    p.deactive();
                    hitSomething = true;
                }
            }


            if (!p.isActive()) it.remove();
        }
    }

    public void render(Graphics g) {
        List<Projectile> safeList = new ArrayList<>(projectiles); // bản sao tạm thời an toàn
        for (Projectile p : safeList) {
            p.render(g);
        }
    }


    public void reset() {
        projectiles.clear();
    }
}
