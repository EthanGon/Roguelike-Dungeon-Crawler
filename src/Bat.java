public class Bat extends Enemy {

    public Bat (int x, int y) {
        super("src/enemy_art/bat/bat", x, y);
        this.alwaysMove = true;
        this.moving = true;
    }
}
