package com.tommo.stream.impl;

import com.tommo.stream.DirectStream;
import com.tommo.stream.Stream;

public class SkipStream<T> extends DirectStream<T> {
	
	private int skipped = 0;
	private int count;
	
	public SkipStream(Stream<T> parent, int count) {
		this.count = count;
	}
	
	@Override
	public void write(T data) {
		if (skipped == count) {
			super.write(data);
		} else {
			skipped++;
		}
	}

}
