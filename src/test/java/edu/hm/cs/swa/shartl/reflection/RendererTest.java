package edu.hm.cs.swa.shartl.reflection;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class RendererTest {
    private SomeClass toRender;
    private Renderer renderer;

    @Before
    public void setUp() {
        toRender = new SomeClass(5);
        renderer = new Renderer(toRender);
    }

    @Test
    public void testRendering() throws Exception {
        final String expectedHeader = "Instance of edu.hm.cs.swa.shartl.reflection.SomeClass:";
        final String expectedInt = "foo (Type int): 5";
        final String expectedArray = "array (Type int[]): [1, 2, 3, ]";
        final String expectedDate = "date (Type java.util.Date): Fri Jan 02 11:17:36 CET 1970";

        final String result = renderer.render();

        assertThat(result, containsString(expectedHeader));
        assertThat(result, containsString(expectedInt));
        assertThat(result, containsString(expectedArray));
        assertThat(result, containsString(expectedDate));
    }

    @Test(expected = NoSuchMethodException.class)
    public void testRenderingFalseRenderer() throws Exception {
        final FalseRendererTestClass falsyClass = new FalseRendererTestClass();
        final Renderer underTest = new Renderer(falsyClass);

        underTest.render();
    }

    @Test
    public void testRenderingMethod() throws Exception {
        final RenderingMethods testClass = new RenderingMethods();
        final Renderer underTest = new Renderer(testClass);
        final String expectedHeader = "Instance of edu.hm.cs.swa.shartl.reflection.RendererTest.RenderingMethods:";
        final String expectedOutput = "testMethod returns: class java.lang.String";

        final String result = underTest.render();
        assertThat(result, containsString(expectedHeader));
        assertThat(result, containsString(expectedOutput));
    }

    class FalseRendererTestClass {
        @RenderMe(with = "edu.hm.cs.swa.shartl.reflection.ArrayRenderer")
        double[] array = {1.0, 2.2, 3.3,};
    }

    class RenderingMethods {
        @RenderMe
        public String testMethod() {
            return "Hello World";
        }

        @RenderMe
        public void notToRender() {
        }
    }
}