

public class TMino extends Mino {
    public TMino() {
        shapes = new int[][][]{
            {{0, 1, 0}, {1, 1, 1}},
            {{1, 0}, {1, 1}, {1, 0}},
            {{1, 1, 1}, {0, 1, 0}},
            {{0, 1}, {1, 1}, {0, 1}}
        };
        form = 5;
    }
}
