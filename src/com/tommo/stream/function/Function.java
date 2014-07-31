package com.tommo.stream.function;

/**
 * Marks an operator function, with no return value, and 1 argument
 * @author tommo
 *
 * @param <V>
 */
public interface Function<V> {

	public void operate(V v);
	
}
