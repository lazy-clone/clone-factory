package io.vepo.clone.aspectj;

import java.lang.reflect.Field;
import java.util.Collection;

import io.vepo.clone.pojo.ClassInspector;

public aspect LazyClone {

	public pointcut fieldAccess(): get(!final !static * *);

	Object around(): target(target) && fieldAccess() {
		System.out.println("Getting target!");
		return proceed();
	}

//	private Object LazyCloneable.original = null;
//
//	private Object LazyCloneable.getOriginal() {
//		return this.original;
//	}

//	public void LazyCloneable.cloneOf(Object original) {
//		if (this.getClass().equals(original.getClass())) {
//			this.original = original;
//			ClassInspector.getAllFields(original.getClass()).stream()
//					.filter(field -> ClassInspector.isPrimitive(field) || ClassInspector.isImmutable(field))
//					.forEach(field -> {
//						try {
//							field.setAccessible(true);
//							field.set(this, field.get(original));
//						} catch (IllegalAccessException e) {
//							throw new RuntimeException(e);
//						}
//					});
//		}
//	}

//	Object around(Object target) : target(target) && fieldAccess() {
//		return proceed(target);
//	}
}