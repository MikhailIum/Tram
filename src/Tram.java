public class Tram {
    int x;
    int y;
    int width = 150;
    int height = 200;

    public Tram(Rail rail) {
        this.x = rail.x - 25;
        this.y = rail.height + height;
    }
}
