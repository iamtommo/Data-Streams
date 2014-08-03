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
	
	public DirectStream() {
		
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
			distributeDataToSubscribers(data);
		} else if (!isBroadcast()){
			backlog.offer(data);
		}
		
		distributeDataToSubstreams(data);
	}
	
	protected void distributeDataToSubscribers(T data) {
		for (int i = 0; i < getSubscribers().size(); i++) {
			StreamSubscription<T> sub = getSubscribers().get(i);
			sub.handleData(data);
		}
	}
	
	protected void distributeDataToSubstreams(T data) {
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
	
	@Override
	protected Stream<T> newSubstream(Stream<T> substream) {
		super.newSubstream(substream);
		keepAndFireBacklog();
		return substream;
	}
	
	/**
	 * Fires all data in the backlog, but does not clear it
	 * <br><br>
	 * This is used for when the stream has no subscribers, but has a substream
	 */
	@SuppressWarnings("unchecked")
	protected void keepAndFireBacklog() {
		T[] data = (T[]) backlog.toArray();
		//TODO OK so technically we clear it, but we re-add it all :>
		backlog.clear();
		for (T d : data) {
			write(d);
		}
	}
	
	/**
	 * Empties and fires all data in the backlog, makes sure we currently have a subscriber, else it's a no-go
	 */
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
