import java.awt.*;

public class Game extends GameBase {

    private Camera mainCam;
    private MapGen map;
    private Player p;
    private int playerSpeed;
    private LevelMusic bgm;
    private Hud ui;

    @Override
    public void start() {
        GameSeed.init();
        mainCam = new Camera();
        map = new MapGen();
        p = new Player(GetGameWidth()/2 - (96/2),GetGameHeight()/2 - (96/2), Player.DN, 96);
        playerSpeed = 5;
        bgm = new LevelMusic("src/sfx/binding_of_isaac_track.wav");
        bgm.loop();
        ui = new Hud();
    }

    public void inGameLoop() {

        handleInput();
        p.update();

    }

    public void paint(Graphics g) {
        map.draw(g);
        p.draw(g);
        map.drawMiniMap(g);
        ui.draw(g);
    }

    public void handleInput() {
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

        map.toggleMap(pressing[_Q]);

        if (pressing[_9] ) {
            p.increaseCurrentHP();
        }

        if (pressing[_8]) {
            p.takeDamage();
            System.out.println(p.getCurrentHP());
        }

        if (pressing[SPACE]) {
            p.useWeapon();
        } else {
            p.hideWeapon();
        }
    }


}
