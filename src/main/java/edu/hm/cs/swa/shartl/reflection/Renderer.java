package edu.hm.cs.swa.shartl.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        final Method[] methods = toRender.getDeclaredMethods();
        final StringBuilder builder = new StringBuilder();

        builder.append("Instance of ");
        builder.append(toRender.getCanonicalName());
        builder.append(":\n");

        renderFields(fields, builder);
        renderMethods(methods, builder);

        System.out.println(builder.toString());
        return builder.toString();
    }

    /**
     * Render fields of given class.
     *
     * @param fields  Fields to be rendered.
     * @param builder For string output.
     * @throws IllegalAccessException if field can't get read via reflection.
     */
    private void renderFields(final Field[] fields, final StringBuilder builder) throws IllegalAccessException {
        for (final Field field : fields) {
            if (field.isAnnotationPresent(RenderMe.class)) {
                final RenderMe annotation = field.getAnnotation(RenderMe.class);
                if (annotation.with().equals("")) {
                    field.setAccessible(true);
                    builder.append(field.getName());
                    builder.append(" (Type ");
                    if (field.getType().isPrimitive()) {
                        builder.append(field.getType().getSimpleName());
                    } else {
                        builder.append(field.getType().getCanonicalName());
                    }
                    builder.append("): ");
                    builder.append(field.get(object).toString());
                    builder.append('\n');
                } else {
                    builder.append(renderViaReflection(annotation.with(), field));
                }
            }
        }
    }

    /**
     * Render methods of given class.
     *
     * @param methods Methods to be rendered.
     * @param builder For string output.
     * @throws IllegalAccessException if field can't get read via reflection.
     */
    private void renderMethods(final Method[] methods, final StringBuilder builder) throws IllegalAccessException {
        for (final Method method : methods) {
            if (method.isAnnotationPresent(RenderMe.class)) {
                if (!method.getReturnType().toString().equals("void")) {
                    builder.append(method.getName());
                    builder.append(" returns: ");

                    builder.append(method.getReturnType());
                    builder.append('\n');
                }
            }
        }
    }

    /**
     * Renders via a custom renderer.
     *
     * @param canonicalName name of renderer for reflection.
     * @param field         field to get information for.
     * @return string with informations about field.
     */
    private String renderViaReflection(final String canonicalName, final Field field) {
        final StringBuilder builder = new StringBuilder();
        try {
            final Class renderer = Class.forName(canonicalName);
            final Object rendererObject = renderer.newInstance();
            final Class clazz = field.getType();
            final Class[] arr = new Class[1];
            arr[0] = clazz;

            builder.append(field.getName());
            builder.append(renderer.getMethod("render", arr).invoke(rendererObject, field.get(this.object)));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
