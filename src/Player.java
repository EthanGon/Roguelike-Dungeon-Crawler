import java.awt.*;

public class Player {
    private float x;
    private float y;
    private boolean UP, DOWN, LEFT, RIGHT;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        if (UP) {this.y--;}
        if (DOWN) {this.y++;}
        if (LEFT) {this.x--;}
        if (RIGHT) {this.x++;}
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect((int) x, (int) y, 64, 64);
    }

}
