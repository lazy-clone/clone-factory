package io.vepo.clone;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.vepo.clone.cloners.Cloner;

class LazyCloneFactory extends CloneFactory {

    private class LazyReflectionCloner<T> extends Cloner<T> {
        private Constructor<T> constructor;
        private Set<Field> fields;

        @SuppressWarnings("unchecked")
        private LazyReflectionCloner(Class<T> objClass) {
            constructor = (Constructor<T>) Stream.of(objClass.getConstructors())
                                                 .filter(c -> c.getParameterCount() == 0).findFirst()
                                                 .orElseThrow(() -> new UnsupportedOperationException("No default constructor"));

            fields = Stream.of(objClass.getDeclaredFields())
                           .filter(field -> !Modifier.isFinal(field.getModifiers()))
                           .peek(f -> f.setAccessible(true))
                           .collect(Collectors.toSet());
        }

        @Override
        public T clone(T obj) {
            T dst;
            try {
                dst = constructor.newInstance();
                fields.forEach(field -> {
                    try {
                        field.set(dst, LazyCloneFactory.this.clone(field.get(obj)));
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
                return dst;
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private class ListCloner extends Cloner<List<?>> {

        @Override
        public List<?> clone(List<?> obj) {
            return obj.stream()
                      .map(item -> LazyCloneFactory.this.clone(item))
                      .collect(toList());
        }

    }

    private class MapCloner extends Cloner<Map<?, ?>> {

        @Override
        public Map<?, ?> clone(Map<?, ?> obj) {

            return obj.entrySet()
                      .stream()
                      .map(entry -> new KeyValue(LazyCloneFactory.this.clone(entry.getKey()),
                                                 LazyCloneFactory.this.clone(entry.getValue())))
                      .collect(toMap(KeyValue::key, KeyValue::value));
        }

    }

    private class SetCloner extends Cloner<Set<?>> {

        @Override
        public Set<?> clone(Set<?> obj) {
            return obj.stream()
                      .map(item -> LazyCloneFactory.this.clone(item))
                      .collect(toSet());
        }

    }

    private static final Package LANG_PACKAGE = String.class.getPackage();

    private Map<Class<?>, Cloner<?>> cloners = new HashMap<>();
    private MapCloner mapCloner = new MapCloner();
    private ListCloner listCloner = new ListCloner();
    private SetCloner setCloner = new SetCloner();

    @Override
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
        } else if (obj.getClass().getPackage() == LANG_PACKAGE) {
            return obj;
        } else {
            return ((Cloner<T>) cloners.computeIfAbsent(obj.getClass(),
                                                        clz -> new LazyReflectionCloner<T>((Class<T>) clz))).clone(obj);
        }

    }

    @Override
    public <T> T[] clone(T[] obj) {
        return cloneArray(obj);
    }

    @SuppressWarnings("unchecked")
    private <T> T cloneArray(T obj) {
        int length = Array.getLength(obj);
        if (obj.getClass().getComponentType().getPackage() == LANG_PACKAGE) {
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

}
