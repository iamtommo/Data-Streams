package com.tommo.stream;

import com.tommo.stream.function.Computation;
import com.tommo.stream.function.Function;
import com.tommo.stream.function.Transformer;

public class Future<V> {
	
	/**
	 * The computation impl to be called when this Future requests a value
	 */
	private Computation<V> computation;
	
	/**
	 * The value of this Future at current point in time
	 */
	private V value;
	
	/**
	 * Flag determining if the value has been set
	 * <br>
	 * We need this because sometimes <i>null</i> is a desired value, and the default unset value
	 * of a Future, if <i>null</i>
	 */
	private boolean valueSet = false;
	
	/**
	 * The callback to be invoked when this future receives a value
	 */
	private Function<V> onValueFunction1;
	
	/**
	 * The Future superceding this Future, if exists
	 */
	private Future<V> superceder;
	
	/**
	 * Creates a new Future with no imminent value
	 */
	public Future() {

	}
	
	public Future(Future<V> superceder) {
		this.superceder = superceder;
	}
	
	public Future(Future<V> superceder, Function<V> onValue) {
		this.superceder = superceder;
		this.onValueFunction1 = onValue;
	}
	
	/**
	 * Creates a new Future with a preset value
	 * @param value The value
	 */
	public Future(V value) {
		this.value = value;
		this.valueSet = true;
	}
	
	/**
	 * Creates a new Future with a value returned from the specified {@link Computation}
	 * @param computation The {@link Computation} impl
	 */
	public Future(Computation<V> computation) {
		this.computation = computation;
	}
	
	private V fireAndReturn(V value) {
		if (onValueFunction1 != null) {
			onValueFunction1.operate(value);
		}
		return value;
	}
	
	/**
	 * Waits for the computation to finish and returns a new Future object
	 * <br><br>
	 * If there was no {@link Computation} impl given to this Future and no superceding Future in the constructor, {@link #sync(Computation)}
	 * should be called instead
	 * <br><br>
	 * If this Future has a superceding Future, {@link #sync()} on the said Future will be called, and we will wait
	 * @throws IllegalStateException If no {@link Computation} was given in the constructor and no superceding Future exists
	 * @return A new Future object promising the value of the computation
	 */
	public Future<V> sync() {
		if (valueSet) {
			fireAndReturn(value);
			return this;
		}
		if (computation == null && superceder == null) {
			throw new IllegalStateException("No computation or superceding Future exists");
		} else if (computation == null) {
			Future<V> f = superceder.sync();
			V v = f.get();
			fireAndReturn(v);
			return f;
		}
		return sync(computation);
	}
	
	/**
	 * Waits for the given computation to finish and returns a new Future object
	 * @throws IllegalStateException If no computation impl was given
	 * @return A new Future object promising the value of the computation
	 */
	public Future<V> sync(Computation<V> computation) {
		if (computation == null) {
			throw new IllegalStateException("Sync called on future with no computation object given");
		}
		return new Future<V>(fireAndReturn(computation.compute()));
	}
	
	/**
	 * Registers a transformer that will be called when this Future has a value
	 * <br><br>
	 * Warning: this method calls {@link #sync()} on the current Future, which waits indeterminently for a value, before transforming
	 * <br><br>
	 * If this Future already has a value, a new Future will be returned with the value of this current Future
	 * which has been transformed by the given {@link Transformer}. If not, the callback will not be invoked until it does.
	 * @throws IllegalStateException If no computation or value has been set for this Future
	 * @param onValue The callback to be invoked when this Future has a value
	 * @return A new Future, with the value of this current Future, after being transformed by the callback
	 */
	public Future<V> transform(Transformer<V> onValue) {
		if (valueSet) {
			return new Future<V>(onValue.transform(get()));
		}
		
		if (computation == null) {
			throw new IllegalStateException("No computation or value has been set for this Future");
		}
		
		return new Future<V>(onValue.transform(sync().get()));
	}
	
	/**
	 * Registers a callback which will be called when this future has a value
	 * @param onValue The callback to be invoked when this Future has a value
	 * @return If we have a value, we return a new Future with the value, else we return a Future with a value
	 * which is grabbed when the superceding Future we're waiting on has received a value
	 */
	public Future<V> then(Function<V> onValue) {
		if (valueSet) {
			onValueFunction1 = onValue;
			return new Future<V>(get());
		}
		
		return new Future<V>(this, onValue);
	}
	
	/**
	 * Creates a {@link Stream} which receives the value of this Future, and fires events
	 * to it's subscribers
	 * <br><br>
	 * Note that the stream will be created instantly with the current value of this Future,
	 * regardless if calculated or not
	 * @return The {@link Stream}
	 */
	public Stream<V> asStream() {
		Stream<V> stream = Stream.fromValue(get());
		return stream;
	}
	
	/**
	 * Sets the value of this Future
	 * @param value The new value
	 * @return This Future
	 */
	public Future<V> set(V value) {
		valueSet = true;
		this.value = value;
		sync();
		return new Future<V>(value);
	}
	
	/**
	 * Returns the value of this Future and invoked on value callbacks if necessary
	 * </br></br>
	 * Make sure that if this Future was defined with no initial value and no computation impl, {@link #sync(Computation)} must be called
	 * @return The value
	 */
	public V get() {
		sync();
		return value;
	}
	
}
