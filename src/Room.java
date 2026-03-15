import javax.swing.*;
import java.awt.*;



public class Room {

    Image roomImage =           new ImageIcon(getClass().getResource("room_art/room.png")).getImage();
    Image northSouthDoorOff =   new ImageIcon(getClass().getResource("room_art/ns-blockade-off.png")).getImage();
    Image eastWestDoorOff =     new ImageIcon(getClass().getResource("room_art/ew-blockade-off.png")).getImage();
    private int pixelSize = 96;

    Rect[] roomBounds = new Rect[8];

    private Room north, south, east, west;
    int x;
    int y;

    public Room(int x, int y) {
        this.x = x;
        this.y = y;
        initRoomBounds();

    }


    public void draw(Graphics g) {
        g.drawImage(roomImage, x, y, null);

        // North Connection
        g.drawImage(northSouthDoorOff, x + 576, y, null);

        // South Connection
        g.drawImage(northSouthDoorOff, x + 576, y + 864, null);

        // West Connection
        g.drawImage(eastWestDoorOff, x, y + 384, null);

        // East Connection
        g.drawImage(eastWestDoorOff, x + 1248, y + 384, null);

        drawRoomBounds(g);

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
    }

    private void drawRoomBounds(Graphics g) {
        g.setColor(Color.red);
        for (int i = 0; i < roomBounds.length; i++) {
            roomBounds[i].draw(g);
        }
    }

}
