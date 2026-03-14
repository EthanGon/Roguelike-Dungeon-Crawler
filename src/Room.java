import javax.swing.*;
import java.awt.*;



public class Room {

    Image roomImage = new ImageIcon(getClass().getResource("room_art/room.png")).getImage();
    Image northSouthDoorOff = new ImageIcon(getClass().getResource("room_art/ns-blockade-off.png")).getImage();
    Image eastWestDoorOff = new ImageIcon(getClass().getResource("room_art/ew-blockade-off.png")).getImage();
    int x = 0;
    int y = 0;

    private Room north, south, east, west;


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
    }

}
