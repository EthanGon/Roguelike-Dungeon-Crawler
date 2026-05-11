import java.awt.*;

public class Game extends GameBase {
    private Camera mainCam = new Camera();
    private Player p = new Player(GetGameWidth()/2 - (64/2),GetGameHeight()/2 - (64/2));
    private MapGen map = new MapGen();


    public void inGameLoop() {

        if(pressing[_W])   p.moveUP(5);
        if(pressing[_S])   p.moveDN(5);
        if(pressing[_A])   p.moveLT(5);
        if(pressing[_D])   p.moveRT(5);
        p.checkCollision();

    }

    public void paint(Graphics g) {
        map.draw(g);
        p.draw(g);
    }
}
