

import java.awt.Color;

public abstract class Mino {
    protected int[][][] shapes;
    protected int form;
    public static final int[] COLORS = {
        Color.CYAN.getRGB(),
        Color.BLUE.getRGB(),
        Color.ORANGE.getRGB(),
        Color.YELLOW.getRGB(),
        Color.GREEN.getRGB(),
        Color.MAGENTA.getRGB(),
        Color.RED.getRGB(),
        Color.DARK_GRAY.getRGB()
    };

    /**
     * Get the shape of the mino at a specific rotation.
     * 
     * @param rotation The rotation index.
     * @return The 2D array representing the shape.
     */
    public int[][] getShape(int rotation) {
        return shapes[rotation % shapes.length];
    }

    /**
     * Get the color of the mino.
     * 
     * @return The color of the mino.
     */
    public Color getColor() {
        return new Color(COLORS[form]);
    }

    /**
     * Get the form of the mino.
     * 
     * @return The form of the mino.
     */
    public int getForm() {
        return form;
    }
}
