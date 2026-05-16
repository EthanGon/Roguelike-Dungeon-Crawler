import java.awt.*;

public class Game extends GameBase {
    private Camera mainCam = new Camera();
    private MapGen map = new MapGen();
    private Player p = new Player(GetGameWidth()/2 - (96/2),GetGameHeight()/2 - (96/2), Player.DN, 96);
    private Enemy test = new Enemy("src/enemy_art/bat/bat", (int) p.x, (int) p.y, 96, 96, Rect.DN);
    private int playerSpeed = 5;


    public void inGameLoop() {



        if(pressing[_W])   p.goUP(playerSpeed);
        else if(pressing[_S])   p.goDN(playerSpeed);
        else if(pressing[_A])   p.goLT(playerSpeed);
        else if(pressing[_D])   p.goRT(playerSpeed);
        p.move();

        if (pressing[_1]) {
            map.toggleCurrentRoomState(true);
        } else if (pressing[_2]) {
            map.toggleCurrentRoomState(false);
        }


        p.checkCollision();

        test.chase(p);

    }

    public void paint(Graphics g) {
        map.draw(g);
        p.draw(g);
        test.draw(g);


        map.drawMiniMap(g);
    }
}
