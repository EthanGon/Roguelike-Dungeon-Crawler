import javax.swing.*;
import java.awt.*;

public class Room {

    Image roomImage = new ImageIcon(getClass().getResource("room.png")).getImage();

    private Room north, south, east, west;


    public void draw(Graphics g) {
        g.drawImage(roomImage, 0, 0, null);
    }

}
