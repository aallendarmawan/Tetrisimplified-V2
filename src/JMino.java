

public class JMino extends Mino {
    public JMino() {
        shapes = new int[][][]{
            {{1, 0, 0}, {1, 1, 1}},
            {{0, 1}, {0, 1}, {1, 1}},
            {{1, 1, 1}, {0, 0, 1}},
            {{1, 1}, {1, 0}, {1, 0}}
        };
        form = 1;
    }
}
