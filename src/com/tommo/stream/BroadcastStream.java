package com.tommo.stream;

public class BroadcastStream<T> extends DirectStream<T> {

	@Override
	public void write(T data) {
		
	}
	
	@Override
	public boolean isBroadcast() {
		return true;
	}

}
