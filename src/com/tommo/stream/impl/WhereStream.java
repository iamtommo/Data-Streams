package com.tommo.stream.impl;

import com.tommo.stream.DirectStream;
import com.tommo.stream.Stream;
import com.tommo.stream.function.Predicate;

public class WhereStream<T> extends DirectStream<T> {
	
	private Stream<T> parent;
	private Predicate<T> predicate;
	
	public WhereStream(Stream<T> parent, Predicate<T> predicate) {
		this.parent = parent;
		this.predicate = predicate;
	}

	@Override
	public void write(T data) {
		if (predicate.test(data)) {
			super.write(data);
		}
	}
	
}
