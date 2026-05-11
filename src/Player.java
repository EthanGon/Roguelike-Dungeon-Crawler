import java.awt.*;
import java.util.Map;


public class Player extends Rect {

    private static Player instance;
    public int size = 64;
    public boolean moving;

    public Player(int x, int y) {
        super(x,y, 64,64);
        this.project = true;
        instance = this;
    }

    public static Player GetPlayer() {
        return instance;
    }

    public void moveUP(int dy)
    {
        y -= dy;
        direction = UP;
        moving = true;
    }

    public void moveUPLT(int dy)
    {
        y -= dy;
        x -= dy;
        direction = UL;
        moving = true;
    }

    public void moveUPRT(int dy)
    {
        y -= dy;
        x += dy;
        direction = UR;
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

    public void checkCollision() {
        MapGen.GetInstance().getRoomContainingPlayer().checkColl();
        handleRoomSwitch();

    }

    public void handleRoomSwitch() {
        Room currRoom = MapGen.GetInstance().getRoomContainingPlayer();


        int offsetX = MapGen.GetInstance().rw;
        int offsetY = MapGen.GetInstance().rh;


        if (y <= currRoom.y) { // TOP
            switchRoom(currRoom, 0, -offsetY, 2);
        }

        if (y >= currRoom.y + offsetY) { // BOT
            switchRoom(currRoom, 0, offsetY, 0);
        }

        if (x <= currRoom.x) { // LEFT
            switchRoom(currRoom, -offsetX, 0, 3);
        }

        if (x >= currRoom.x + offsetX) { // RIGHT
            switchRoom(currRoom, offsetX, 0, 1);
        }



    }

    public void switchRoom(Room currRoom, int ox, int oy, int dir) {
        Room newRoom;

        newRoom = MapGen.GetInstance().getRoom(currRoom.x + ox, currRoom.y + oy);
        Camera.GetInstance().setPos(newRoom.x, newRoom.y);
        this.x = (newRoom.connectionSpawnPoints[dir].x) + ((96 - (size))/2);
        this.y = (newRoom.connectionSpawnPoints[dir].y) + ((96 - (size))/2);
        MapGen.GetInstance().setRoomContainingPlayer(newRoom);
    }

    public void draw(Graphics g) {
        super.draw(g);
    }

}
