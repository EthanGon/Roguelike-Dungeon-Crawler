import javax.swing.*;
import java.awt.*;

public class Hud {

    private Image restartScreen =           new ImageIcon(getClass().getResource("restart_screen.png")).getImage();
    private Image[] heartIcons = new Image[3];
    private int scale = 16 * 5;
    private int rw = 96 * 14;
    private int rh = 96 * 10;

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

        if (!Player.GetPlayer().isAlive() || !Enemy.boss.isAlive()) {
            drawRestartScreen(g);
        }

        if (MapGen.GetInstance().getRoomContainingPlayer() == Room.getBossRoom() && Enemy.boss.isAlive()) {
            drawBossHealth(g);
        }
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

    public void drawRestartScreen(Graphics g) {
        g.drawImage(restartScreen, 0, 0, null);
    }

    public void drawBossHealth(Graphics g) {
        int bw = 500;
        int bh = 50;
        int yOffset = 20;

        Enemy boss = Enemy.boss;

        double bossHPPercentage = (double) boss.enemyHP / boss.enemyMaxHP;

        g.setColor(new Color(0, 0, 0, 137));
        g.fillRect((this.rw / 2) - (bw/2),yOffset, bw, bh);
        g.setColor(Color.RED);
        g.fillRect((this.rw / 2) - (bw/2),yOffset, (int) (((bossHPPercentage*100)/100) * bw), bh);

    }


}
