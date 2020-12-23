import java.util.Random;

public class Person {
    int x;
    int y;
    int height = 100;
    int width = 100;
    double speed = 2;
    double angleDeg;

    Person(Frame frame){
        x = new Random().nextInt((int) (frame.currentScale * frame.getWidth())) + frame.x0;
        y = new Random().nextInt(frame.rail.height);
        angleDeg = new Random().nextInt(180);
    }

    public void checkCollision(Frame frame){
        Tram tram = frame.tram;
        if (x + width >= tram.x && x <= tram.x + tram.width && y + height >= tram.y && y <= tram.y + tram.height){
            frame.peopleToBeRemoved.add(this);
        }
    }
}
