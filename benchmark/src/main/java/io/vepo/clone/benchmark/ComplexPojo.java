package io.vepo.clone.benchmark;

import java.io.Serializable;
import java.util.Objects;


public class ComplexPojo implements Serializable {
    private static final long serialVersionUID = -7024076305288115562L;
    private int id;
    private SimplePojo pojo;
    private int[] intValues;

    public ComplexPojo() {
    }

    public ComplexPojo(int id, SimplePojo pojo) {
        this(id, pojo, null);
    }

    public ComplexPojo(int id, SimplePojo pojo, int[] intValues) {
        this.id = id;
        this.pojo = pojo;
        this.intValues = intValues;
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

    public int[] getIntValues() {
        return intValues;
    }

    public void setIntValues(int[] intValues) {
        this.intValues = intValues;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pojo, intValues);
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
        return id == other.id && Objects.equals(pojo, other.pojo) && Objects.equals(intValues, other.intValues);
    }

    @Override
    public String toString() {
        return String.format("ComplexPojo [id=%s, pojo=%s, intValues=%s]", id, pojo, intValues);
    }

}
