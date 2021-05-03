package io.vepo.clone;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.vepo.clone.pojo.ClassInspector;

/**
 * <p>
 * This class provide access to all possible deep cloning strategies. There are
 * two cloning approach: shallow and deep. Shallow means that all references
 * from the cloned class will not be cloned. The Deep approach means that all
 * references will be cloned too.
 * </p>
 * 
 * <p>
 * The available strategies are:
 * <ul>
 * <li><b>Eager</b>: All references by the cloned object will be cloned during
 * the clone operation.</li>
 * <li><b>Lazy</b>: The object will only be cloned when some change is
 * required.</li>
 * </ul>
 * </p>
 * 
 * @author vepo
 *
 */
public abstract class CloneFactory {
    private class KeyValue {
        private final Object key;
        private final Object value;

        private KeyValue(Object key, Object value) {
            this.key = key;
            this.value = value;
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
        public int hashCode() {
            return Objects.hash(key, value);
        }

        private Object key() {
            return key;
        }

        @Override
        public String toString() {
            return String.format("KeyValue [key=%s, value=%s]", key, value);
        }

        private Object value() {
            return value;
        }

    }

    private class ListCloner extends ObjectCloner<List<?>> {

        @Override
        public List<?> clone(List<?> obj) {
            return obj.stream()
                      .map(item -> CloneFactory.this.clone(item))
                      .collect(toList());
        }

    }

    private class MapCloner extends ObjectCloner<Map<?, ?>> {

        @Override
        public Map<?, ?> clone(Map<?, ?> obj) {

            return obj.entrySet()
                      .stream()
                      .map(entry -> new KeyValue(CloneFactory.this.clone(entry.getKey()),
                                                 CloneFactory.this.clone(entry.getValue())))
                      .collect(toMap(KeyValue::key, KeyValue::value));
        }

    }

    protected abstract class ObjectCloner<T> {

        abstract T clone(T obj);
    }

    private class SetCloner extends ObjectCloner<Set<?>> {

        @Override
        public Set<?> clone(Set<?> obj) {
            return obj.stream()
                      .map(item -> CloneFactory.this.clone(item))
                      .collect(toSet());
        }

    }

    public static CloneFactory deep() {
        return new DeepCloneFactory();
    }

    public static CloneFactory lazy() {
        return new LazyCloneFactory();
    }

    private ListCloner listCloner = new ListCloner();
    private MapCloner mapCloner = new MapCloner();
    private SetCloner setCloner = new SetCloner();

    CloneFactory() {
    }

    @SuppressWarnings("unchecked")
    public <T> T clone(T obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Map) {
            return (T) mapCloner.clone((Map<?, ?>) obj);
        } else if (obj instanceof List) {
            return (T) listCloner.clone((List<?>) obj);
        } else if (obj instanceof Set) {
            return (T) setCloner.clone((Set<?>) obj);
        } else if (obj.getClass().isArray()) {
            return cloneArray(obj);
        } else if (ClassInspector.isPrimitive(obj.getClass())) {
            return obj;
        } else {
            return cloneObject(obj);
        }

    }

    /**
     * <p>
     * Deep clone an array. This clone method will clone all objects referred by
     * this array.
     * </p>
     * <p>
     * <code>
     * Object[] objs = new Object[]{ new Object(), new Object()}; <br/>
     * Object[] deepClonedObjs = CloneFactory.eager().clone(objs); <br/>
     * Object[] shallowClonedObjs = objs.clone(); <br/><br/>
     * 
     * // The arrays will always be different <br/>
     * assertTrue(objs != deepClonedObjs); <br/>
     * assertTrue(objs != shallowClonedObjs); <br/><br/>
     * // The array clone method does not clone objects inside the array. <br/>
     * assertTrue(objs[0] != deepClonedObjs[0]); <br/>
     * assertTrue(objs[0] == shallowClonedObjs[0]);
     * </code>
     * </p>
     * 
     * @param <T> The element class. If it is a primitive type, there is no need for
     *            cloning. Otherwise it will clone all the object graph.
     * @param obj This value will be completely independent of the returned.
     * @return a new instance of the object. Any change on this instance will not
     *         affect the original object.
     */
    public <T> T[] clone(T[] obj) {
        return cloneArray(obj);
    }

    @SuppressWarnings("unchecked")
    private <T> T cloneArray(T obj) {
        int length = Array.getLength(obj);
        if (ClassInspector.isPrimitive(obj.getClass().getComponentType())) {
            Class<?> arrayType = obj.getClass().getComponentType();
            T copy = (T) Array.newInstance(arrayType, length);
            System.arraycopy(obj, 0, copy, 0, length);
            return copy;
        } else {
            Class<?> arrayType = obj.getClass().getComponentType();
            T copy = (T) Array.newInstance(arrayType, length);
            for (int i = 0; i < length; ++i) {
                Array.set(copy, i, clone(Array.get(obj, i)));
            }
            return copy;
        }
    }

    protected abstract <T> T cloneObject(T obj);
}
