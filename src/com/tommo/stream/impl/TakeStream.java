package com.tommo.stream.impl;

import com.tommo.stream.DirectStream;
import com.tommo.stream.Stream;

public class TakeStream<T> extends DirectStream<T> {

	private Stream<T> parent;
	private int take;
	private int taken = 0;
	
	public TakeStream(Stream<T> parent, int take) {
		this.parent = parent;
		this.take = take;
	}
	
	@Override
	public void write(T data) {
		if (taken < take) {
			super.write(data);
			taken++;
		} else {
			distributeDataToSubscribers(data);
			distributeDataToSubstreams(data);
		}
	}

}
