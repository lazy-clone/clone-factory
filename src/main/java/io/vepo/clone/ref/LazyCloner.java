package io.vepo.clone.ref;

import static net.bytebuddy.description.modifier.Visibility.PRIVATE;

import java.lang.reflect.InvocationHandler;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType.Loaded;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

public class LazyCloner {

    private static final ByteBuddy BYTE_BUDDY = new ByteBuddy();

    @SuppressWarnings("unchecked")
    public <T> T clone(T obj) {
        try {
            InvocationHandler handler = (proxy, method, args) -> {
                System.out.println(proxy.getClass());
                System.out.println("Calling: " + method.getName() + " with args: " + args);
                return method.invoke(obj, args);
            };

            Loaded<? extends Object> x = BYTE_BUDDY.subclass(obj.getClass())
                                                   .defineField("source", obj.getClass(), PRIVATE)
                                                   .method(ElementMatchers.any())
                                                   .intercept(InvocationHandlerAdapter.of(handler))
                                                   .make()
                                                   .load(obj.getClass().getClassLoader());
            return (T) x.getLoaded().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}