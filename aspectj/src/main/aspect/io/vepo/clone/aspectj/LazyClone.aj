package io.vepo.clone.aspectj;

import io.vepo.clone.pojo.ClassInspector;

public aspect LazyClone {

	public pointcut fieldAccess(): get(!final !static * LazyCloneable+.*) && !within(LazyClone);

	private Object LazyCloneable.original = null;

	public void LazyCloneable.cloneOf(Object original) {
		if (this.getClass().equals(original.getClass())) {
			this.original = original;
			ClassInspector.getAllFields(original.getClass()).stream()
					.filter(field -> ClassInspector.isPrimitive(field) || ClassInspector.isImmutable(field))
					.forEach(field -> {
						try {
							field.setAccessible(true);
							field.set(this, field.get(original));
						} catch (IllegalAccessException e) {
							throw new RuntimeException(e);
						}
					});
		}
	}

	Object around(Object target) : target(target) && fieldAccess() {
		if (((LazyCloneable) target).getOriginal() == null) {
			return proceed(target);
		} else {
			LazyCloneable clone = (LazyCloneable) target;

			String fieldName = thisJoinPointStaticPart.getSignature().getName();
			Field field = getDeclaredField(clone, fieldName);
			Class<?> fieldClass = field.getType();
			field.setAccessible(true);
			if (isPrimitive(field) || isImmutable(field) || field.get(clone) != null) {
				returnproceed(clone);
			} else {
				Object fieldOriginalValue = field.get(clone.getOriginal());
				if (fieldOriginalValue == null) {
					return null;
				} else if (LazyCloneable.class.isAssignableFrom(fieldClass)) {
					LazyCloneable fieldClone = cloneCloneable((LazyCloneable) fieldOriginalValue);
					field.set(clone, fieldClone);
					return fieldClone;
				} else if (Collection.class.isAssignableFrom(fieldClass)) {
					Collection fieldClone = cloneCollection((Collection) fieldOriginalValue);
					field.set(clone, fieldClone);
					return fieldClone;

				}
			}
		}
	}
}