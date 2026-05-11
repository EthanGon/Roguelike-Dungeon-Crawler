import java.awt.*;

public class Sprite extends Rect {


    public Sprite(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    public void draw(Graphics g) {
        g.fillRect(x, y, w, h);
    }
}
