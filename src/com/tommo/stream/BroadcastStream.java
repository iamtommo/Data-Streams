package com.tommo.stream;

/**
 * A broadcast stream, allowing for multiple listeners
 * @author tommo
 *
 * @param <T> The data type
 */
public class BroadcastStream<T> extends DirectStream<T> {
	
	public BroadcastStream() {
		
	}

	/**
	 * Creates a new broadcast stream mirroring the given stream
	 * <br><br>
	 * Note that upon deriving from an existing stream with backlogged events, this stream will
	 * distribute them as soon as possible
	 * @param deriveFrom The existing stream to derive from
	 */
	public BroadcastStream(Stream<T> deriveFrom) {
		initializeFromExisting(deriveFrom);
	}
	
	@Override
	public void addSubscriber(StreamSubscription<T> subscriber) {
		getSubscribers().add(subscriber);
		if (getBacklog().size() > 0) {
			emptyAndFireBacklog();
		}
	}
	
	@Override
	public boolean isBroadcast() {
		return true;
	}

}
