import com.sun.xml.internal.bind.v2.TODO;

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
//        x = new Random().nextInt(frame.mapBlock.width + 100) - 50  + frame.x0;
//
//        if (x >= frame.x0 && x <= frame.x0 + frame.getWidth())
//            y = new Random().nextInt(frame.mapBlock.height) - 50 + frame.y0;
//        else y = new Random().nextInt(frame.mapBlock.height + 50) - 50 + frame.y0;

        //TODO: вернуть

        do {
            x = new Random().nextInt(frame.mapBlock.width) + frame.x0;
            y = new Random().nextInt(frame.mapBlock.height) + frame.y0;
        } while (((frame.tram.currentRailBlock.direction == Direction.UP && x + width >= frame.tram.currentRailBlock.x - 2 && x <= frame.tram.currentRailBlock.x + RailBlock.width + 2 && y <= frame.tram.currentRailBlock.y && y + height >= frame.tram.currentRailBlock.y - RailBlock.width)
                || (frame.tram.currentRailBlock.direction == Direction.RIGHT && x + width >= frame.tram.currentRailBlock.x && x <= frame.tram.currentRailBlock.x + RailBlock.width && y <= frame.tram.currentRailBlock.y + RailBlock.width + 2 && y + height >= frame.tram.currentRailBlock.y - 2)
                || (frame.tram.currentRailBlock.direction == Direction.LEFT && x + width >= frame.tram.currentRailBlock.x - RailBlock.width && x <= frame.tram.currentRailBlock.x && y <= frame.tram.currentRailBlock.y + 2 && y + height >= frame.tram.currentRailBlock.y - RailBlock.width - 2)));
        angleDeg = new Random().nextInt(180);
        color = Color.orange;
    }

    Person(double x, double y, double angleDeg){
        this.x = x;
        this.y = y;
        this.angleDeg = angleDeg;
        color = Color.orange;
    }

    public void checkCollision(Frame frame){
        Tram tram = frame.tram;
        if (x + width >= tram.x && x <= tram.x + tram.width && y + height >= tram.y && y <= tram.y + tram.height) {
            frame.peopleToBeRemoved.add(this);
            frame.deadPeople.add(this);
            color = Color.RED;
            frame.points -= 50;
        }
    }
}
