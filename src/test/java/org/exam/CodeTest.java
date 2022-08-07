package org.exam;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CodeTest {

    @Test
    public void sayHelloTest(){
        Code code = new Code();
        assertEquals("Hello there", code.sayHello());
    }
}
