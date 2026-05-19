public class Enemy extends Sprite {

    public static Enemy boss;
    private final static String[] pose = {"up", "dn", "lt", "rt"};
    private int moveSpeed = 2;
    private boolean isDead = false;
    public int enemyHP;
    public int enemyMaxHP;

    public Enemy(String name, int x, int y) {
        super(name, x, y, 96, 96, Sprite.DN, pose);
    }

    public void chase(Rect r) {
        if(isDead) return;
        if(x > r.x){
            moveLT(moveSpeed);
        }

        if (x < r.x) {
            moveRT(moveSpeed);
        }

        if (y > r.y) {
            moveUP(moveSpeed);
        }

        if (y < r.y) {
            moveDN(moveSpeed);
        }
    }

    public void setDead() {
        isDead = true;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isAlive() {
        return !isDead;
    }



}
