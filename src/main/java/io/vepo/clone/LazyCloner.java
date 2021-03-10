package io.vepo.clone;

public class LazyCloner {

    @SuppressWarnings("unchecked")
    public <T> T clone(T obj) {
        try {
            T clone = (T) obj.getClass().newInstance();
            return clone;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}