package entities;

public class UnitData {
    public int id;
    public int numSprites ,idleStart, idleEnd, runStart, runEnd, atkStart, atkEnd;
    public int maxHp, atkDmg, cost;
    public float buildTime,atkSpeed, atkRange, walkSpeed;
    public String name;

    public UnitData(int id ,int numSprites,int idleStart, int idleEnd, int runStart, int runEnd, int atkStart, int atkEnd,
                    int maxHp, int atkDmg, float atkSpeed, float atkRange, float walkSpeed, float buildTime, int cost, String name) {

        this.id = id;
        this.numSprites = numSprites;
        this.idleStart = idleStart;
        this.idleEnd = idleEnd;
        this.runStart = runStart;
        this.runEnd = runEnd;
        this.atkStart = atkStart;
        this.atkEnd = atkEnd;
        this.maxHp = maxHp;
        this.atkDmg = atkDmg;
        this.atkSpeed = atkSpeed;
        this.atkRange = atkRange;
        this.walkSpeed = walkSpeed;
        this.buildTime = buildTime;
        this.cost = cost;
        this.name = name;
    }
}
