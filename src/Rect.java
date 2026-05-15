import java.awt.*;

public class Rect {
    int x;
    int y;

    int w;
    int h;


    int vx;
    int vy;

    int direction = DN;

    static final int UP = 0;
    static final int DN = 1;
    static final int LT = 2;
    static final int RT = 3;

    static final int UL = 4;
    static final int DL = 5;
    static final int UR = 6;
    static final int DR = 7;

    // used for map dir
    boolean[] dir = new boolean[4];

    // used for dir object is facing
    boolean project;
    boolean hasCollision = true;

    public Rect(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Rect(int x, int y, int w, int h, int val) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        dir[val] = true;
    }

    public void pushLeft(Rect r)
    {
        r.x -= (r.x + r.w - x + 1);
    }

    public void pushRight(Rect r)
    {
        r.x += (x + w - r.x + 1);
    }

    public void pushUp(Rect r)
    {
        r.y -= (r.y + r.h - y + 1);
    }

    public void pushDown(Rect r)
    {
        r.y += (y + h - r.y + 1);
    }

    public void pushes(Rect r)
    {
        if (hasCollision) {
            if(r.direction == UP)  pushDown(r);
            if(r.direction == DN)  pushUp(r);
            if(r.direction == LT)  pushRight(r);
            if(r.direction == RT)  pushLeft(r);
        }
    }

    public void draw(Graphics g) {

        if (project) {
            int cx = Camera.GetInstance().getX();
            int cy = Camera.GetInstance().getY();

            g.drawRect(x - cx, y - cy, w, h);
        } else {
            g.drawRect(x, y, w, h);
        }
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