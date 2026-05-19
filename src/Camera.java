public class Camera {
    public int x;
    public int y;

    private static Camera instance;

    public Camera() {
        instance = this;
    }

    public static Camera GetInstance() {
        return instance;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }



}
