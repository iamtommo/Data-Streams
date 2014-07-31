package com.tommo.stream.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.tommo.stream.Future;
import com.tommo.stream.function.Computation;
import com.tommo.stream.function.Function;
import com.tommo.stream.function.Transformer;

@RunWith(JUnit4.class)
public class FutureTest {
	
	private int i = 0;
	private final int defaultIntValue = 1;
	private final Computation<Integer> defaultIntComputation = new Computation<Integer>() {
		@Override
		public Integer compute() {
			return defaultIntValue;
		}
	};
	
	private final Transformer<Integer> defaultIntTransformer = new Transformer<Integer>() {
		@Override
		public Integer transform(Integer value) {
			return value + 1;
		}
	};
	
	@Test
	public void valueConstructorTest() {
		Future<Integer> future = new Future<Integer>(69);
		Assert.assertTrue(future.get() == 69);
	}

	@Test
	public void computationConstructorTest() {
		Future<Integer> future = new Future<Integer>(new Computation<Integer>() {
			@Override
			public Integer compute() {
				return 6 + 4;
			}
		});
		
		Assert.assertTrue(future.sync().get() == 10);
	}
	
	@Test(expected = IllegalStateException.class)
	public void syncTest() {
		Future<Integer> future = new Future<Integer>();
		
		Assert.assertTrue(future.get() == null);
		
		Future<Integer> newFuture = future.sync(new Computation<Integer>() {
			@Override
			public Integer compute() {
				return 5;
			}
		});
		
		Assert.assertTrue(future.get() == null);
		Assert.assertTrue(newFuture.get() == 5);
	}
	
	@Test
	public void thenTestTransformer() {
		Future<Integer> future = new Future<Integer>();
		
		Assert.assertTrue(future.sync(defaultIntComputation)
				.transform(defaultIntTransformer)
				.transform(defaultIntTransformer)
				.get() == (defaultIntValue + 2));
	}
	
	@Test
	public void thenTestFunction() {
		Future<Integer> future = new Future<Integer>();
		
		Future<Integer> f = future.sync(defaultIntComputation)
				.then(new Function<Integer>() {
					@Override
					public void operate(Integer v) {
						FutureTest.this.i += 1;
					}
				})
				.then(new Function<Integer>() {
					@Override
					public void operate(Integer v) {
						FutureTest.this.i += 2;
					}
				});

		Assert.assertTrue(f.get() == defaultIntValue);
		Assert.assertTrue(FutureTest.this.i == 3);
	}
	
	@Test
	public void testSupercedingFuture() {
		Future<Integer> parent = new Future<Integer>();
		
		parent.then(new Function<Integer>() {
			@Override
			public void operate(Integer v) {
				Assert.assertTrue(v == 69);
			}
		}).sync(new Computation<Integer>() {
			@Override
			public Integer compute() {
				return 69;
			}
		});
	}
	
}
