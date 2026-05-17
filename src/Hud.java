import javax.swing.*;
import java.awt.*;

public class Hud {

    private Image[] heartIcons = new Image[3];
    private int scale = 16 * 5;

    public Hud () {
        loadIcons();
    }

    public void loadIcons() {
        for (int i = 0; i < heartIcons.length; i++) {
            heartIcons[i] = new ImageIcon(getClass().getResource("ui_art/" + "heart_" + i + ".png")).getImage();
        }
    }

    public void draw(Graphics g) {
        drawHealthDisplay(g);
    }

    public void drawHealthDisplay(Graphics g) {
        for (int i = 0; i < Player.GetPlayer().getMaxHeath()/2; i++) {
            g.drawImage(heartIcons[2], i * scale, 0, scale, scale, null);
        }


        for (int i = 0; i < (Player.GetPlayer().getCurrentHP()); i++) {
            if (i == Player.GetPlayer().getCurrentHP()-1 && (Player.GetPlayer().getCurrentHP() % 2 != 0)) {
                g.drawImage(heartIcons[1], i/2*scale, 0, scale, scale, null);
                break;
            }
            g.drawImage(heartIcons[0], i/2 * scale, 0, scale, scale, null);
        }
    }


}
