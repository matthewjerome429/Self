package com.cathaypacific.mmbbizrule.util;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.validation.constraints.NotNull;

/**
 * Encapsulate a property of object, or a chain of properties. Make it safe to get or set even when some level is null.
 * 
 * @param <O>
 *            the type of object with this property.
 * @param <T>
 *            the type of this property.
 */
public class NullSafeProperty<O, T> {

	private Supplier<T> constructor;
	
	private Function<O, T> getter;
	
	private BiConsumer<O, T> setter;
	
	private O object;
	
	private NullSafeProperty<?, O> baseProperty;
	
	private NullSafeProperty(@NotNull Function<O, T> getter, @NotNull BiConsumer<O, T> setter,
			@NotNull Supplier<T> constructor) {

		this.getter = getter;
		this.setter = setter;
		this.constructor = constructor;
		
		Objects.requireNonNull(getter);
		Objects.requireNonNull(setter);
		Objects.requireNonNull(constructor);
	}
	
	/**
	 * Constructs <code>NullSafeProperty</code> base on a object.
	 * 
	 * @param object
	 * @param getter
	 * @param setter
	 * @param constructor
	 */
	public NullSafeProperty(@NotNull O object, @NotNull Function<O, T> getter, @NotNull BiConsumer<O, T> setter,
			@NotNull Supplier<T> constructor) {

		this(getter, setter, constructor);
		this.object = object;
		
		Objects.requireNonNull(object);
	}
	
	/**
	 * Constructs <code>NullSafeProperty</code> base on another <code>NullSafeProperty</code>.
	 * 
	 * @param baseProperty
	 * @param getter
	 * @param setter
	 * @param constructor
	 */
	public NullSafeProperty(@NotNull NullSafeProperty<?, O> baseProperty, Function<O, T> getter,
			BiConsumer<O, T> setter, Supplier<T> constructor) {

		this(getter, setter, constructor);
		this.baseProperty = baseProperty;
		
		Objects.requireNonNull(baseProperty);
	}
	
	/**
	 * Gets property value.<br>
	 * If this property and all base levels are not null, just return current value.
	 * If one of based properties is null, returns null.
	 * 
	 * @return
	 */
	public T get() {

		if (object != null) {
			return getter.apply(object);
		} else {
			O baseObject = baseProperty.get();
			if (baseObject != null) {
				return getter.apply(baseObject);
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Sets property value.<br>
	 * If one of based properties is null and set value is not null, create default object to every base level of null.
	 * 
	 * @param value
	 */
	public void set(T value) {
		
		if (value == null) {
			return;
		}

		if (object != null) {
			setter.accept(object, value);
		} else {
			O baseObject = baseProperty.get();
			if (baseObject != null) {
				setter.accept(baseObject, value);
			} else {
				baseObject = baseProperty.makeNotNull();
				setter.accept(baseObject, value);
			}
		}
		
	}
	
	/**
	 * Makes property not null.<br>
	 * If this property and all base levels are not null, just return current value.
	 * If one of based properties is null, create default object to every level of null.
	 * 
	 * @return
	 */
	public T makeNotNull() {
		
		T value = get();
		if (value != null) {
			return value;
		}
		
		T initValue = constructor.get();
		if (object != null) {
			setter.accept(object, initValue);
		} else {
			O baseObject = baseProperty.get();
			if (baseObject != null) {
				setter.accept(baseObject, initValue);
			} else {
				baseObject = baseProperty.makeNotNull();
				setter.accept(baseObject, initValue);
			}
		}
		return initValue;
	}
	
}
