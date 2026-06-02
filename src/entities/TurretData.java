package entities;

public class TurretData {
    public final int id;
    public final int damage;
    public final float attackSpeed; // đòn / giây
    public final float range; // phạm vi tính theo pixel
    public final int cost;
    public TurretData(int id, int damage, float attackSpeed, float range, int cost) {
        this.id = id;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;

        this.cost = cost;
    }
}
