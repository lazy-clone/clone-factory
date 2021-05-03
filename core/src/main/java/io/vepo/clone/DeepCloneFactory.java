package io.vepo.clone;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.vepo.clone.pojo.ClassInspector;

class DeepCloneFactory extends CloneFactory {
    private class DeepObjectReflectionCloner<T> extends ObjectCloner<T> {
        private Constructor<T> constructor;
        private Set<Field> fields;

        private DeepObjectReflectionCloner(Class<T> objClass) {
            constructor = ClassInspector.getDefaultContructor(objClass);
            fields = ClassInspector.getAllFields(objClass);
        }

        @Override
        public T clone(T obj) {
            T dst;
            try {
                dst = constructor.newInstance();
                fields.forEach(field -> {
                    try {
                        field.set(dst, DeepCloneFactory.this.clone(field.get(obj)));
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

    private Map<Class<?>, ObjectCloner<?>> cloners = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T cloneObject(T obj) {
        return ((ObjectCloner<T>) cloners.computeIfAbsent(obj.getClass(),
                                                          clz -> new DeepObjectReflectionCloner<T>((Class<T>) clz))).clone(obj);
    }

}
