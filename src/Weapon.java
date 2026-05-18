import javax.swing.*;
import java.awt.*;

public class Weapon extends Rect {

    private Image[] weaponDirection = new Image[4];
    private boolean isActive = false;
    private int collOffset = 25;

    public Weapon(int x, int y, int w, int h) {
        super(x, y, w, h);
        this.project = false;
        loadImages();
    }

    private void loadImages() {
        for (int i = 0; i < weaponDirection.length; i++) {
            weaponDirection[i] = new ImageIcon(getClass().getResource("weapon/" + "wpn_" + i + ".png")).getImage();
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive() {
        isActive = true;
    }

    public void setInactive() {
        isActive = false;
    }

    public void draw(Graphics g) {
        int offset = 15;

        g.setColor(Color.blue);
        g.drawRect(x, y, w, h);
        if (Player.GetPlayer().getDir() == UP) {g.drawImage(weaponDirection[Player.GetPlayer().getDir()], Player.GetPlayer().x, (Player.GetPlayer().y - 96) + offset, null);}
        if (Player.GetPlayer().getDir() == DN) {g.drawImage(weaponDirection[Player.GetPlayer().getDir()], Player.GetPlayer().x, (Player.GetPlayer().y + 96) - offset, null);}
        if (Player.GetPlayer().getDir() == LT) {g.drawImage(weaponDirection[Player.GetPlayer().getDir()], (Player.GetPlayer().x - 96) + offset, Player.GetPlayer().y, null);}
        if (Player.GetPlayer().getDir() == RT) {g.drawImage(weaponDirection[Player.GetPlayer().getDir()], (Player.GetPlayer().x + 96) - offset, Player.GetPlayer().y, null);}
    }

    public void setPosition() {
        int offset = 15;
        if (Player.GetPlayer().getDir() == UP) {setPosition(Player.GetPlayer().x, (Player.GetPlayer().y - 96) + offset);}
        if (Player.GetPlayer().getDir() == DN) {setPosition(Player.GetPlayer().x, (Player.GetPlayer().y + 96) - offset);}
        if (Player.GetPlayer().getDir() == LT) {setPosition((Player.GetPlayer().x - 96) + offset, Player.GetPlayer().y);}
        if (Player.GetPlayer().getDir() == RT) {setPosition((Player.GetPlayer().x + 96) - offset, Player.GetPlayer().y);}
        setWeaponBox();

    }

    public void setWeaponBox() {
        if (Player.GetPlayer().getDir() == UP || Player.GetPlayer().getDir() == DN) {
            this.w = 96 - collOffset;
            this.h = 96;
            this.x = x + (collOffset/2);
        } else if (Player.GetPlayer().getDir() == LT || Player.GetPlayer().getDir() == RT) {
            this.h = 96 - collOffset;
            this.w = 96;
            this.y = y + (collOffset/2);
        }
    }

    public void setPosition(int dx, int dy) {
        x = dx;
        y = dy;
    }

    public void setDir(int i) {
        direction = i;
    }
}
