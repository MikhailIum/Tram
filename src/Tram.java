public class Tram {
    int x;
    double y;
    int width = 2;
    int height = 35;

    public Tram(RailBlock railBlock) {
        this.x = railBlock.x - 25;
        this.y = railBlock.y - (height - railBlock.height);
    }
}
