

public class LMino extends Mino {
    public LMino() {
        shapes = new int[][][]{
            {{0, 0, 1}, {1, 1, 1}},
            {{1, 1}, {0, 1}, {0, 1}},
            {{1, 1, 1}, {1, 0, 0}},
            {{1, 0}, {1, 0}, {1, 1}}
        };
        form = 2;
    }
}
