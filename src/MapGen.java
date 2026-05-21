import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class MapGen {
    private ArrayList<Rect> rects = new ArrayList<>();
    private ArrayList<Room> rooms = new ArrayList<>();
    private Queue<Rect> newRoomsOnMap = new LinkedList<>();
    private Queue<Room> newRooms = new LinkedList<>();
    private Room containingPlayer;
    private static MapGen instance;
    private int maxRoom = 5;

    public int rw = 96 * 14;
    public int rh = 96 * 10;
    private final int UP = 0;
    private final int LEFT = 1;
    private final int DOWN = 2;
    private final int RIGHT = 3;
    private boolean showMiniMap = true;
    private boolean canSwitchMap = true;
    private int miniMapScale = 35; // keep between 20-35

    public MapGen() {
        instance = this;
        createRooms();
        snapMiniMapToCorner();
        setBossRoom();
        setConditionForUnlockingBoss();
        System.out.println("Needed Rooms to clear: " + LevelLogic.getRoomsNeededCleared());
    }

    public void createRooms() {
        long startTime = System.nanoTime();

        // creates the starting room
        rects.add(new Rect(200, 200, miniMapScale, miniMapScale));
        rooms.add(new Room(0,0));
        containingPlayer = rooms.getFirst();

        while (rects.size() < maxRoom) {
            for (int i = 0; i < rects.size(); i++) {

                Rect curr = rects.get(i);
                Room currRoom = rooms.get(i);

                int dir = GameSeed.get().nextInt(4);

                if (curr.dir[dir] == true && currRoom.hasAdjacentRoom(dir)) { // already has a connection there
                    continue;
                }

                Room newRoomToAdd;

                // directions go counter-clockwise
                if (dir == UP) { // TOP
                    if (roomAtPosition(rects.get(i).x, rects.get(i).y - miniMapScale)) {continue;}
                    newRoomsOnMap.add(new Rect(curr.x, curr.y - miniMapScale, miniMapScale, miniMapScale, DOWN));

                    newRoomToAdd = new Room(currRoom.getX(), currRoom.getY() - rh, DOWN, currRoom);
                    newRoomToAdd.getDoor(DOWN).hasCollision = true;
                    newRooms.add(newRoomToAdd);

                } else if (dir == LEFT) { // LEFT
                    if (roomAtPosition(rects.get(i).x - miniMapScale, rects.get(i).y)) {continue;}
                    newRoomsOnMap.add(new Rect(curr.x - miniMapScale, curr.y, miniMapScale, miniMapScale, RIGHT));

                    newRoomToAdd = new Room(currRoom.getX() - rw, currRoom.getY(), RIGHT, currRoom);
                    newRoomToAdd.getDoor(RIGHT).hasCollision = true;
                    newRooms.add(newRoomToAdd);
                } else if (dir == DOWN) { // BOTTOM
                    if (roomAtPosition(rects.get(i).x, rects.get(i).y + miniMapScale)) {continue;}
                    newRoomsOnMap.add(new Rect(curr.x, curr.y + miniMapScale, miniMapScale, miniMapScale, UP));

                    newRoomToAdd = new Room(currRoom.getX(), currRoom.getY() + rh, UP, currRoom);
                    newRoomToAdd.getDoor(UP).hasCollision = true;
                    newRooms.add(newRoomToAdd);
                } else { // RIGHT
                    if (roomAtPosition(rects.get(i).x + miniMapScale, rects.get(i).y)) {continue;}
                    newRoomsOnMap.add(new Rect(curr.x + miniMapScale, curr.y, miniMapScale, miniMapScale, LEFT));

                    newRoomToAdd = new Room(currRoom.getX() + rw, currRoom.getY(), LEFT, currRoom);
                    newRoomToAdd.getDoor(LEFT).hasCollision = true;
                    newRooms.add(newRoomToAdd);
                }

                curr.dir[dir] = true;
                currRoom.getDoor(dir).hasCollision = true;
                currRoom.setAdjacentRoom(dir, newRoomToAdd);

                // Check if processing all room results in max rooms
                if (rects.size() + newRoomsOnMap.size() >= maxRoom) {
                    break;
                }

            }
            processNewRooms();

        }

        rollRoomsEnemies();

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        System.out.println("Map Creation Elapsed Time: " + (elapsedTime / 1e+9 + " seconds"));
    }

    public void rollRoomsEnemies() {
        // Decide if room should have enemies
        for (int i = 1; i < rooms.size(); i++) {
            int spawnEnemies = chanceInt(85);

            if (spawnEnemies == 1) {
                rooms.get(i).giveEnemies(true);
                rooms.get(i).setRoomUncleared();
            } else {
                rooms.get(i).giveEnemies(false);
                rooms.get(i).setRoomCleared();
            }

        }
        rooms.getFirst().setRoomCleared();
    }

    public void draw(Graphics g) {
        drawCurrentRoom(g);
    }

    public void drawCurrentRoom(Graphics g) {
        containingPlayer.draw(g);
    }

    public void drawMiniMap(Graphics g) {
        if (!showMiniMap) {
            return;
        }

        int miniOffset = 10;

        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(lowestRoomX() - miniOffset, lowestRoomY() - miniOffset, (highestRoomX() - lowestRoomX()) + miniOffset * 2, (highestRoomY() - lowestRoomY()) + miniOffset * 2);

        for (int i = 0; i < rects.size(); i++) {


            g.setColor(Color.gray);

            if (rooms.get(i).hasEnemies()) {
                g.setColor(Color.red);
            }

            if (rooms.get(i).hasBoss()) {
                g.setColor(Color.magenta);
            }

            if (rooms.get(i) == containingPlayer) {
                g.setColor(Color.blue);
            }
            g.fillRect(rects.get(i).x, rects.get(i).y, rects.get(i).w, rects.get(i).h);


            g.setColor(Color.WHITE);
            g.drawRect(rects.get(i).x, rects.get(i).y, rects.get(i).w, rects.get(i).h);

        }

        // draw connections
        for (int i = 0; i < rects.size(); i++) {
            g.setColor(Color.WHITE);
            g.drawString(i + "", rects.get(i).x, rects.get(i).y + rects.get(i).h / 2);

            // draw connections
            g.setColor(Color.YELLOW);


            // top/bottom connection (has a room above it)
            if (rects.get(i).dir[UP] == true) {
                g.fillRect((rects.get(i).x + miniMapScale/2), rects.get(i).y - 3, 5, 6);
            }

            // top/bottom connection (has a room below it)
            if (rects.get(i).dir[DOWN] == true) {
                g.fillRect((rects.get(i).x + miniMapScale/2), (rects.get(i).y + miniMapScale) - 3, 5, 6);
            }

            // left/right connection (has a room to it's left)
            if (rects.get(i).dir[LEFT] == true) {
                g.fillRect((rects.get(i).x - 3), (rects.get(i).y + miniMapScale/2), 6, 5);
            }

            // left/right connection (has a room to it's right)
            if (rects.get(i).dir[RIGHT] == true) {
                g.fillRect(((rects.get(i).x + miniMapScale) - 3), (rects.get(i).y + miniMapScale/2), 6, 5);
            }

        }
    }

    public void processNewRooms() {
        rects.addAll(newRoomsOnMap);
        rooms.addAll(newRooms);
        newRoomsOnMap.clear();
        newRooms.clear();
    }

    public boolean roomAtPosition(int x, int y) {
        for (Rect rect : rects) {
            if (rect.x == x && rect.y == y) {
                return true;
            }
        }

        for (Rect rect : newRoomsOnMap) {
            if (rect.x == x && rect.y == y) {
                return true;
            }
        }

        return false;
    }

    public Room getRoom(int x, int y) {
        for (Room rm : rooms) {
            if (rm.getX() == x && rm.getY() == y) {
                return rm;
            }
        }

        return null;
    }

    public void setRoomContainingPlayer(Room rm) {
        containingPlayer = rm;
    }

    public Room getRoomContainingPlayer() {
        return containingPlayer;
    }

    public static MapGen GetInstance() {
        return instance;
    }

    public void toggleCurrentRoomState(boolean val) {
        if (val) containingPlayer.setRoomCleared();
        if (!val) containingPlayer.setRoomUncleared();
    }

    public int lowestRoomX() {
        int low = rects.getFirst().x;

        for (Rect r : rects) {
            if (r.x < low) {
                low = r.x;
            }
        }

        return low;
    }

    public int lowestRoomY() {
        int low = rects.getFirst().y;

        for (Rect r : rects) {
            if (r.y < low) {
                low = r.y;
            }
        }

        return low;
    }

    public int highestRoomX() {
        int h = (rects.getFirst().x + rects.getFirst().w);

        for (Rect r : rects) {
            if ((r.x + r.w) > h) {
                h = r.x + r.w;
            }
        }


        return h;
    }

    public int highestRoomY() {
        int h = rects.getFirst().y + rects.getFirst().h;

        for (Rect r : rects) {
            if (r.y + r.h > h) {
                h = r.y + r.h;
            }
        }

        return h;
    }

    public void toggleMap(boolean val) {
        if (val && canSwitchMap) {
            showMiniMap = !showMiniMap;
            canSwitchMap = false;
        }

        if (!val) {
            canSwitchMap = true;
        }
    }

    public void snapMiniMapToCorner() {
        while (highestRoomX() != rw - 10) {
            for (Rect r : rects) {
                r.x += 1;
            }
        }

        while (lowestRoomY() != 10) {
            for (Rect r : rects) {
                r.y -= 1;
            }
        }
    }

    public int chanceInt(int percent) {
        return GameSeed.get().nextInt(100) < percent ? 1 : 0;
    }

    // Sets the boss rooms to be the fartest room that has one door
    public void setBossRoom() {
        Room farthestFromSpawn = roomsWithOneDoor().getFirst();
        double d = distanceBetweenRooms(rooms.getFirst(), farthestFromSpawn);

        for (int i = 0; i < roomsWithOneDoor().size(); i++) {
            if (distanceBetweenRooms(rooms.getFirst(), roomsWithOneDoor().get(i)) > d) {
                farthestFromSpawn = roomsWithOneDoor().get(i);
            }
        }

        farthestFromSpawn.giveBoss();
        Room.findBossDoor().hasCollision = true;
    }


    // finds all the rooms with one door
    public ArrayList<Room> roomsWithOneDoor() {
        ArrayList<Room> rms = new ArrayList<>();

        for (Room r : rooms) {
            if (r.numConnections() == 1) {
                rms.add(r);
            }
        }

        return rms;
    }

    public double distanceBetweenRooms(Room rm1, Room rm2) {
        return MyMath.distance(rm1.getX(), rm1.getY(), rm2.getX(), rm2.getY());
    }

    public void setConditionForUnlockingBoss() {
        for (Room r : rooms) {
            if (!r.hasBoss() && r != rooms.getFirst() && r.hasEnemies()) {
                LevelLogic.increaseNeededClears();
            }
        }
    }





}
