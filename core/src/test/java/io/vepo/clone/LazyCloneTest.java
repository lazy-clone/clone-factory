package io.vepo.clone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
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

    private static final CloneFactory lazyCloneFactory = CloneFactory.lazy();

    @Test
    @DisplayName("Simple POJO")
    void pojoTest() {
        SimplePojo pojo = new SimplePojo(7, "username");
        SimplePojo clonedPojo = lazyCloneFactory.clone(pojo);
        assertTrue(pojo != clonedPojo);
        assertEquals(pojo.getId(), clonedPojo.getId());
        assertEquals(pojo.getUsername(), clonedPojo.getUsername());
//        assertEquals(pojo, clonedPojo);
        clonedPojo.setUsername("my-new-username");
        assertNotEquals("my-new-username", pojo.getUsername());
        
        Set<SimplePojo> set = new HashSet<SimplePojo>();
        set.add(pojo);
        set.add(clonedPojo);
        System.out.println(set.size());
    }

    @Test
    @DisplayName("Complex POJO")
    void complexTest() {
        ComplexPojo pojo = new ComplexPojo(13, new SimplePojo(7, "username"));
        ComplexPojo clonedPojo = lazyCloneFactory.clone(pojo);
        assertTrue(pojo != clonedPojo);
        assertTrue(pojo.getPojo() != clonedPojo.getPojo());

    }

    @Test
    @DisplayName("Complex POJO - Set Again")
    void complexSetAgainTest() {
        ComplexPojo pojo = new ComplexPojo(13, new SimplePojo(7, "username"));
        ComplexPojo clonedPojo = lazyCloneFactory.clone(pojo);
        assertTrue(pojo != clonedPojo);
        clonedPojo.setPojo(pojo.getPojo());
        assertTrue(pojo.getPojo() == clonedPojo.getPojo());

    }

    @Test
    @DisplayName("Maps")
    void mapsTest() {
        Map<String, SimplePojo> pojoMap = new HashMap<String, SimplePojo>();
        pojoMap.put("user-1", new SimplePojo(7, "user-1"));
        pojoMap.put("user-2", new SimplePojo(13, "user-2"));

        Map<String, SimplePojo> clonedPojoMap = lazyCloneFactory.clone(pojoMap);
        assertTrue(pojoMap != clonedPojoMap);

        assertTrue(pojoMap.get("user-1") != clonedPojoMap.get("user-1"));
        assertEquals(pojoMap.get("user-1").getId(), clonedPojoMap.get("user-1").getId());
        pojoMap.get("user-1").setId(8);
        assertNotEquals(pojoMap.get("user-1").getId(), clonedPojoMap.get("user-1").getId());
        assertTrue(pojoMap.get("user-2") != clonedPojoMap.get("user-2"));
    }

    @Test
    @DisplayName("Lists")
    void listsTest() {
        List<SimplePojo> pojoList = new ArrayList<SimplePojo>();
        pojoList.add(new SimplePojo(7, "user-1"));
        pojoList.add(new SimplePojo(13, "user-2"));

        List<SimplePojo> clonedPojoList = lazyCloneFactory.clone(pojoList);
        assertTrue(pojoList != clonedPojoList);

        assertTrue(pojoList.get(0) != clonedPojoList.get(0));
        assertEquals(pojoList.get(0).getId(), clonedPojoList.get(0).getId());
        pojoList.get(0).setId(8);
        assertNotEquals(pojoList.get(0).getId(), clonedPojoList.get(0).getId());
        assertTrue(pojoList.get(1) != clonedPojoList.get(1));
    }

    @Test
    @DisplayName("Sets")
    void setsTest() {
        Set<SimplePojo> pojoSet = new HashSet<SimplePojo>();
        pojoSet.add(new SimplePojo(7, "user-1"));
        pojoSet.add(new SimplePojo(13, "user-2"));

        Set<SimplePojo> clonedPojoSet = lazyCloneFactory.clone(pojoSet);
        assertTrue(pojoSet != clonedPojoSet);

        // TODO pojoSet.forEach(value -> assertTrue(clonedPojoSet.contains(value)));
    }

    @Test
    @DisplayName("Array")
    void arryTest() {
        SimplePojo[] pojoArray = new SimplePojo[2];
        pojoArray[0] = new SimplePojo(7, "user-1");
        pojoArray[1] = new SimplePojo(13, "user-2");

        SimplePojo[] clonedPojoArray = lazyCloneFactory.clone(pojoArray);
        assertTrue(pojoArray != clonedPojoArray);
        // TODO assertTrue(Arrays.equals(pojoArray, clonedPojoArray));

        assertTrue(pojoArray[0] != clonedPojoArray[0]);
        // TODO assertEquals(pojoArray[0], clonedPojoArray[0]);

        assertTrue(pojoArray[1] != clonedPojoArray[1]);
        // TODO assertEquals(pojoArray[1], clonedPojoArray[1]);
    }
}
