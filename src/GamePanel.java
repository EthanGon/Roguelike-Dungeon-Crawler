import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends Canvas implements Runnable, KeyListener {

    private Thread thread;
    private Player player = new Player(50,50);
    private Image offScreenDrawing;
    private Graphics offScreenPen;

    public GamePanel() {
        this.setBackground(Color.WHITE);
        this.setSize(1280, 720);

        addKeyListener(this);
        requestFocus();
        thread = new Thread(this);
        thread.start();

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

    public void paint(Graphics g) {
        player.draw(g);
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            player.move();

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

    @Override
    public void keyTyped(KeyEvent e) {}
}
