package io.vepo.clone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

@DisplayName("Eager Clone")
public class EagerCloneTest {

    private static final CloneFactory eagerCloneFactory = CloneFactory.deep();

    @Test
    @DisplayName("Simple POJO")
    void pojoTest() {
        SimplePojo pojo = new SimplePojo(7, "username");
        SimplePojo clonedPojo = eagerCloneFactory.clone(pojo);
        assertTrue(pojo != clonedPojo);
        assertEquals(pojo, clonedPojo);
        
        Set<SimplePojo> set = new HashSet<SimplePojo>();
        set.add(pojo);
        set.add(clonedPojo);
        System.out.println(set.size());
    }

    @Test
    @DisplayName("Complex POJO")
    void complexTest() {
        ComplexPojo pojo = new ComplexPojo(13, new SimplePojo(7, "username"));
        ComplexPojo clonedPojo = eagerCloneFactory.clone(pojo);
        assertTrue(pojo != clonedPojo);
        assertEquals(pojo, clonedPojo);
        assertTrue(pojo.getPojo() != clonedPojo.getPojo());
        assertEquals(pojo.getPojo(), clonedPojo.getPojo());
    }

    @Test
    @DisplayName("Maps")
    void mapsTest() {
        Map<String, SimplePojo> pojoMap = new HashMap<String, SimplePojo>();
        pojoMap.put("user-1", new SimplePojo(7, "user-1"));
        pojoMap.put("user-2", new SimplePojo(13, "user-2"));

        Map<String, SimplePojo> clonedPojoMap = eagerCloneFactory.clone(pojoMap);
        assertTrue(pojoMap != clonedPojoMap);
        assertEquals(pojoMap, clonedPojoMap);

        assertTrue(pojoMap.get("user-1") != clonedPojoMap.get("user-1"));
        assertEquals(pojoMap.get("user-1"), clonedPojoMap.get("user-1"));

        assertTrue(pojoMap.get("user-2") != clonedPojoMap.get("user-2"));
        assertEquals(pojoMap.get("user-2"), clonedPojoMap.get("user-2"));
    }

    @Test
    @DisplayName("Lists")
    void listsTest() {
        List<SimplePojo> pojoList = new ArrayList<SimplePojo>();
        pojoList.add(new SimplePojo(7, "user-1"));
        pojoList.add(new SimplePojo(13, "user-2"));

        List<SimplePojo> clonedPojoList = eagerCloneFactory.clone(pojoList);
        assertTrue(pojoList != clonedPojoList);
        assertEquals(pojoList, clonedPojoList);

        assertTrue(pojoList.get(0) != clonedPojoList.get(0));
        assertEquals(pojoList.get(0), clonedPojoList.get(0));

        assertTrue(pojoList.get(1) != clonedPojoList.get(1));
        assertEquals(pojoList.get(1), clonedPojoList.get(1));
    }

    @Test
    @DisplayName("Sets")
    void setsTest() {
        Set<SimplePojo> pojoSet = new HashSet<SimplePojo>();
        pojoSet.add(new SimplePojo(7, "user-1"));
        pojoSet.add(new SimplePojo(13, "user-2"));

        Set<SimplePojo> clonedPojoSet = eagerCloneFactory.clone(pojoSet);
        assertTrue(pojoSet != clonedPojoSet);
        assertEquals(pojoSet, clonedPojoSet);

        pojoSet.forEach(value -> assertTrue(clonedPojoSet.contains(value)));
    }

    @Test
    @DisplayName("Array - Pojo")
    void arrayTest() {
        SimplePojo[] pojoArray = new SimplePojo[2];
        pojoArray[0] = new SimplePojo(7, "user-1");
        pojoArray[1] = new SimplePojo(13, "user-2");

        SimplePojo[] clonedPojoArray = eagerCloneFactory.clone(pojoArray);
        assertTrue(pojoArray != clonedPojoArray);
        assertTrue(Arrays.equals(pojoArray, clonedPojoArray));

        assertTrue(pojoArray[0] != clonedPojoArray[0]);
        assertEquals(pojoArray[0], clonedPojoArray[0]);

        assertTrue(pojoArray[1] != clonedPojoArray[1]);
        assertEquals(pojoArray[1], clonedPojoArray[1]);

        pojoArray[0] = null;
        assertNotNull(clonedPojoArray[0]);
    }

    @Test
    @DisplayName("Array - Pojo - Independent Array")
    void arrayIndependentTest() {
        SimplePojo[] pojoArray = new SimplePojo[1];
        pojoArray[0] = new SimplePojo(7, "user-1");

        SimplePojo[] clonedPojoArray = eagerCloneFactory.clone(pojoArray);
        assertTrue(pojoArray != clonedPojoArray);
        assertTrue(Arrays.equals(pojoArray, clonedPojoArray));

        pojoArray[0] = null;
        assertNotNull(clonedPojoArray[0]);
    }

}
