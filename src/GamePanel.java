import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends Canvas implements Runnable, KeyListener {

    private Thread thread;
    private Player player;
    private Image offScreenDrawing;
    private Graphics offScreenPen;

    private int pixelSize = 96;
    private int mapSizeX = 14;
    private int mapSizeY = 10;
    Room r1 = new Room();


    public GamePanel() {
        this.setBackground(Color.WHITE);
        this.setSize(pixelSize * mapSizeX, pixelSize * mapSizeY);
        player = new Player();

        addKeyListener(this);
        requestFocus();
        thread = new Thread(this);
        thread.start();

    }

    public void paint(Graphics g) {
//        g.setColor(Color.BLACK);
//        for (int y = 0; y < mapSizeY; y++) {
//            for (int x = 0; x < mapSizeX; x++) {
//                g.fillRect(x * pixelSize, y * pixelSize, pixelSize-1, pixelSize-1);
//            }
//        }

        r1.draw(g);

        g.setColor(Color.YELLOW);
        player.draw(g);
    }

    @Override
    public void run() {
        while (true) {


            player.move();
            repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }



    @Override
    public void keyPressed(KeyEvent e) {
        player.pressingKey[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.pressingKey[e.getKeyCode()] = false;
    }

    // Creates the back buffer to draw on
    public void initBuffer() {
        offScreenDrawing = createImage(getWidth(), getHeight());
        offScreenPen = offScreenDrawing.getGraphics();
    }

    // Handles drawing everything to the offscreen, since update is called first, then  after that's done show it
    public void update(Graphics g) {
        offScreenPen.clearRect(0, 0, this.getWidth(), this.getHeight());
        paint(offScreenPen);
        g.drawImage(offScreenDrawing, 0, 0, this);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
