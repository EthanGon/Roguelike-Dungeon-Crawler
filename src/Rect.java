import java.awt.*;

public class Rect {
    int x;
    int y;

    int w;
    int h;

    public Rect(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }


    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect(x, y, w, h);
    }

    public boolean overlaps(Rect other) {
        return (  this.x <= other.x + other.w) &&
                (other.x <=  this.x +  this.w) &&
                ( this.y <= other.y + other.h) &&
                (other.y <=  this.y +  this.h);

    }

    public boolean contains(int mx, int my) {
        return  (mx > x)   &&
                (mx < x+w) &&
                (my > y)   &&
                (my < y+h);
    }





}