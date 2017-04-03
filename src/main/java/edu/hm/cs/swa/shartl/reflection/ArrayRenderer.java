package edu.hm.cs.swa.shartl.reflection;

/**
 * Implementation of ArrayRenderer.
 */
public class ArrayRenderer {
    /**
     * Renders an integer array.
     * @param array array to render.
     * @return information about given array.
     */
    public String render(int[] array) {
        final StringBuilder builder = new StringBuilder();
        builder.append("(Type int[]): ");
        builder.append('[');
        for (int index = 0; index < array.length; index++) {
            builder.append(array[index]);
            builder.append(", ");
        }
        builder.append("]\n");
        return builder.toString();
    }
}
