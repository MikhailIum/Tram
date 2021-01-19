public class Tram {
    double x;
    double y;
    int width = 2;
    int height = 35;

    public Tram(RailBlock railBlock) {
        this.x = railBlock.x - 0.15;
        this.y = railBlock.y - (height - railBlock.height);
    }
}
