import java.util.LinkedList;

public class RailBuilder {
    int x;
    int y;
    int xCenter;
    int yCenter;
    static final int R = 1;
    Direction direction;
    LinkedList<RailBlock> rail = new LinkedList<>();

    RailBuilder(int x, int y, Direction direction){
        this.x = x;
        this.y = y;
        this.xCenter = x;
        this.yCenter = y;
        this.direction = direction;
        this.move();
        System.out.println("x = " + rail.getFirst().x + ", y = " + rail.getFirst().y + ", direction = " + rail.getFirst().direction);
    }

    void move(){
        rail.add(new RailBlock(x, y, direction, false));
        x += direction.dx*RailBlock.length;
        y += direction.dy*RailBlock.length;
    }

    void rotate(boolean isRight){
        if (direction == Direction.UP){
            if (isRight){
                direction = Direction.RIGHT;
                xCenter = x + RailBlock.width + R;
                yCenter = y;
                x += RailBlock.length + R;
                y -= RailBlock.length + R;
                rail.add(new RailBlock(x, y, direction, 180, 90, true, xCenter, y));
            }
            else {
                direction = Direction.LEFT;
                xCenter = x - R;
                yCenter = y;
                x -= R;
                y -= R;
                rail.add(new RailBlock(x, y, direction, 0, 90, true, xCenter, yCenter));
            }
        }
        else if (direction == Direction.DOWN){
            if(isRight) {
                direction = Direction.LEFT;
                xCenter = x - RailBlock.width + R;
                yCenter = y;
                x -= R;
                y += R;
                rail.add(new RailBlock(x, y, direction, 0, -90, true, xCenter, yCenter));
            }
            else {
                direction = Direction.RIGHT;
                xCenter = x + R;
                yCenter = y;
                x += RailBlock.length + R;
                y += RailBlock.length + R;
                rail.add(new RailBlock(x, y, direction, 180, -90, true, xCenter, yCenter));
            }
        }
        else if (direction == Direction.RIGHT) {
            if (isRight) {
                direction = Direction.DOWN;
                yCenter = y - RailBlock.width + R;
                xCenter = x;
                x += RailBlock.length + R;
                y += RailBlock.length + R;
                rail.add(new RailBlock(x, y, direction, 90, 0, true, xCenter, yCenter));
            } else {
                direction = Direction.UP;
                yCenter = y - R;
                xCenter = x;
                x += R;
                y -= R;
                rail.add(new RailBlock(x, y, direction, -90, 0, true, xCenter, yCenter));
            }
        }
        else {
            if(isRight) {
                direction = Direction.UP;
                yCenter = y - RailBlock.width + R;
                xCenter = x;
                x -= RailBlock.length + R;
                y -= RailBlock.length + R;
                rail.add(new RailBlock(x, y, direction, -90, 180, true, xCenter, yCenter));
            }
            else {
                direction = Direction.DOWN;
                yCenter = x + R;
                xCenter = x;
                x -= R;
                y += R;
                rail.add(new RailBlock(x, y, direction, 90, 180, true, xCenter, yCenter));
            }
        }

    }
}
