import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        GamePanel gameScreen = new GamePanel();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gameScreen);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }
}