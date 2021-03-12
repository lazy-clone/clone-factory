package io.vepo.clone;

import java.util.Objects;

class KeyValue {
    private final Object key;
    private final Object value;

    KeyValue(Object key, Object value) {
        this.key = key;
        this.value = value;
    }

    Object key() {
        return key;
    }

    Object value() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
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
        KeyValue other = (KeyValue) obj;
        return Objects.equals(key, other.key) && Objects.equals(value, other.value);
    }

    @Override
    public String toString() {
        return String.format("KeyValue [key=%s, value=%s]", key, value);
    }

}
