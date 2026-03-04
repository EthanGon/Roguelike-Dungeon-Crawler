import java.awt.*;

public class GamePanel extends Canvas implements Runnable {

    private Thread thread;
    private Player player = new Player(50,50);

    public GamePanel() {
        this.setBackground(Color.BLUE);
        this.setSize(1280, 720);

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

            System.out.println("Running");
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
