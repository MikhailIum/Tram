public class RailBlock {
    int x;
    int y;
    static final int length = 5;
    static final int width = 2;
    Direction direction;
    int ang1;
    int ang2;

    RailBlock(int x, int y, Direction direction, int ang1, int ang2){
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.ang1 = ang1;
        this.ang2 = ang2;
    }
}
