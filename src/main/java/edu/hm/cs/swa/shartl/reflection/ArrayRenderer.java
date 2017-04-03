package edu.hm.cs.swa.shartl.reflection;

public class ArrayRenderer {

    public String render(int[] array) {
        final StringBuilder builder = new StringBuilder();
        builder.append("");
        builder.append(" (Type int[]): ");
        builder.append('[');
        for (int index = 0; index < array.length; index++) {
            builder.append(array[index]);
            builder.append(", ");
        }
        builder.append("]\n");
        return builder.toString();
    }
}
