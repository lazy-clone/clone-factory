package io.vepo.clone;

import static io.vepo.clone.Data.givenRandomUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import io.vepo.clone.Data.User;

public class LazyClonerTest {

    @Test
    void cloneTest() {
        LazyCloner lazyCloner = new LazyCloner();

        User user = givenRandomUser();
        User clonedUser = lazyCloner.clone(user);
        assertFalse(user == clonedUser);
        assertEquals(user.getId(), clonedUser.getId());
    }

}
