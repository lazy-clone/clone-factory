package io.vepo.clone;

import static io.vepo.clone.Data.givenRandomUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import io.vepo.clone.Data.User;

public class EagerClonerTest {

    @Test
    void shallowCloneTest() {
        EagerCloner eagerCloner = new EagerCloner();

        User user = givenRandomUser();
        User clonedUser = eagerCloner.shallowClone(user);
        assertFalse(user == clonedUser);
        assertEquals(user, clonedUser);
    }

    @Test
    void deepCloneTest() {
        EagerCloner eagerCloner = new EagerCloner();

        User user = givenRandomUser();
        User clonedUser = eagerCloner.deepClone(user);
        assertFalse(user == clonedUser);
        assertEquals(user, clonedUser);
        assertFalse(user.getRoles() == clonedUser.getRoles());
    }

}
