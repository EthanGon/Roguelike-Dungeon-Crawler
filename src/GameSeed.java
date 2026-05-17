import java.util.Random;

public class GameSeed {
    private static int seed;
    private static Random random;

    public static void init(long seed) {
        random = new Random(seed);
    }

    public static void init() {
        random = new Random();
    }

    public static Random get() {
        return random;
    }
}
