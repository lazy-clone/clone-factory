package io.vepo.clone.samples;

import java.io.Serializable;
import java.util.Objects;

public class SimplePojo implements Serializable {
    private static final long serialVersionUID = 5504748915753773316L;
    private int id;
    private String username;

    public SimplePojo() {
    }

    public SimplePojo(int id, String username) {
        this.id = id;
        this.username = username;
    }

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
        SimplePojo other = (SimplePojo) obj;
        return id == other.id && Objects.equals(username, other.username);
    }

    @Override
    public String toString() {
        return String.format("Pojo [id=%s, username=%s]", id, username);
    }

}
