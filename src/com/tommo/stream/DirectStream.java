package com.tommo.stream;

import java.util.LinkedList;
import java.util.Queue;

import com.tommo.stream.function.Function;

public class DirectStream<T> extends Stream<T> {
	
	/**
	 * Since this is a single-listener direct stream, we buffer the data if no
	 * listener has been added yet
	 */
	private Queue<T> backlog = new LinkedList<T>();
	
	protected DirectStream() {
		
	}
	
	public Future<Integer> length() {
		return new Future<Integer>(backlog.size());
	}
	
	public Future<T> single() {
		final Future<T> future = new Future<T>();
		final StreamSubscription<T> sub = new StreamSubscription<T>();
		sub.setHandleData(new Function<T>() {
			@Override
			public void operate(T v) {
				future.sync(v);
				DirectStream.this.removeSubscriber(sub);
			}
		});
		addSubscriber(sub);
		return future;
	}

	@Override
	public void write(T data) {
		if (hasSubscriber()) {
			for (StreamSubscription<T> sub : getSubscribers()) {
				sub.handleData(data);
			}
		} else {
			backlog.offer(data);
		}
	}
	
	@Override
	public void addSubscriber(StreamSubscription<T> subscriber) {
		if (getSubscribers().size() > 0) {
			throw new IllegalStateException("Non-broadcast streams only allow a single listener");
		}
		super.addSubscriber(subscriber);
		if (backlog.size() > 0) {
			emptyAndFireBacklog();
		}
	}
	
	protected void emptyAndFireBacklog() {
		while (!backlog.isEmpty() && hasSubscriber()) {
			write(backlog.poll());
		}
	}
	
	public boolean hasSubscriber() {
		return getSubscribers().size() > 0;
	}
	
	public StreamSubscription<T> getSubscriber() {
		return getSubscribers().get(0);
	}

	public Queue<T> getBacklog() {
		return backlog;
	}
	
}
