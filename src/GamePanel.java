import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends Canvas implements Runnable, KeyListener {

    private Thread thread;
    private Player player = new Player(50,50);

    public GamePanel() {
        this.setBackground(Color.WHITE);
        this.setSize(1280, 720);

        addKeyListener(this);
        requestFocus();
        thread = new Thread(this);
        thread.start();


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
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }



    @Override
    public void keyPressed(KeyEvent e) {
        player.pressingKey[e.getKeyCode()] = true;
        System.out.println(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.pressingKey[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
