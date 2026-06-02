package ui;

import entities.Base;
import entities.UnitManager;
import gamestates.Playing;

import java.util.Random;

public class EnemyAi {
    private final Random rand = new Random();
    private final int FRAME_PER_SECOND = 200;

    private int gameStart;
    private int age;
    private int numTroops;
    private int spawnCoolDown;
    private int timeBetweenSpawn;
    private int numHut = 0;

    private Playing playing;
    private UnitManager unitManager;
    private Base base;

    public EnemyAi(Playing playing) {
        this.playing = playing;
        this.gameStart = 0;
        this.age = 1;
        this.numTroops = 0;
        this.timeBetweenSpawn = FRAME_PER_SECOND * 3;
    }

    public void settingAi() {
        unitManager = playing.getMangager();
        base = playing.getEnemyBase();
    }

    public void resetAi() {
        gameStart = 0;
        age = 1;
        numTroops = 0;
        timeBetweenSpawn = FRAME_PER_SECOND * 3;
        numHut = 0;
    }

    public void updateAI() {
        gameStart++;
        // Hut and turret logic at 30s of age
        if (gameStart == 30 * FRAME_PER_SECOND && age == 1 && numHut < 1) {
            addHutAndTurret();
        }
        if (gameStart == (120 * 2 + 30) * FRAME_PER_SECOND && age == 3 && numHut < 2) {
            addHutAndTurret();
        }
        if (gameStart == (120 * 4 + 30) * FRAME_PER_SECOND && age == 5 && numHut < 3) {
            addHutAndTurret();
        }

        // Age evolution
        if (gameStart > age * 120 * FRAME_PER_SECOND && age < 5) {
            evo();
            playing.setEnemyAge(age);
        }

        // Replace turret logic at 30s of each new age
        int replaceTime = (age - 1) * 120 * FRAME_PER_SECOND + 30 * FRAME_PER_SECOND;
        if (gameStart == replaceTime && numHut > 0) {
            replaceTurrets();
        }

        // Spawn unit logic
        if (spawnCoolDown > 0) {
            spawnCoolDown--;
            return;
        }

        if (numTroops < 4) {
            spawnSmallUnit();
        } else if (numTroops < 7 && (gameStart - (age - 1) * 120 * FRAME_PER_SECOND) > 30 * FRAME_PER_SECOND * age) {
            spawnBigUnit();
        }


        // Extra spawning logic for final age
        if (gameStart > (age + 1) * 120 * FRAME_PER_SECOND && age == 5 && numTroops < 10) {
            spawnBigUnit();
            timeBetweenSpawn = (int)(FRAME_PER_SECOND * 2.5);
            spawnCoolDown = timeBetweenSpawn;
        }
    }

    private void spawnSmallUnit() {
        unitManager.addUnit(base.getBounds().x, base.getBounds().y + base.getBounds().height, getRandomSmallTroops(), false);
        numTroops++;
        spawnCoolDown = timeBetweenSpawn;
    }

    private void spawnBigUnit() {
        unitManager.addUnit(base.getBounds().x, base.getBounds().y + base.getBounds().height, getRandomBigTroops(), false);
        numTroops++;
        spawnCoolDown = timeBetweenSpawn;
    }

    private int getRandomSmallTroops() {
        int[] options = {age * 10 + 1, age * 10 + 2, age * 10 + 3};
        return options[rand.nextInt(options.length)];
    }

    private int getRandomBigTroops() {
        int[] options = {age * 10 + 2, age * 10 + 3, age * 10 + 4};
        return options[rand.nextInt(options.length)];
    }

    public void decreasePop() {
        numTroops--;
    }

    private void evo() {
        age++;
        base.evo();
    }

    private void addHutAndTurret() {
        if (numHut >= 3) return;

        System.out.println("adding hut to enemybase");
        base.addHut();
        System.out.println(base.getHuts().size());
        int hutIndex = numHut;
        int turretId = getRandomTurretIdForAge(age);
        playing.getTurretManager().addEnemyTurret(hutIndex, turretId);
        numHut++;
    }

    private void replaceTurrets() {
        for (int i = 0; i < numHut; i++) {
            playing.getTurretManager().removeEnemyAt(i);

            int turretId = getRandomTurretIdForAge(age);
            playing.getTurretManager().addEnemyTurret(i, turretId);

//            // Nếu đời 3 hoặc 4, thêm turret thứ 2
//            if (age >= 3 && age <= 4) {
//                int secondTurretId = getRandomTurretIdForAge(age);
//                playing.getTurretManager().addEnemyTurret(i, secondTurretId);
//            }
//            // Nếu đời 5, thêm 3 turret
//            else if (age == 5) {
//                for (int j = 0; j < 2; j++) {
//                    int extraTurretId = getRandomTurretIdForAge(age);
//                    playing.getTurretManager().addEnemyTurret(i, extraTurretId);
//                }
//            }
        }
    }

    private int getRandomTurretIdForAge(int age) {
        int baseId = age * 10 + 1;
        return baseId + rand.nextInt(3); // Random từ x1 đến x3
    }
}
