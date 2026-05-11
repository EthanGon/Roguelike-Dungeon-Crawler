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
    private MapGen mapGen;
    private Camera mainCam;


    public GamePanel() {
        this.setBackground(Color.WHITE);

        mainCam = new Camera();
        mapGen  = new MapGen();
        player  = new Player((getWidth()/2) - 32, (getHeight()/2) - 32);

        addKeyListener(this);
        requestFocus();
        thread = new Thread(this);
        thread.start();

    }

    public void paint(Graphics g) {
        mapGen.draw(g);

        g.setColor(Color.YELLOW);
        player.draw(g);


    }

    @Override
    public void run() {
        while (true) {

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

    }

    @Override
    public void keyReleased(KeyEvent e) {

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

//        g.setColor(Color.BLACK);
//        for (int y = 0; y < mapSizeY; y++) {
//            for (int x = 0; x < mapSizeX; x++) {
//                g.fillRect(x * pixelSize, y * pixelSize, pixelSize-1, pixelSize-1);
//            }
//        }
