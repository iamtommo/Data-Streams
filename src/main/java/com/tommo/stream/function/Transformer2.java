package com.tommo.stream.function;

/**
 * Marks a transform implementation that reduces two values into a single value
 * @author tommo
 *
 * @param <V>
 */
public interface Transformer2<V> {
	
	public V transform(V value1, V value2);

}
