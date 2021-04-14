import java.util.LinkedList;

public class Tram {
    double x;
    double y;
    int width = 3;
    int height = 5;
    //TODO: height = 35
    double ang;
    RailBlock currentRailBlock;
    LinkedList<RailBlock> rail;

    public Tram(LinkedList<RailBlock> rail) {
        this.x = rail.getFirst().x;
        this.y = rail.getFirst().y;
        currentRailBlock = rail.getFirst();
        this.rail = rail;
    }
}
