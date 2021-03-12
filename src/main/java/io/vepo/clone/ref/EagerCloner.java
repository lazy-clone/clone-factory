package io.vepo.clone.ref;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EagerCloner {

    private static final Package LANG_PACKAGE = String.class.getPackage();

    @SuppressWarnings({
        "unchecked",
        "rawtypes" })
    private <T> void cloneArray(Class<T> type, Supplier<?> getter, Consumer<T> setter) throws IllegalAccessException {
        if (type == byte.class) {
            setter.accept((T) ((byte[]) getter.get()).clone());
        } else if (type == short.class) {
            setter.accept((T) ((short[]) getter.get()).clone());
        } else if (type == int.class) {
            setter.accept((T) ((int[]) getter.get()).clone());
        } else if (type == long.class) {
            setter.accept((T) ((long[]) getter.get()).clone());
        } else if (type == float.class) {
            setter.accept((T) ((float[]) getter.get()).clone());
        } else if (type == double.class) {
            setter.accept((T) ((double[]) getter.get()).clone());
        } else if (type == char.class) {
            setter.accept((T) ((char[]) getter.get()).clone());
        } else if (type == boolean.class) {
            setter.accept((T) ((boolean[]) getter.get()).clone());
        } else {
            setter.accept((T) Stream.of((Object[]) getter.get())
                                    .map(value -> {
                                        if (value instanceof Collection) {
                                            return cloneCollection(value.getClass(), () -> value);
                                        } else if (value instanceof Map) {
                                            return ((Set<Entry>) ((Map) value).entrySet()).stream()
                                                                                          .collect(toMap(Entry::getKey,
                                                                                                         Entry::getValue));
                                        } else {
                                            return value;
                                        }
                                    }).toArray());
        }
    }

    @SuppressWarnings({
        "rawtypes",
        "unchecked" })
    private <T extends Collection<?>> T cloneCollection(Class<?> type, Supplier<?> getter) {
        if (isList(type)) {
            return (T) ((List) getter.get()).stream().collect(toList());
        } else if (isSet(type)) {
            return (T) ((Set) getter.get()).stream().collect(Collectors.toSet());
        } else {
            throw new IllegalStateException();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T deepClone(T obj) {
        try {
            Class<T> objClass = (Class<T>) obj.getClass();
            T clone = objClass.newInstance();
            Stream.of(objClass.getDeclaredFields()).forEach(field -> deepClone(obj, clone, field));
            return clone;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({
        "rawtypes",
        "unchecked" })
    private <T> void deepClone(T src, T dst, Field field) {
        try {
            Class<?> type = field.getType();
            field.setAccessible(true);
            if (isCollection(type)) {
                field.set(dst, cloneCollection(type, () -> {
                    try {
                        return field.get(src);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }));
            } else if (isMap(type)) {
                field.set(dst, ((Set<Entry>) (((Map) field.get(src)).entrySet())).stream()
                                                                                 .collect(toMap(Entry::getKey,
                                                                                                Entry::getValue)));
            } else

            if (type.isArray()) {
                cloneArray(type.getComponentType(), () -> {
                    try {
                        return field.get(src);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }, (value) -> {
                    try {
                        field.set(dst, value);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else if (type.isPrimitive() || type.getPackage() == LANG_PACKAGE) {
                field.set(dst, field.get(src));
            } else {
                field.set(dst, deepClone(field.get(src)));
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isCollection(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }

    private boolean isList(Class<?> type) {
        return List.class.isAssignableFrom(type);
    }

    private boolean isMap(Class<?> type) {
        return Map.class.isAssignableFrom(type);
    }

    private boolean isSet(Class<?> type) {
        return Set.class.isAssignableFrom(type);
    }

    @SuppressWarnings("unchecked")
    public <T> T shallowClone(T obj) {
        try {
            Class<T> objClass = (Class<T>) obj.getClass();
            T clone = objClass.newInstance();
            Stream.of(objClass.getDeclaredFields()).forEach(field -> shallowClone(obj, clone, field));
            return clone;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void shallowClone(T src, T dst, Field field) {
        try {
            field.setAccessible(true);
            field.set(dst, field.get(src));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}