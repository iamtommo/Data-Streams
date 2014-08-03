package com.tommo.stream.impl;

import com.tommo.stream.DirectStream;
import com.tommo.stream.Future;
import com.tommo.stream.Stream;

public class ElementAtStream<T> extends DirectStream<T> {
	
	private int currentIndex = 0;
	private int index;
	private Future<T> future = new Future<T>();
	
	public ElementAtStream(Stream<T> parent, int index) {
		this.index = index;
	}
	
	@Override
	public void write(T data) {
		if (currentIndex == index) {
			future.set(data);
		}
		currentIndex++;
		super.write(data);
	}
	
	public Future<T> getFuture() {
		return future;
	}

}
