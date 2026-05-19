import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class Room {

    private Image roomImage =           new ImageIcon(getClass().getResource("room_art/room.png")).getImage();
    private Image northSouthDoorOff =   new ImageIcon(getClass().getResource("room_art/ns-blockade-off.png")).getImage();
    private Image eastWestDoorOff =     new ImageIcon(getClass().getResource("room_art/ew-blockade-off.png")).getImage();
    private Image northSouthDoorOn =    new ImageIcon(getClass().getResource("room_art/ns-blockade-on.png")).getImage();
    private Image eastWestDoorOn =      new ImageIcon(getClass().getResource("room_art/ew-blockade-on.png")).getImage();

    private static Room bossRoom;
    private static int numRooms = 0;
    private final int x;
    private final int y;
    private int pixelSize = 96;
    private int roomNumber;
    private int rw = 96 * 14;
    private int rh = 96 * 10;
    private boolean hasBoss;

    private boolean hasEnemies;
    private boolean roomCleared;

    private Room[] adjRooms = new Room[4];
    private Rect[] doorBounds = new Rect[4];
    private Rect[] roomBounds = new Rect[8];
    private Rect[] walkableSpace = new Rect[12 * 8];
    private Rect[] entryPoints = new Rect[4];
    private Rect roomBox;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private int enemiesDead = 0;

    public Room(int x, int y) {
        this.x = x;
        this.y = y;
        initRoomBounds();
        createRoomSwitchSpawns();
        createWalkableGrid();
        numRooms++;
        roomNumber = numRooms;
    }

    public Room(int x, int y, int connection, Room cameFrom) {
        this(x,y);
        adjRooms[connection] = cameFrom;

    }

    public void createRoomSwitchSpawns() {

        // Counter Clockwise (N,W,S,E)
        entryPoints[0] = new Rect(x + (96 * 6) + 48, y + 96, 96, 96);
        entryPoints[1] = new Rect(x + 96, y + (96 * 4) + 48, 96, 96);
        entryPoints[2] = new Rect(x + (96 * 6) + 48, y + (96 * 8), 96, 96);
        entryPoints[3] = new Rect(x + (96 * 12), y + (96 * 4) + 48, 96, 96);

        for (Rect b : entryPoints) {
            b.project = true;
        }
    }

    public void createWalkableGrid() {
        int xo = 96;
        int yo = 96;
        int size = walkableSpace.length;
        int index = 0;
        int countSwitch = 0;

        int startX = x + 96;
        int startY = y + 96;

        for (int i = 0; i < size; i++) {
            walkableSpace[index] = new Rect(startX, startY, 96,96);
            walkableSpace[index].project = true;
            startX += xo;
            index++;
            countSwitch++;

            if (countSwitch >= 12) {
                startX = x + 96;
                startY += yo;
                countSwitch = 0;
            }

        }
    }

    public void draw(Graphics g) {
        drawRoom(g);
        drawRoomBounds(g);
        drawEntryPoints(g);
        drawWalkableSpace(g);
        drawEnemies(g);

    }

    public void drawEnemies(Graphics g) {
        for (Enemy e : enemies) {
            if (e.isAlive()) {
                e.draw(g);
            }
        }
    }

    private void drawRoom(Graphics g) {
        int cx = Camera.GetInstance().getX();
        int cy = Camera.GetInstance().getY();

        g.drawImage(roomImage, x - cx , y - cy, null);

        if (adjRooms[0] != null) {g.drawImage(northSouthDoorOn, (x + 576) - cx, y - cy, null);} // 0 TOP
        if (adjRooms[2] != null) {g.drawImage(northSouthDoorOn, (x + 576) - cx, (y + 864) - cy, null);} // 2 BOT
        if (adjRooms[1] != null) {g.drawImage(eastWestDoorOn, x - cx, (y + 384) - cy, null);} // 3 EAST
        if (adjRooms[3] != null) {g.drawImage(eastWestDoorOn, (x + 1248) - cx, (y + 384) - cy, null);} // 1 WEST

        if (adjRooms[0] != null && !doorBounds[0].hasCollision) {g.drawImage(northSouthDoorOff, (x + 576) - cx, y - cy, null);} // 0 TOP
        if (adjRooms[2] != null && !doorBounds[2].hasCollision) {g.drawImage(northSouthDoorOff, (x + 576) - cx, (y + 864) - cy, null);} // 2 BOT
        if (adjRooms[1] != null && !doorBounds[1].hasCollision) {g.drawImage(eastWestDoorOff, x - cx, (y + 384) - cy, null);} // 3 EAST
        if (adjRooms[3] != null && !doorBounds[3].hasCollision) {g.drawImage(eastWestDoorOff, (x + 1248) - cx, (y + 384) - cy, null);} // 1 WEST
    }


    private void drawWalkableSpace(Graphics g) {
        g.setColor(Color.lightGray);

        int i = 0;
        for (Rect r : walkableSpace) {
            g.drawString("Room: #" + roomNumber, r.x - Camera.GetInstance().getX(), r.y - Camera.GetInstance().getY() + 12);
            g.drawString("Tile: #" + i, r.x - Camera.GetInstance().getX(), r.y - Camera.GetInstance().getY() + 24);
            r.draw(g);
            i++;
        }
    }

    private void drawEntryPoints(Graphics g) {
        g.setColor(Color.green);
        for (Rect spawn : entryPoints) {
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
        roomWallCollisions();
        checkEnemyCollisions();

   }

   public void roomWallCollisions() {
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

   public void checkEnemyCollisions() {
       if (enemiesDead == enemies.size() || (roomNumber == 1)) {return;};

       for (Enemy e : enemies) {
           if (e.isAlive() && e.overlaps(Player.GetPlayer().getSmallerBox())) {
               Player.GetPlayer().takeDamage();
           }
       }

       for (Enemy e : enemies) {
           if (e.isAlive() && e.overlaps(Player.GetPlayer().getWeapon()) && Player.GetPlayer().getWeapon().isActive() && !hasBoss) {
               e.setDead();
               enemiesDead++;

               if (enemiesDead == enemies.size()) {
                   setRoomCleared();
                   LevelLogic.increaseClearedRooms();
               }
           }

       }

       if (hasBoss) {
           Enemy boss = enemies.getFirst();
           if (boss.isAlive() && boss.overlaps(Player.GetPlayer().getWeapon())) {
               boss.enemyHP--;
               if (boss.enemyHP == 0) {
                   boss.setDead();
               }
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
            if (adjRooms[i] != null && !adjRooms[i].hasBoss()) {
                doorBounds[i].hasCollision = false;
            }

            if (adjRooms[i] != null && adjRooms[i].hasBoss()) {
                doorBounds[i].hasCollision = true;
            }
        }

        for (Enemy e : enemies) {
            e.setDead();
        }


    }

    public boolean isRoomCleared() {
        return roomCleared;
    }

    public void giveEnemies(boolean value) {
        if (value) {
            hasEnemies = true;
            EnemySpawner.spawnEnemies(this);
        }
        else {hasEnemies = false;};
    }

    public void moveEnemies() {
        for (Enemy e : enemies) {
            if (e.isAlive() && !e.overlaps(Player.GetPlayer().getSmallerBox())) {
                e.chase(Player.GetPlayer());
            }
        }
    }

    public boolean hasEnemies() {
        return hasEnemies;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rect getTile(int index) {
        return walkableSpace[index];
    }

    public int getNumTiles() {
        return walkableSpace.length;
    }

    public Rect getEntryPoint(int index) {
        return entryPoints[index];
    }

    public boolean hasAdjacentRoom(int dir) {
        return adjRooms[dir] != null;
    }

    public void setAdjacentRoom(int dir, Room rm) {
        adjRooms[dir] = rm;
    }

    public void addEnemy(Enemy e) {
        enemies.add(e);
    }

    public boolean hasBoss() {
        return hasBoss;
    }

    public void giveBoss() {
        hasBoss = true;
        bossRoom = this;

        enemies.clear();
        enemies.add(new Bat(x + (rw/2), y + (rh/2)));
        Enemy boss = enemies.getFirst();

        boss.w *= 2;
        boss.h *= 2;
        Enemy.boss = boss;
        boss.enemyMaxHP = 350;
        boss.enemyHP = boss.enemyMaxHP;

    }

    public int numConnections() {
        int i = 0;

        for (Room r : adjRooms) {
            if (r != null) {
                i++;
            }
        }

        return i;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public static Rect findBossDoor() {
        Room adjRoomToBoss = null;

        for (int i = 0; i < 4; i++) {
            if (bossRoom.adjRooms[i] != null) {
                adjRoomToBoss = bossRoom.adjRooms[i];
                break;
            }
        }

        for (int i = 0; i < 4; i++) {
            if (adjRoomToBoss.adjRooms[i] != null && adjRoomToBoss.adjRooms[i].hasBoss) {
                System.out.println(adjRoomToBoss.adjRooms[i].doorBounds[i]);
                return adjRoomToBoss.doorBounds[i];
            }
        }

        return null;
    }

    public static void resetNumRooms() {
        numRooms = 0;
    }

    public static Room getBossRoom() {
        return bossRoom;
    }






}
