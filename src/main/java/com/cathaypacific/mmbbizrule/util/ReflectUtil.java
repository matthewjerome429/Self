package com.cathaypacific.mmbbizrule.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import com.cathaypacific.mbcommon.loging.LogAgent;

public class ReflectUtil {

	private static final LogAgent LOGGER = LogAgent.getLogAgent(ReflectUtil.class);

	private ReflectUtil() {
		// Empty constructor.
	}
	
	public static class ReflectResult<T> {
		private T result = null;
		private boolean available = false;
		
		public T getResult() {
			return result;
		}
		private void setResult(T value) {
			this.result = value;
		}
		public boolean isAvailable() {
			return available;
		}
		private void setAvailable(boolean available) {
			this.available = available;
		}
		public Optional<T> toOptional() {
			if (available) {
				return Optional.of(result);
			} else {
				return Optional.empty();
			}
		}
	}

	public static <T> ReflectResult<T> getValue(Object instance, Field field, Class<T> valueType) {
		Objects.requireNonNull(instance);
		Objects.requireNonNull(field);
		Objects.requireNonNull(valueType);

		ReflectResult<T> result = new ReflectResult<>();

		try {
			Object value = field.get(instance);
			if (value == null) {
				result.setResult(null);
				result.setAvailable(true);
			} else if (valueType.isInstance(value)) {
				result.setResult((T) field.get(instance));
				result.setAvailable(true);
			} else {
				LOGGER.error("Get value of field " + field.getName() + " is not expected type.");
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			LOGGER.error("Get value of field " + field.getName() + " failed.", e);
		}

		return result;
	}
	
	public static ReflectResult<Object> getValue(Object instance, Field field) {
		return getValue(instance, field, Object.class);
	}

	public static ReflectResult<Field> getField(Object instance, String fieldName) {
		Objects.requireNonNull(instance);
		Objects.requireNonNull(fieldName);

		ReflectResult<Field> result = new ReflectResult<>();
		
		Class<?> clazz = instance.getClass();
		Field field = null;
		try {
			field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			
			result.setResult(field);
			result.setAvailable(true);
		} catch (NoSuchFieldException | SecurityException e) {
			LOGGER.error("Get field " + fieldName + " failed.", e);
		}

		return result;
	}

	public static <A extends Annotation> ReflectResult<Field> getField(Object instance, Class<A> annotationType,
			Predicate<A> annotationChecker) {
		Objects.requireNonNull(instance);
		Objects.requireNonNull(annotationType);
		Objects.requireNonNull(annotationChecker);
		
		ReflectResult<Field> result = new ReflectResult<>();
		
		Class<?> clazz = instance.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			A[] annotations = field.getAnnotationsByType(annotationType);
			for (A annotation : annotations) {
				if (annotationChecker.test(annotation)) {
					result.setResult(field);
					result.setAvailable(true);
					
					return result;
				}
			}
		}
		
		LOGGER.warn("Expected field is not found.");
		return result;
	}
	
	public static ReflectResult<Field> getField(Object instance, Class<? extends Annotation> annotationType) {
		return getField(instance, annotationType, annotation -> true);
	}
	
	public static <T> ReflectResult<T> getFieldValue(Object instance, String fieldName, Class<T> valueType) {
		ReflectResult<Field> fieldResult = getField(instance, fieldName);
		if (fieldResult.isAvailable()) {
			Field field = fieldResult.getResult();
			return getValue(instance, field, valueType);
		} else {
			return new ReflectResult<>();
		}
	}
	
	public static ReflectResult<Object> getFieldValue(Object instance, String fieldName) {
		return getFieldValue(instance, fieldName, Object.class);
	}
	
	public static <T> ReflectResult<T> newInstance(Class<T> clazz) {
		Objects.requireNonNull(clazz);
		
		ReflectResult<T> result = new ReflectResult<>();
		
		try {
			T value = clazz.newInstance();
			result.setResult(value);
			result.setAvailable(true);
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.error("Create new instance of class " + clazz.getSimpleName() + " failed.", e);
		}
		
		return result;
	}
	
}
