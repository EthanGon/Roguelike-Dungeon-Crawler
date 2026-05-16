import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class MapGen {
    private ArrayList<Rect> rects = new ArrayList<>();
    private ArrayList<Room> rooms = new ArrayList<>();
    private Queue<Rect> newRoomsOnMap = new LinkedList<>();
    private Queue<Room> newRooms = new LinkedList<>();
    private Room containingPlayer;
    private static MapGen instance;
    private int maxRoom = 12;

    public int rw = 96 * 14;
    public int rh = 96 * 10;
    private final int UP = 0;
    private final int LEFT = 1;
    private final int DOWN = 2;
    private final int RIGHT = 3;
    public boolean showMiniMap = true;
    public boolean canSwitchMap = true;

    public MapGen() {
        instance = this;
        createRooms();
    }

    public void createRooms() {
        long startTime = System.nanoTime();

        // creates the starting room
        rects.add(new Rect(200, 200, 25, 25));
        rooms.add(new Room(0,0));
        containingPlayer = rooms.getFirst();
        Random random = new Random();

        while (rects.size() < maxRoom) {
            for (int i = 0; i < rects.size(); i++) {

                Rect curr = rects.get(i);
                Room currRoom = rooms.get(i);

                int dir = random.nextInt(4);

                if (curr.dir[dir] == true && currRoom.hasAdjacentRoom(dir)) { // already has a connection there
                    continue;
                }

                Room newRoomToAdd;

                // directions go counter-clockwise
                if (dir == UP) { // TOP
                    if (roomAtPosition(rects.get(i).x, rects.get(i).y - 25)) {continue;}
                    newRoomsOnMap.add(new Rect(curr.x, curr.y - 25, 25, 25, DOWN));

                    newRoomToAdd = new Room(currRoom.getX(), currRoom.getY() - rh, DOWN, currRoom);
                    newRoomToAdd.getDoor(DOWN).hasCollision = true;
                    newRooms.add(newRoomToAdd);

                } else if (dir == LEFT) { // LEFT
                    if (roomAtPosition(rects.get(i).x - 25, rects.get(i).y)) {continue;}
                    newRoomsOnMap.add(new Rect(curr.x - 25, curr.y, 25, 25, 3));

                    newRoomToAdd = new Room(currRoom.getX() - rw, currRoom.getY(), 3, currRoom);
                    newRoomToAdd.getDoor(3).hasCollision = true;
                    newRooms.add(newRoomToAdd);
                } else if (dir == DOWN) { // BOTTOM
                    if (roomAtPosition(rects.get(i).x, rects.get(i).y + 25)) {continue;}
                    newRoomsOnMap.add(new Rect(curr.x, curr.y + 25, 25, 25, 0));

                    newRoomToAdd = new Room(currRoom.getX(), currRoom.getY() + rh, 0, currRoom);
                    newRoomToAdd.getDoor(0).hasCollision = true;
                    newRooms.add(newRoomToAdd);
                } else { // RIGHT
                    if (roomAtPosition(rects.get(i).x + 25, rects.get(i).y)) {continue;}
                    newRoomsOnMap.add(new Rect(curr.x + 25, curr.y, 25, 25, 1));

                    newRoomToAdd = new Room(currRoom.getX() + rw, currRoom.getY(), 1, currRoom);
                    newRoomToAdd.getDoor(1).hasCollision = true;
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

        // Decide if room should have enemies
        for (int i = 0; i < rooms.size(); i++) {
            int spawnEnemies = random.nextInt(2);

            if (spawnEnemies == 0) {
                rooms.get(i).giveEnemies(true);
                rooms.get(i).setRoomUncleared();
            } else {
                rooms.get(i).giveEnemies(false);
                rooms.get(i).setRoomCleared();
            }

        }
        rooms.getFirst().setRoomCleared();

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        System.out.println("Map Creation Elapsed Time: " + (elapsedTime / 1e+9 + " seconds"));
    }

    public void draw(Graphics g) {
        drawRooms(g);

    }

    public void drawRooms(Graphics g) {
        containingPlayer.draw(g);
    }

    public void drawMiniMap(Graphics g) {
        if (!showMiniMap) {
            return;
        }

        int ox = 3;
        int oy = 3;
        int miniOffset = 10;

        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(lowestRoomX() - miniOffset, lowestRoomY() - miniOffset, (highestRoomX() - lowestRoomX()) + miniOffset * 2, (highestRoomY() - lowestRoomY()) + miniOffset * 2);

        for (int i = 0; i < rects.size(); i++) {


            g.setColor(Color.gray);

            if (rooms.get(i).hasEnemies()) {
                g.setColor(Color.red);
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
            g.setColor(Color.GREEN);

            // top/bottom connection (has a room above it)
            if (rects.get(i).dir[UP] == true) {
                g.fillRect((rects.get(i).x + 12), rects.get(i).y - 3, 1, 6);
            }

            // top/bottom connection (has a room below it)
            if (rects.get(i).dir[DOWN] == true) {
                g.fillRect((rects.get(i).x + 12), (rects.get(i).y + 25) - 3, 1, 6);
            }

            // left/right connection (has a room to it's left)
            if (rects.get(i).dir[LEFT] == true) {
                g.fillRect((rects.get(i).x - 3), (rects.get(i).y + 12), 6, 1);
            }

            // left/right connection (has a room to it's right)
            if (rects.get(i).dir[RIGHT] == true) {
                g.fillRect(((rects.get(i).x + 25) - 3), (rects.get(i).y + 12), 6, 1);
            }

        }
    }

    public void processNewRooms() {
        rects.addAll(newRoomsOnMap);

        rooms.addAll(newRooms);
        newRoomsOnMap.clear();
        newRooms.clear();
    }

    // lazy check for if a room exist at a pos
    /* Checking if a room already exist in a certain position or will exist in a certain position
    Could probably replace this logic with a hashmap but I'm lazy rn
    * */
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

        // TODO: rm.spawnEnemies if hasEnemies && notCleared
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




}
