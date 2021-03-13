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
    }

    void move(){
        rail.add(new RailBlock(x, y, direction, false));
        x += direction.dx*RailBlock.length;
        y += direction.dy*RailBlock.length;
    }

    void deleteFirst(){
        rail.removeFirst();
    }

    void rotate(boolean isRight){
        if (direction == Direction.UP){
            if (isRight){
                direction = Direction.RIGHT;
                xCenter = x + RailBlock.width + R;
                yCenter = y;
                rail.add(new RailBlock(x, y, direction, 180, -90, true, xCenter, yCenter));
                x += RailBlock.width + R;
                y -= RailBlock.width + R;
            }
            else {
                direction = Direction.LEFT;
                xCenter = x - R;
                yCenter = y;
                rail.add(new RailBlock(x, y, direction, 0, 90, true, xCenter, yCenter));
                x -= R;
                y -= R;
            }
        }
        else if (direction == Direction.RIGHT) {
                direction = Direction.UP;
                yCenter = y - R;
                xCenter = x;
                rail.add(new RailBlock(x, y, direction, -90, 90, true, xCenter, yCenter));
                x += R;
                y -= R;
        }
        else {
                direction = Direction.UP;
                yCenter = y - RailBlock.width - R;
                xCenter = x;
                rail.add(new RailBlock(x, y, direction, -90, -90, true, xCenter, yCenter));
                x -= RailBlock.width + R;
                y -= RailBlock.width + R;
        }

    }
}
