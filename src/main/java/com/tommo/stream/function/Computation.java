package com.tommo.stream.function;

/**
 * Marks a computation implementation
 * @author tommo
 *
 * @param <T>
 */
public interface Computation<T> {
	
	public T compute();

}
