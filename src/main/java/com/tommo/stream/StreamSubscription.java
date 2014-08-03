package com.tommo.stream;

import com.tommo.stream.function.Function;

public class StreamSubscription<T> {
	
	private Function<T> handleData;
	private Function<T> handleDone;
	
	public StreamSubscription() {
		
	}
	
	public StreamSubscription(Function<T> handleData) {
		this.handleData = handleData;
	}
	
	public void handleData(T data) {
		if (handleData == null) {
			throw new IllegalStateException("Data handler == null");
		}
		handleData.operate(data);
	}
	
	public void onData(Function<T> handleData) {
		this.handleData = handleData;
	}
	
	public void setHandleData(Function<T> handleData) {
		this.handleData = handleData;
	}

}
