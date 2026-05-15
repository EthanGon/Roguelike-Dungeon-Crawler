public class Enemy extends Sprite {

    private final static String[] pose = {"up", "dn", "lt", "rt"};

    public Enemy(String name, int x, int y, int w, int h, int direction, String[] pose) {
        super(name, x, y, w, h, direction, pose);
    }



}
