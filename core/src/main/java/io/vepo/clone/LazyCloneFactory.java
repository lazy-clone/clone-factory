package io.vepo.clone;

import static net.bytebuddy.description.modifier.Visibility.PRIVATE;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.vepo.clone.pojo.ClassInspector;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.Implementation.Composable;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.matcher.ElementMatchers;

class LazyCloneFactory extends CloneFactory {

    private class GetterLazyLoadInvocationHandler implements InvocationHandler {

        private GetterLazyLoadInvocationHandler() {
        }

        @Override
        public Object invoke(Object object, Method method, Object[] args) throws Throwable {
            ProxyableObject proxy = (ProxyableObject) object;
            Field field = ClassInspector.getFieldByGetter(method);
            Set<Field> clonedFields = proxy.$$getClonedFields();
            if (!clonedFields.contains(field)) {
                field.setAccessible(true);
                field.set(proxy, LazyCloneFactory.this.clone(field.get(proxy.$$getLazySource())));
                clonedFields.add(field);
            } else {
                field.setAccessible(true);
            }
            return field.get(proxy);
        }

    }

    private class LazyObjectProxyCloner<T> extends ObjectCloner<T> {

        private static final String FIELD_LAZY_CLONED_FIELDS = "$$_lazy_cloned_fields";
        private static final String FIELD_LAZY_SOURCE = "$$_lazy_source";
        private Constructor<? extends T> proxyConstructor;

        private LazyObjectProxyCloner(Class<T> objectClass) {
            try {
                Set<Field> copyableFields = new HashSet<Field>();
                Set<Field> clonableFields = new HashSet<Field>();
                ClassInspector.getAllFields(objectClass).forEach(field -> {
                    if (ClassInspector.isPrimitive(field.getType()) || ClassInspector.isImmutable(field.getType())) {
                        copyableFields.add(field);
                    } else {
                        clonableFields.add(field);
                    }
                });
                Class<? extends T> proxyClass = BYTE_BUDDY.subclass(objectClass)
                                                          .implement(ProxyableObject.class)
                                                          .defineField(FIELD_LAZY_SOURCE, objectClass, PRIVATE)
                                                          .defineField(FIELD_LAZY_CLONED_FIELDS, Set.class, PRIVATE)
                                                          .defineConstructor(Visibility.PUBLIC)
                                                          .withParameter(objectClass)
                                                          .intercept(createSetters(MethodCall.invoke(objectClass.getConstructor())
                                                                                             .andThen(FieldAccessor.ofField(FIELD_LAZY_SOURCE)
                                                                                                                   .setsArgumentAt(0))
                                                                                             .andThen(FieldAccessor.ofField(FIELD_LAZY_CLONED_FIELDS)
                                                                                                                   .setsValue(new HashSet<Field>())),
                                                                                   copyableFields))
                                                          .method(ElementMatchers.anyOf(clonableFields.stream()
                                                                                                      .map(ClassInspector::getGetter)
                                                                                                      .toArray(Method[]::new)))
                                                          .intercept(InvocationHandlerAdapter.of(getterHandler))
                                                          .method(ElementMatchers.anyOf(clonableFields.stream()
                                                                                                      .map(ClassInspector::getSetter)
                                                                                                      .toArray(Method[]::new)))
                                                          .intercept(InvocationHandlerAdapter.of(setterHandler))
                                                          .method(ElementMatchers.named("$$getLazySource"))
                                                          .intercept(FieldAccessor.ofField(FIELD_LAZY_SOURCE))
                                                          .method(ElementMatchers.named("$$getClonedFields"))
                                                          .intercept(FieldAccessor.ofField(FIELD_LAZY_CLONED_FIELDS))
                                                          .make()
                                                          .load(objectClass.getClassLoader(),
                                                                ClassLoadingStrategy.Default.INJECTION)
                                                          .getLoaded();

                proxyConstructor = proxyClass.getConstructor(objectClass);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Cannot create proxy class!", e);
            }
        }

        private Implementation createSetters(Composable constructor, Set<Field> fields) {
            Composable constructorExecution = constructor;
            for (Field f : fields) {
                constructorExecution =
                        constructorExecution.andThen(MethodCall.invoke(ClassInspector.getSetter(f))
                                                               .withMethodCall(MethodCall.invoke(ClassInspector.getGetter(f))
                                                                                         .onArgument(0)));
            }
            return constructorExecution;
        }

        @Override
        public T clone(T obj) {
            try {
                return proxyConstructor.newInstance(obj);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public interface ProxyableObject {
        public Set<Field> $$getClonedFields();

        public Object $$getLazySource();
    }

    private class SetterLazyLoadInvocationHandler implements InvocationHandler {

        private SetterLazyLoadInvocationHandler() {
        }

        @Override
        public Object invoke(Object object, Method method, Object[] args) throws Throwable {
            ProxyableObject proxy = (ProxyableObject) object;
            Field field = ClassInspector.getFieldBySetter(method);
            Set<Field> clonedFields = proxy.$$getClonedFields();
            if (!clonedFields.contains(field)) {
                field.setAccessible(true);
                field.set(proxy, LazyCloneFactory.this.clone(field.get(proxy.$$getLazySource())));
                clonedFields.add(field);
            } else {
                field.setAccessible(true);
            }
            field.set(proxy, args[0]);
            return null;
        }

    }

    private final ByteBuddy BYTE_BUDDY = new ByteBuddy();
    private Map<Class<?>, ObjectCloner<?>> cloners = new HashMap<>();

    private GetterLazyLoadInvocationHandler getterHandler = new GetterLazyLoadInvocationHandler();

    private SetterLazyLoadInvocationHandler setterHandler = new SetterLazyLoadInvocationHandler();

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T cloneObject(T obj) {
        return ((ObjectCloner<T>) cloners.computeIfAbsent(obj.getClass(),
                                                          clz -> new LazyObjectProxyCloner<T>((Class<T>) clz))).clone(obj);
    }

}
