import java.awt.*;

public class Player extends Sprite {

    private static Player instance;
    public int size = 96;
    public boolean moving;
    private final static String[] pose = {"up", "dn", "lt", "rt"};
    int offsetX = MapGen.GetInstance().rw;
    int offsetY = MapGen.GetInstance().rh;
    private Rect smallerBox;
    private int smallBoxOffset = 85;
    private int health = 12;
    private int maxHeath = 12;
    private int iframeTime = 2;
    private int iframeTimer = 0;
    private boolean canTakeDamage = true;

    public Player(int x, int y, int dir, int size) {
        super("src/player_animation/p", x,y, size,size, dir, pose);
        this.project = true;
        instance = this;
        health = maxHeath;

        smallerBox = new Rect(x,y, size - smallBoxOffset, size - smallBoxOffset);
        smallerBox.project = true;
    }

    public void update() {
        checkCollision();
        handleSmallBox();

        if (!canTakeDamage) {
            handleInvulFrame();
        }
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

    public void handleSmallBox() {
        smallerBox.x = x + (smallBoxOffset/2);
        smallerBox.y = y + (smallBoxOffset/2);
    }

    public void handleRoomSwitch() {
        Room currRoom = MapGen.GetInstance().getRoomContainingPlayer();
        currRoom.moveEnemies();

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

    public void drawSmallBox(Graphics g) {
        smallerBox.draw(g);
    }

    public Rect getSmallerBox() {
        return smallerBox;
    }

    public int getCurrentHP() {
        return health;
    }

    public int getMaxHeath() {
        return maxHeath;
    }

    public void increaseCurrentHP() {
        if (getCurrentHP() < maxHeath) {
            health++;
        }
    }

    public void decreaseCurrentHP() {
        if (getCurrentHP() > 0) {
            health--;
        }
    }

    public void takeDamage() {
        if (canTakeDamage) {
            startInvulFrame();
            decreaseCurrentHP();
        }
    }

    public void startInvulFrame() {
        canTakeDamage = false;
    }

    public void handleInvulFrame() {
        if (iframeTimer >= (57 * iframeTime)) {
            canTakeDamage = true;
            iframeTimer = 0;
        }
        iframeTimer++;
    }

    public boolean canTakeDamage() {
        return canTakeDamage;
    }

}
