package com.tommo.stream.impl;

import com.tommo.stream.DirectStream;
import com.tommo.stream.Future;
import com.tommo.stream.Stream;
import com.tommo.stream.function.Predicate;

public class AnyStream<T> extends DirectStream<T> {
	
	private Predicate<T> test;
	private Future<Boolean> future = new Future<Boolean>();
	
	public AnyStream(Stream<T> parent, Predicate<T> test) {
		this.test = test;
	}
	
	@Override
	public void write(T data) {
		if (test.test(data)) {
			future.set(true);
		}
		super.write(data);
	}
	
	public Future<Boolean> getFuture() {
		return future;
	}

}
