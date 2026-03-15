import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class MapGenTest {
    ArrayList<Rect> rects = new ArrayList<>();
    Queue<Rect> newRooms = new LinkedList<>();

    int maxRoom = 10;

    public MapGenTest() {
        long startTime = System.nanoTime();

        rects.add(new Rect(200, 200, 25, 25));
        Random random = new Random();

        while (rects.size() < maxRoom) {
            for (int i = 0; i < rects.size(); i++) {
                Rect curr = rects.get(i);

                // randomize which direction to spawn a room
                int dir = random.nextInt(4);

                if (curr.dir[dir] == true) { // already has a connection there
                    continue;
                }

                if (dir == 0) { // TOP
                    if (roomAtPosition(rects.get(i).x, rects.get(i).y - 25)) {continue;}
                    newRooms.add(new Rect(curr.x, curr.y - 25, 25, 25, 2));
                } else if (dir == 1) { // LEFT
                    if (roomAtPosition(rects.get(i).x - 25, rects.get(i).y)) {continue;}
                    newRooms.add(new Rect(curr.x - 25, curr.y, 25, 25, 3));
                } else if (dir == 2) { // BOTTOM
                    if (roomAtPosition(rects.get(i).x, rects.get(i).y + 25)) {continue;}
                    newRooms.add(new Rect(curr.x, curr.y + 25, 25, 25, 0));
                } else { // RIGHT
                    if (roomAtPosition(rects.get(i).x + 25, rects.get(i).y)) {continue;}
                    newRooms.add(new Rect(curr.x + 25, curr.y, 25, 25, 1));
                }
                curr.dir[dir] = true;

                // Check if processing all room results in max rooms
                if (rects.size() + newRooms.size() >= maxRoom) {
                    break;
                }

            }
            processNewRooms();

        }
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        System.out.println("Elapsed time: " + elapsedTime + " ns");

    }

    public void draw(Graphics g) {
        for (int i = 0; i < rects.size(); i++) {
            g.setColor(Color.black);

            if (i == 0) {
                g.setColor(Color.blue);
            }

            g.fillRect(rects.get(i).x, rects.get(i).y, rects.get(i).w, rects.get(i).h);

            g.setColor(Color.magenta);
            rects.get(i).draw(g);

        }

        for (int i = 0; i < rects.size(); i++) {
            g.setColor(Color.WHITE);
            g.drawString(i + "", rects.get(i).x, rects.get(i).y + rects.get(i).h / 2);

            // draw connections
            g.setColor(Color.GREEN);

            // top/bottom connection (has a room above it)
            if (rects.get(i).dir[0] == true) {
                g.fillRect((rects.get(i).x + 12), rects.get(i).y - 3, 1, 6);
            }

            // top/bottom connection (has a room below it)
            if (rects.get(i).dir[2] == true) {
                g.fillRect((rects.get(i).x + 12), (rects.get(i).y + 25) - 3, 1, 6);
            }

            // left/right connection (has a room to it's left)
            if (rects.get(i).dir[1] == true) {
                g.fillRect((rects.get(i).x - 3), (rects.get(i).y + 12), 6, 1);
            }

            // left/right connection (has a room to it's right)
            if (rects.get(i).dir[3] == true) {
                g.fillRect(((rects.get(i).x + 25) - 3), (rects.get(i).y + 12), 6, 1);
            }

        }


    }

    public void processNewRooms() {
        for (Rect rect : newRooms) {
            rects.add(rect);
        }
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

        for (Rect rect : newRooms) {
            if (rect.x == x && rect.y == y) {
                return true;
            }
        }

        return false;
    }


}
