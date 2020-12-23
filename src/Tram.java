public class Tram {
    int x;
    int y;
    int width = 150;
    int height = 200;

    public Tram(RailBlock railBlock) {
        this.x = railBlock.x - 25;
        this.y = railBlock.y + height;
    }
}
