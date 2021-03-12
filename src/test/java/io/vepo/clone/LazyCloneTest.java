package io.vepo.clone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.vepo.clone.samples.ComplexPojo;
import io.vepo.clone.samples.SimplePojo;

@DisplayName("Lazy Clone")
class LazyCloneTest {

    @Test
    @DisplayName("Simple POJO")
    void pojoTest() {
        SimplePojo pojo = new SimplePojo(7, "username");
        SimplePojo clonnedPojo = CloneFactory.lazy().clone(pojo);
        assertTrue(pojo != clonnedPojo);
        assertEquals(pojo, clonnedPojo);
    }

    @Test
    @DisplayName("Complex POJO")
    void complexTest() {
        ComplexPojo pojo = new ComplexPojo(13, new SimplePojo(7, "username"));
        ComplexPojo clonnedPojo = CloneFactory.lazy().clone(pojo);
        assertTrue(pojo != clonnedPojo);
        assertEquals(pojo, clonnedPojo);
        assertTrue(pojo.getPojo() != clonnedPojo.getPojo());
        assertEquals(pojo.getPojo(), clonnedPojo.getPojo());
    }

    @Test
    @DisplayName("Maps")
    void mapsTest() {
        Map<String, SimplePojo> pojoMap = new HashMap<String, SimplePojo>();
        pojoMap.put("user-1", new SimplePojo(7, "user-1"));
        pojoMap.put("user-2", new SimplePojo(13, "user-2"));

        Map<String, SimplePojo> clonnedPojoMap = CloneFactory.lazy().clone(pojoMap);
        assertTrue(pojoMap != clonnedPojoMap);
        assertEquals(pojoMap, clonnedPojoMap);

        assertTrue(pojoMap.get("user-1") != clonnedPojoMap.get("user-1"));
        assertEquals(pojoMap.get("user-1"), clonnedPojoMap.get("user-1"));

        assertTrue(pojoMap.get("user-2") != clonnedPojoMap.get("user-2"));
        assertEquals(pojoMap.get("user-2"), clonnedPojoMap.get("user-2"));
    }

    @Test
    @DisplayName("Lists")
    void listsTest() {
        List<SimplePojo> pojoList = new ArrayList<SimplePojo>();
        pojoList.add(new SimplePojo(7, "user-1"));
        pojoList.add(new SimplePojo(13, "user-2"));

        List<SimplePojo> clonnedPojoList = CloneFactory.lazy().clone(pojoList);
        assertTrue(pojoList != clonnedPojoList);
        assertEquals(pojoList, clonnedPojoList);

        assertTrue(pojoList.get(0) != clonnedPojoList.get(0));
        assertEquals(pojoList.get(0), clonnedPojoList.get(0));

        assertTrue(pojoList.get(1) != clonnedPojoList.get(1));
        assertEquals(pojoList.get(1), clonnedPojoList.get(1));
    }

    @Test
    @DisplayName("Sets")
    void setsTest() {
        Set<SimplePojo> pojoSet = new HashSet<SimplePojo>();
        pojoSet.add(new SimplePojo(7, "user-1"));
        pojoSet.add(new SimplePojo(13, "user-2"));

        Set<SimplePojo> clonnedPojoSet = CloneFactory.lazy().clone(pojoSet);
        assertTrue(pojoSet != clonnedPojoSet);
        assertEquals(pojoSet, clonnedPojoSet);

        pojoSet.forEach(value -> assertTrue(clonnedPojoSet.contains(value)));
    }

    @Test
    @DisplayName("Array")
    void arryTest() {
        SimplePojo[] pojoArray = new SimplePojo[2];
        pojoArray[0] = new SimplePojo(7, "user-1");
        pojoArray[1] = new SimplePojo(13, "user-2");

        SimplePojo[] clonnedPojoArray = CloneFactory.lazy().clone(pojoArray);
        assertTrue(pojoArray != clonnedPojoArray);
        assertTrue(Arrays.equals(pojoArray, clonnedPojoArray));

        assertTrue(pojoArray[0] != clonnedPojoArray[0]);
        assertEquals(pojoArray[0], clonnedPojoArray[0]);

        assertTrue(pojoArray[1] != clonnedPojoArray[1]);
        assertEquals(pojoArray[1], clonnedPojoArray[1]);
    }
}
