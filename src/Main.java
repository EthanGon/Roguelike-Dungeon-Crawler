import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        GamePanel gameScreen = new GamePanel();

        JFrame frame = new JFrame("Roguelike Dungeon Crawler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gameScreen);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        gameScreen.initBuffer();


    }
}