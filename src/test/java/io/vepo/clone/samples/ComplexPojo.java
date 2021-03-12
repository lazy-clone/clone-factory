package io.vepo.clone.samples;

import java.util.Objects;

public class ComplexPojo {

    private int id;
    private SimplePojo pojo;

    public ComplexPojo() {
    }

    public ComplexPojo(int id, SimplePojo pojo) {
        this.id = id;
        this.pojo = pojo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SimplePojo getPojo() {
        return pojo;
    }

    public void setPojo(SimplePojo pojo) {
        this.pojo = pojo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pojo);
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
        ComplexPojo other = (ComplexPojo) obj;
        return id == other.id && Objects.equals(pojo, other.pojo);
    }

    @Override
    public String toString() {
        return String.format("ComplexPojo [id=%s, pojo=%s]", id, pojo);
    }

}
