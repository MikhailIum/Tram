import java.awt.*;
import java.util.Random;

public class Person {
    double x;
    double y;
    int height = 1;
    int width = 1;
    double speed = 1.4;
    double angleDeg;
    double acceleration = 0;
    Color color;

    Person(Frame frame){
        int dir = new Random().nextBoolean() ? 1: -1;
        x = dir * (new Random().nextInt(frame.mapBlock.width) + 50 + frame.x0);

        dir = new Random().nextBoolean() ? 1: -1;
        y = dir * (new Random().nextInt(frame.mapBlock.height) + 50 + frame.y0);

        angleDeg = new Random().nextInt(180);
        color = Color.orange;
    }

    public void checkCollision(Frame frame){
        Tram tram = frame.tram;
        if (x + width >= tram.x && x <= tram.x + tram.width && y + height >= tram.y && y <= tram.y + tram.height) {
//            frame.peopleToBeRemoved.add(this);
            color = Color.RED;
        }
    }
}
