package com.tommo.stream.function;

import java.util.LinkedList;
import java.util.List;

/**
 * Encompasses a cumulative chain of {@link Transformer}s
 * @author tommo
 *
 */
public class CumulativeTransformer<T> {
	
	private List<Transformer<T>> transformers = new LinkedList<Transformer<T>>();
	
	public CumulativeTransformer() {
		
	}
	
	public T transform(T data) {
		for (Transformer<T> t : transformers) {
			data = t.transform(data);
		}
		return data;
	}
	
	public void addTransformer(Transformer<T> transformer) {
		transformers.add(transformer);
	}

}
