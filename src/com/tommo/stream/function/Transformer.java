package com.tommo.stream.function;

/**
 * Marks a transform implementation
 * @author tommo
 *
 * @param <V>
 */
public interface Transformer<V> {
	
	public V transform(V value);

}
