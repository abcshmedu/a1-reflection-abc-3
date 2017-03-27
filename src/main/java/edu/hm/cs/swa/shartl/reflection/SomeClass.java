package edu.hm.cs.swa.shartl.reflection;

import java.util.Date;

public class SomeClass {
    @RenderMe(with = "edu.hm.renderer.ArrayRenderer")
    int[] array = {1, 2, 3,};
    @RenderMe
    private int foo;
    @RenderMe
    private Date date = new Date(123456789);

    public SomeClass(int foo) {
        this.foo = foo;
    }
}