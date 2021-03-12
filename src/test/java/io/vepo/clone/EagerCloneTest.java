package io.vepo.clone;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Eager Clone")
public class EagerCloneTest {
    public static class Pojo {
        private int id;
        private String username;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, username);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Pojo other = (Pojo) obj;
            return id == other.id && Objects.equals(username, other.username);
        }

        @Override
        public String toString() {
            return String.format("Pojo [id=%s, username=%s]", id, username);
        }
    }

    @Test
    @DisplayName("POJO")
    void pojoTest() {
        Pojo pojo = new Pojo();
        pojo.setId(7);
        pojo.setUsername("username");
        Pojo clonnedPojo = CloneFactory.eager().clone(pojo);
        assertTrue(pojo != clonnedPojo);
    }
}
