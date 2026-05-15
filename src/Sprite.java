import java.awt.*;

public class Sprite extends Rect {

    String name;
    boolean moving = false;
    boolean physics = false;
    Animation[] animation = new Animation[4];

    public Sprite(String name, int x, int y, int w, int h, int direction, String[] pose)
    {
        super(x, y, w, h);


        this.name = name;

        for(int i = 0; i < animation.length; i++)
        {
            animation[i] = new Animation(name + "_" + pose[i], 3, 10, "gif");
        }


        this.direction = direction;
    }

    public void move()
    {
        x += vx;
        y += vy;

        if (physics == false)
        {
            vx = 0;
            vy = 0;
        }
    }


    public void goUP(int dy)
    {
        vy = -dy;
        direction = UP;
        moving = true;
    }

    public void goDN(int dy)
    {
        vy = dy;
        direction = DN;
        moving = true;
    }

    public void goLT(int dx)
    {
        vx = -dx;
        direction = LT;
        moving = true;
    }

    public void goRT(int dx)
    {
        vx = dx;

        direction = RT;

        moving = true;

    }

    public void moveUP(int dy)
    {
        y -= dy;

        direction = UP;

        moving = true;
    }

    public void moveDN(int dy)
    {
        y += dy;

        direction = DN;

        moving = true;
    }

    public void moveLT(int dx)
    {
        x -= dx;

        direction = LT;

        moving = true;
    }

    public void moveRT(int dx)
    {
        x += dx;

        direction = RT;

        moving = true;

    }

    @Override
    public void draw(Graphics g)
    {
        int cx = Camera.GetInstance().getX();;
        int cy = Camera.GetInstance().getY();

       // g.fillRect(x - cx, y - cy, w, h);

        if(moving)
        {
            g.drawImage(animation[direction].nextImage(), x - cx, y - cy, w, h, null);
        }
        else
        {
            g.drawImage(animation[direction].stillImage(), x - cx, y - cy, w, h, null);
        }


        moving = false;
    }

}
