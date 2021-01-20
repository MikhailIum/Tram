public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    int dx;
    int dy;
    Direction(int dx, int dy){
        this.dx = dx;
        this.dy = dy;
    }
}
