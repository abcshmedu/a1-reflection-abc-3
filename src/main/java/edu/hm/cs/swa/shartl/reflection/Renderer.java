package edu.hm.cs.swa.shartl.reflection;

import java.lang.reflect.Field;

/**
 * Renderer, which outputs string of @RenderMe vars.
 */
public class Renderer {
    private final Object object;

    /**
     * Constructor to get object to render.
     *
     * @param object which have to be rendered.
     */
    public Renderer(final Object object) {
        this.object = object;
    }

    /**
     * Render the given object.
     *
     * @return String output of methods with @RenderMe.
     * @throws IllegalAccessException if field can't get read via reflection.
     */
    public String render() throws IllegalAccessException {
        final Class toRender = this.object.getClass();
        final Field[] fields = toRender.getDeclaredFields();
        final StringBuilder builder = new StringBuilder();
        builder.append("Instance of ");
        builder.append(toRender.getCanonicalName());
        builder.append(":\n");
        for (final Field field : fields) {
            if (field.isAnnotationPresent(edu.hm.cs.swa.shartl.reflection.RenderMe.class)) {
                field.setAccessible(true);
                builder.append(field.getName());
                builder.append(" (Type ");
                if (field.getType().isPrimitive()) {
                    builder.append(field.getType().getSimpleName());
                } else {
                    builder.append(field.getType().getCanonicalName());
                }
                builder.append("): ");
                builder.append(field.get(object));
                builder.append('\n');
            }
        }
        System.out.println(builder.toString());
        return builder.toString();
    }
}
