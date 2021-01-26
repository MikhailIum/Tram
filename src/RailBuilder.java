import java.util.LinkedList;

public class RailBuilder {
    int x;
    int y;
    static final int R = 5;
    Direction direction;
    LinkedList<RailBlock> rail = new LinkedList<>();

    RailBuilder(int x, int y, Direction direction){
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.move();
        System.out.println("x = " + rail.getFirst().x + ", y = " + rail.getFirst().y + ", direction = " + rail.getFirst().direction);
    }

    void move(){
        x += direction.dx*RailBlock.length;
        y += direction.dy*RailBlock.length;
        rail.add(new RailBlock(x, y, direction, 0, 0));
    }

    void rotate(boolean isRight){
        if (direction == Direction.UP){
            if (isRight){
                x += RailBlock.width + R;
                direction = Direction.RIGHT;
                rail.add(new RailBlock(x, y, direction, 180, 90));
            }
            else {
                x -= R;
                direction = Direction.LEFT;
                rail.add(new RailBlock(x, y, direction, 0, 90));
            }
        }
        else if (direction == Direction.DOWN){
            if(isRight) {
                x -= RailBlock.width + R;
                direction = Direction.LEFT;
                rail.add(new RailBlock(x, y, direction, 0, -90));
            }
            else {
                x += R;
                direction = Direction.RIGHT;
                rail.add(new RailBlock(x, y, direction, 180, -90));
            }
        }
        else if (direction == Direction.RIGHT) {
            if (isRight) {
                y -= RailBlock.width + R;
                direction = Direction.DOWN;
                rail.add(new RailBlock(x, y, direction, 90, 0));
            } else {
                y -= R;
                direction = Direction.UP;
                rail.add(new RailBlock(x, y, direction, -90, 0));
            }
        }
        else {
            if(isRight) {
                y -= RailBlock.width + R;
                direction = Direction.UP;
                rail.add(new RailBlock(x, y, direction, -90, 180));
            }
            else {
                y += R;
                direction = Direction.DOWN;
                rail.add(new RailBlock(x, y, direction, 90, 180));
            }
        }

    }
}
