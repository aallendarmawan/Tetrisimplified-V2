


public class ZMino extends Mino {
    public ZMino() {
        shapes = new int[][][]{
            {{1, 1, 0}, {0, 1, 1}},
            {{0, 1}, {1, 1}, {1, 0}},
            {{1, 1, 0}, {0, 1, 1}},
            {{0, 1}, {1, 1}, {1, 0}}
        };
        form = 6;
    }
}
