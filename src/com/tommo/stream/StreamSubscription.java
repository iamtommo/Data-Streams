package com.tommo.stream;

import com.tommo.stream.function.Function;

public class StreamSubscription<T> {
	
	private Function<T> handleData;
	private Function<T> handleDone;
	private boolean stopped = false;
	
	public StreamSubscription() {
		
	}
	
	public StreamSubscription(Function<T> handleData) {
		this.handleData = handleData;
	}
	
	public void stop() {
		stopped = true;
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
	
	public boolean hasStopped() {
		return stopped;
	}
	
	public void setHandleData(Function<T> handleData) {
		this.handleData = handleData;
	}

}
