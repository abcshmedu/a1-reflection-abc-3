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
     * @throws ClassNotFoundException if canonical name is incorrect.
     * @throws IllegalAccessException if given class is no renderer and from class Class instead.
     * @throws InstantiationException if renderer class has no default constructor.
     * @throws NoSuchMethodException if method "render" was not found.
     * @throws InvocationTargetException if method "render" can not be invoked.
     */
    public String render() throws IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
            InstantiationException, InvocationTargetException {
        final Class toRender = this.object.getClass();
        final Field[] fields = toRender.getDeclaredFields();
        final Method[] methods = toRender.getDeclaredMethods();
        final StringBuilder builder = new StringBuilder();

        builder.append("Instance of ");
        builder.append(toRender.getCanonicalName());
        builder.append(":\n");

        renderFields(fields, builder);
        renderMethods(methods, builder);

        return builder.toString();
    }

    /**
     * Render fields of given class.
     *
     * @param fields  Fields to be rendered.
     * @param builder For string output.
     * @throws IllegalAccessException if field can't get read via reflection.
     * @throws ClassNotFoundException if canonical name is incorrect.
     * @throws IllegalAccessException if given class is no renderer and from class Class instead.
     * @throws InstantiationException if renderer class has no default constructor.
     * @throws NoSuchMethodException if method "render" was not found.
     * @throws InvocationTargetException if method "render" can not be invoked.
     */
    private void renderFields(final Field[] fields, final StringBuilder builder) throws IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException, InvocationTargetException {
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
     * @param canonicalName name of renderer for reflection.
     * @param field field to get information for.
     * @return string with information about field.
     * @throws ClassNotFoundException if canonical name is incorrect.
     * @throws IllegalAccessException if given class is no renderer and from class Class instead.
     * @throws InstantiationException if renderer class has no default constructor.
     * @throws NoSuchMethodException if method "render" was not found.
     * @throws InvocationTargetException if method "render" can not be invoked.
     */
    private String renderViaReflection(final String canonicalName, final Field field) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        final StringBuilder builder = new StringBuilder();
        final Class renderer = Class.forName(canonicalName);
        final Object rendererObject = renderer.newInstance();
        final Class clazz = field.getType();
        final Class[] parameterArray = new Class[1];
        parameterArray[0] = clazz;

        builder.append(field.getName());
        builder.append(' ');
        builder.append(renderer.getMethod("render", parameterArray)
                .invoke(rendererObject, field.get(this.object)));

        return builder.toString();
    }
}
