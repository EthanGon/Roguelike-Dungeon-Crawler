import java.awt.*;

public class Game extends GameBase {
    private Camera mainCam = new Camera();
    private MapGen map = new MapGen();
    private Player p = new Player(GetGameWidth()/2 - (96/2),GetGameHeight()/2 - (96/2), Player.DN, 96);


    public void inGameLoop() {

        if(pressing[_W])   p.goUP(5);
        if(pressing[_S])   p.goDN(5);
        if(pressing[_A])   p.goLT(5);
        if(pressing[_D])   p.goRT(5);
        p.move();

        if (pressing[_1]) {
            map.toggleCurrentRoomState(true);
        } else if (pressing[_2]) {
            map.toggleCurrentRoomState(false);
        }


        p.checkCollision();

    }

    public void paint(Graphics g) {
        map.draw(g);
        p.draw(g);
        map.drawMiniMap(g);
    }
}
