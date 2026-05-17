public class EnemySpawner {

    public static void spawnEnemies(Room rm) {
        int[] possibleEnemyCount = {2, 4};
        int amount = possibleEnemyCount[GameSeed.get().nextInt(0, 2)];
        int pattern = GameSeed.get().nextInt(2);

        if (amount == 2) {
            if (pattern == 0) {
                rm.addEnemy(new Bat(rm.getTile(13).x, rm.getTile(13).y));
                rm.addEnemy(new Bat(rm.getTile(82).x, rm.getTile(82).y));
            } else {
                rm.addEnemy(new Bat(rm.getTile(22).x, rm.getTile(22).y));
                rm.addEnemy(new Bat(rm.getTile(73).x, rm.getTile(73).y));
            }
        } else {
            rm.addEnemy(new Bat(rm.getTile(0).x, rm.getTile(0).y));
            rm.addEnemy(new Bat(rm.getTile(11).x, rm.getTile(11).y));
            rm.addEnemy(new Bat(rm.getTile(84).x, rm.getTile(84).y));
            rm.addEnemy(new Bat(rm.getTile(95).x, rm.getTile(95).y));
        }

    }





}
