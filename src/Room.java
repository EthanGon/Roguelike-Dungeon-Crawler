import javax.swing.*;
import java.awt.*;



public class Room {

    private Image roomImage =           new ImageIcon(getClass().getResource("room_art/room.png")).getImage();
    private Image northSouthDoorOff =   new ImageIcon(getClass().getResource("room_art/ns-blockade-off.png")).getImage();
    private Image eastWestDoorOff =     new ImageIcon(getClass().getResource("room_art/ew-blockade-off.png")).getImage();
    private Image northSouthDoorOn =    new ImageIcon(getClass().getResource("room_art/ns-blockade-on.png")).getImage();
    private Image eastWestDoorOn =      new ImageIcon(getClass().getResource("room_art/ew-blockade-on.png")).getImage();

    private int pixelSize = 96;
    private boolean roomCleared;
    private int rw = 96 * 14;
    private int rh = 96 * 10;

    private Rect[] roomBounds = new Rect[8];
    private Rect roomBox;
    public boolean hasEnemies;

    public Room[] adjRooms = new Room[4];
    private Rect[] doorBounds = new Rect[4];
    private Rect[] enemySpawnPoints;


    int x;
    int y;

    public Rect[] connectionSpawnPoints = new Rect[4];

    public Room(int x, int y) {
        this.x = x;
        this.y = y;
        initRoomBounds();
        createRoomSwitchSpawns();

    }

    public Room(int x, int y, int connection, Room cameFrom) {
        adjRooms[connection] = cameFrom;
        this.x = x;
        this.y = y;
        initRoomBounds();
        createRoomSwitchSpawns();

    }

    public void createRoomSwitchSpawns() {

        // Counter Clockwise (N,W,S,E)
        connectionSpawnPoints[0] = new Rect(x + (96 * 6) + 48, y + 96, 96, 96);
        connectionSpawnPoints[1] = new Rect(x + 96, y + (96 * 4) + 48, 96, 96);
        connectionSpawnPoints[2] = new Rect(x + (96 * 6) + 48, y + (96 * 8), 96, 96);
        connectionSpawnPoints[3] = new Rect(x + (96 * 12), y + (96 * 4) + 48, 96, 96);

        for (Rect b : connectionSpawnPoints) {
            b.project = true;
        }
    }

    public void draw(Graphics g) {

        int cx = Camera.GetInstance().getX();
        int cy = Camera.GetInstance().getY();

        g.drawImage(roomImage, x - cx , y - cy, null);

        // draw the connection to adjacent rooms
        if (!roomCleared) {
            if (adjRooms[0] != null) {g.drawImage(northSouthDoorOn, (x + 576) - cx, y - cy, null);} // 0 TOP
            if (adjRooms[2] != null) {g.drawImage(northSouthDoorOn, (x + 576) - cx, (y + 864) - cy, null);} // 2 BOT
            if (adjRooms[1] != null) {g.drawImage(eastWestDoorOn, x - cx, (y + 384) - cy, null);} // 3 EAST
            if (adjRooms[3] != null) {g.drawImage(eastWestDoorOn, (x + 1248) - cx, (y + 384) - cy, null);} // 1 WEST
        } else {
            if (adjRooms[0] != null) {g.drawImage(northSouthDoorOff, (x + 576) - cx, y - cy, null);} // 0 TOP
            if (adjRooms[2] != null) {g.drawImage(northSouthDoorOff, (x + 576) - cx, (y + 864) - cy, null);} // 2 BOT
            if (adjRooms[1] != null) {g.drawImage(eastWestDoorOff, x - cx, (y + 384) - cy, null);} // 3 EAST
            if (adjRooms[3] != null) {g.drawImage(eastWestDoorOff, (x + 1248) - cx, (y + 384) - cy, null);} // 1 WEST
        }

        drawRoomBounds(g);

        g.setColor(Color.green);
        for (Rect spawn : connectionSpawnPoints) {
            spawn.draw(g);
        }

    }

    private void initRoomBounds() {
        // Top Bounds
        roomBounds[0] = new Rect( x, y, pixelSize, pixelSize * 4);
        roomBounds[1] = new Rect(x + pixelSize, y, pixelSize * 5, pixelSize);
        roomBounds[2] = new Rect(x + (pixelSize * 13), y, pixelSize, pixelSize * 4);
        roomBounds[3] = new Rect(x + (pixelSize * 8), y, pixelSize * 5, pixelSize);

        // Bottom Bounds
        roomBounds[4] = new Rect(x, y + (pixelSize * 6), pixelSize, pixelSize * 4);
        roomBounds[5] = new Rect(x + pixelSize, y + pixelSize * 9, pixelSize * 5, pixelSize);
        roomBounds[6] = new Rect(x + (pixelSize * 13), y + (pixelSize * 6), pixelSize, pixelSize * 4);
        roomBounds[7] = new Rect(x + (pixelSize * 8), y + (pixelSize * 9), 480, pixelSize);

        for (Rect b : roomBounds) {
            b.project = true;
        }

        doorBounds[0] = new Rect(x + 576, y, 96 * 2, 96); // TOP
        doorBounds[2] = new Rect(x + 576, y + 864, 96 * 2, 96); // BOT
        doorBounds[1] = new Rect(x, y + 384, 96, 96 * 2); // LEFT
        doorBounds[3] = new Rect(x + 1248, y + 384, 96, 96 * 2); // RIGHT

        for (Rect b : doorBounds) {
            b.project = true;
        }

        roomBox = new Rect(x, y, rw, rh);
    }

    private void drawRoomBounds(Graphics g) {
        g.setColor(Color.red);
        for (int i = 0; i < roomBounds.length; i++) {
            roomBounds[i].draw(g);
        }

        g.setColor(Color.blue);
        for (int i = 0; i < doorBounds.length; i++) {
            if (doorBounds[i] != null) {
                doorBounds[i].draw(g);
            }
        }
    }

   public void checkColl() {
       for (int i = 0; i < roomBounds.length; i++) {
           if (roomBounds[i].overlaps(Player.GetPlayer())) {
               roomBounds[i].pushes(Player.GetPlayer());
           }
       }

       for (int i = 0; i < doorBounds.length; i++) {
           if (doorBounds[i] != null && doorBounds[i].overlaps(Player.GetPlayer())) {
               doorBounds[i].pushes(Player.GetPlayer());
           }
       }
   }

   public Rect getDoor(int index) {
        return doorBounds[index];
   }

   public void setRoomUncleared() {
        roomCleared = false;
        for (int i = 0; i < adjRooms.length; i++) {
            if (adjRooms[i] != null) {
                doorBounds[i].hasCollision = true;
            }
        }
   }

    public void setRoomCleared() {
        roomCleared = true;
        hasEnemies = false;

        for (int i = 0; i < adjRooms.length; i++) {
            if (adjRooms[i] != null) {
                doorBounds[i].hasCollision = false;
            }
        }
    }



}
