package io.vepo.clone.pojo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.vepo.clone.samples.SimplePojo;
import io.vepo.clone.samples.SimpleRecord;

class MethodInspectorTest {

    @Test
    void getSetterTest() throws NoSuchFieldException, SecurityException, NoSuchMethodException {
        assertEquals(SimplePojo.class.getMethod("setId", int.class),
                     ClassInspector.getSetter(SimplePojo.class.getDeclaredField("id")));
        assertEquals(SimpleRecord.class.getMethod("id", int.class),
                     ClassInspector.getSetter(SimpleRecord.class.getDeclaredField("id")));
    }

    @Test
    void getFieldBySetterTest() throws NoSuchFieldException, SecurityException, NoSuchMethodException {
        assertEquals(SimplePojo.class.getDeclaredField("id"),
                     ClassInspector.getFieldBySetter(SimplePojo.class.getMethod("setId", int.class)));
        assertEquals(SimpleRecord.class.getDeclaredField("id"),
                     ClassInspector.getFieldBySetter(SimpleRecord.class.getMethod("id", int.class)));
    }

    @Test
    void getGetterTest() throws NoSuchMethodException, SecurityException, NoSuchFieldException {
        assertEquals(SimplePojo.class.getMethod("getId"),
                     ClassInspector.getGetter(SimplePojo.class.getDeclaredField("id")));
        assertEquals(SimpleRecord.class.getMethod("id"),
                     ClassInspector.getGetter(SimpleRecord.class.getDeclaredField("id")));
    }

    @Test
    void getFieldByGetterTest() throws NoSuchMethodException, SecurityException, NoSuchFieldException {
        assertEquals(SimplePojo.class.getDeclaredField("id"),
                     ClassInspector.getFieldByGetter(SimplePojo.class.getMethod("getId")));
        assertEquals(SimpleRecord.class.getDeclaredField("id"),
                     ClassInspector.getFieldByGetter(SimpleRecord.class.getMethod("id")));
    }

}
