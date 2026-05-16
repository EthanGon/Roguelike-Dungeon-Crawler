import java.awt.*;

public class Player extends Sprite {

    private static Player instance;
    public int size = 96;
    public boolean moving;
    private final static String[] pose = {"up", "dn", "lt", "rt"};
    int offsetX = MapGen.GetInstance().rw;
    int offsetY = MapGen.GetInstance().rh;

    public Player(int x, int y, int dir, int size) {
        super("src/player_animation/p", x,y, size,size, dir, pose);
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

        if (y <= currRoom.getY()) { // TOP
            switchRoom(currRoom, 0, -offsetY, 2);
        }

        if (y >= currRoom.getY() + offsetY) { // BOT
            switchRoom(currRoom, 0, offsetY, 0);
        }

        if (x <= currRoom.getX()) { // LEFT
            switchRoom(currRoom, -offsetX, 0, 3);
        }

        if (x >= currRoom.getX() + offsetX) { // RIGHT
            switchRoom(currRoom, offsetX, 0, 1);
        }



    }

    public void switchRoom(Room currRoom, int ox, int oy, int dir) {
        Room newRoom;

        newRoom = MapGen.GetInstance().getRoom(currRoom.getX() + ox, currRoom.getY() + oy);
        Camera.GetInstance().setPos(newRoom.getX(), newRoom.getY());

        // centers the player on the spawn box
        this.x = (newRoom.getEntryPoint(dir).x) + ((96 - (size))/2);
        this.y = (newRoom.getEntryPoint(dir).y) + ((96 - (size))/2);

        MapGen.GetInstance().setRoomContainingPlayer(newRoom);
    }

}
