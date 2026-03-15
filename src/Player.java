import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    public boolean[] pressingKey = new boolean[1024];
    public static final int UP 			= KeyEvent.VK_W;
    public static final int DN 			= KeyEvent.VK_S;
    public static final int LT 			= KeyEvent.VK_A;
    public static final int RT 			= KeyEvent.VK_D;


    private float x;
    private float y;
    private int speed = 5;
    public int size = 64;
    private Rect bounds;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        bounds = new Rect(x, y, size, size);
    }

    public void move() {
        if (pressingKey[UP]) {this.y -= speed;}
        if (pressingKey[DN]) {this.y += speed;}
        if (pressingKey[LT]) {this.x -= speed;}
        if (pressingKey[RT]) {this.x += speed;}

    }

    public void draw(Graphics g) {
        g.fillRect((int) x, (int) y, size, size);

        g.setColor(Color.green);
        g.drawRect((int) x, (int) y, size, size);
    }

}
