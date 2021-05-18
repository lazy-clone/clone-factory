package io.vepo.clone.pojo;

import static java.util.stream.Collectors.toSet;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Stream;

public class ClassInspector {
	public static Method getSetter(Field field) {
		return Stream.of(field.getDeclaringClass().getDeclaredMethods())
				.filter(method -> !Modifier.isStatic(method.getModifiers()))
				.filter(method -> method.getParameterCount() == 1 && method.getParameterTypes()[0] == field.getType())
				.filter(method -> method.getName().equals(field.getName())
						|| method.getName().equals("set" + capitalize(field.getName())))
				.findFirst().orElseThrow(() -> new UnsupportedOperationException("No setter for " + field.getName()));
	}

	public static Method getGetter(Field field) {
		return Stream.of(field.getDeclaringClass().getDeclaredMethods())
				.filter(method -> !Modifier.isStatic(method.getModifiers()))
				.filter(method -> method.getParameterCount() == 0 && method.getReturnType() == field.getType())
				.filter(method -> method.getName().equals(field.getName())
						|| method.getName().equals("get" + capitalize(field.getName())))
				.findFirst().orElseThrow(() -> new UnsupportedOperationException("No getter for " + field.getName()));
	}

	public static Field getField(Object obj, String fieldName) {
		try {
			return obj.getClass().getDeclaredField(fieldName);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	private static String capitalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static Field getFieldBySetter(Method method) {
		if (method.getParameterCount() != 1) {
			throw new IllegalStateException("Not a setter: " + method);
		}
		return Stream.of(method.getDeclaringClass().getDeclaredFields())
				.filter(field -> field.getType() == method.getParameterTypes()[0])
				.filter(field -> method.getName().equals(field.getName())
						|| method.getName().equals("set" + capitalize(field.getName())))
				.findFirst().orElseThrow(() -> new UnsupportedOperationException("No field for " + method.getName()));
	}

	public static Field getFieldByGetter(Method method) {
		if (method.getParameterCount() != 0) {
			throw new IllegalStateException("Not a getter: " + method);
		}
		return Stream.of(method.getDeclaringClass().getDeclaredFields())
				.filter(field -> field.getType() == method.getReturnType())
				.filter(field -> method.getName().equals(field.getName())
						|| method.getName().equals("get" + capitalize(field.getName())))
				.findFirst().orElseThrow(() -> new UnsupportedOperationException("No field for " + method.getName()));
	}

	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> getDefaultContructor(Class<T> objectClass) {
		return (Constructor<T>) Stream.of(objectClass.getConstructors()).filter(c -> c.getParameterCount() == 0)
				.findFirst().orElseThrow(() -> new UnsupportedOperationException("No default constructor"));
	}

	private static Stream<Field> allFields(Class<?> objClass) {
		if (objClass.isPrimitive() || objClass == Object.class) {
			return Stream.empty();
		} else {
			return Stream.concat(Stream.of(objClass.getDeclaredFields()), allFields(objClass.getSuperclass()));
		}
	}

	public static Set<Field> getAllFields(Class<?> objClass) {
		return allFields(objClass)
				.filter(field -> !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()))
				.peek(f -> f.setAccessible(true)).collect(toSet());
	}

	private static final Package LANG_PACKAGE = String.class.getPackage();

	public static boolean isPrimitive(Class<?> type) {
		return type.isPrimitive() || type.getPackage() == LANG_PACKAGE;
	}

	public static boolean isPrimitive(Field field) {
		return isPrimitive(field.getType());
	}

	public static boolean isImmutable(Class<?> type) {
		if (type == Object.class) {
			return true;
		} else {
			return allFields(type).filter(field -> !Modifier.isStatic(field.getModifiers())).allMatch(
					field -> !Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())
							&& (isPrimitive(field.getType()) || isImmutable(field.getType())));
		}
	}

	public static boolean isImmutable(Field field) {
		return Modifier.isFinal(field.getModifiers()) && isImmutable(field.getType());
	}

}
