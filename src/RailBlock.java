public class RailBlock {
    int x;
    int y;
    static final int length = 2;
    static final int width = 2;
    Direction direction;
    int ang1;
    int ang2;
    boolean isRotate;
    int xCenter;
    int yCenter;

    RailBlock(int x, int y, Direction direction, int ang1, int ang2, boolean isRotate, int xCenter, int yCenter){
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.ang1 = ang1;
        this.ang2 = ang2;
        this.isRotate = isRotate;
        this.xCenter = xCenter;
        this.yCenter = yCenter;
    }

    RailBlock(int x, int y, Direction direction, boolean isRotate){
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.isRotate = isRotate;
    }
}
