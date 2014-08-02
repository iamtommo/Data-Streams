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
			for (int i = 0; i < getSubscribers().size(); i++) {
				StreamSubscription<T> sub = getSubscribers().get(i);
				sub.handleData(data);
			}
		} else {
			backlog.offer(data);
		}
		
		for (Stream<T> stream : getSubstreams()) {
			stream.write(data);
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
	
	protected void initializeFromExisting(Stream<T> existing) {
		for (StreamSubscription<T> sub : existing.getSubscribers()) {
			addSubscriber(sub);
		}
		if (existing instanceof DirectStream) {
			DirectStream<T> ds = (DirectStream<T>) existing;
			for (T data : ds.getBacklog()) {
				getBacklog().offer(data);
			}
		}
		emptyAndFireBacklog();
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
