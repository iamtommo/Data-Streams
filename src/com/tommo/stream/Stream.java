package com.tommo.stream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tommo.stream.function.Function;

/**
 * A stream consists of a flow of data, which can be listened to
 * <br><br>
 * There are two types of Streams. A single subscription {@link DirectStream} is where there's only one subscriber allowed
 * and all events will be backlogged and fired when a listener is added. The other is a {@link BroadcastStream}, where multiple subscribers
 * are permitted, and events will be fired and never backlogged, regardless of how many subscribers it has and when the data was fired.
 * @author tommo
 *
 * @param <T> The data type flowing through this stream
 */
public abstract class Stream<T> {
	
	private List<StreamSubscription<T>> subscribers = new ArrayList<StreamSubscription<T>>();
	
	protected Stream() {
		
	}
	
	public static <T> Stream<T> broadcast(DirectStream<T> deriveFrom) {
		return new BroadcastStream<T>(deriveFrom);
	}
	
	public static <T> Stream<T> newStream() {
		return new DirectStream<T>();
	}
	
	public static <T> Stream<T> fromValue(T value) {
		Stream<T> stream = new DirectStream<T>();
		stream.write(value);
		return stream;
	}
	
	public static <T> Stream<T> fromIterable(Iterable<T> i) {
		Stream<T> stream = new DirectStream<T>();
		stream.write(i);
		return stream;
	}
	
	public static <T> Stream<T> fromArray(T[] array) {
		Stream<T> stream = new DirectStream<T>();
		stream.write(array);
		return stream;
	}
	
	public static <T> Stream<T> newBroadcast() {
		return new BroadcastStream<T>();
	}
	
	public abstract void write(T data);
	
	/**
	 * Returns the length of this stream
	 * @return The length
	 */
	public abstract Future<Integer> length();
	
	/**
	 * Returns the next value this stream receives
	 * @return The next value
	 */
	public abstract Future<T> single();
	
	public void write(T[] data) {
		for (T t : data) {
			write(t);
		}
	}
	
	public void write(Iterable<T> data) {
		Iterator<T> iter = data.iterator();
		while (iter.hasNext()) {
			write(iter.next());
		}
	}
	
	/**
	 * Checks if this Stream is a broadcast stream, meaning it can have multiple listeners
	 * @return true if this is a broadcast stream, false if it is a direct single subscription stream
	 */
	public boolean isBroadcast() {
		return false;
	}
	
	public void addSubscriber(StreamSubscription<T> subscriber) {
		subscribers.add(subscriber);
	}
	
	public void removeSubscriber(StreamSubscription<T> subscriber) {
		subscribers.remove(subscriber);
	}
	
	public StreamSubscription<T> listen(Function<T> functor) {
		StreamSubscription<T> subscriber = new StreamSubscription<T>(functor);
		addSubscriber(subscriber);
		return subscriber;
	}

	public List<StreamSubscription<T>> getSubscribers() {
		return subscribers;
	}
	
}
