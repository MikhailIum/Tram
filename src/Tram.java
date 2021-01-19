import java.util.LinkedList;

public class Tram {
    double x;
    double y;
    int width = 2;
    int height = 35;

    public Tram(LinkedList<RailBlock> rail) {
        this.x = rail.getFirst().x - 0.15;
        this.y = rail.getFirst().y + rail.getFirst().height - height;
    }
}
