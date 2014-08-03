package com.tommo.stream.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.tommo.stream.Stream;
import com.tommo.stream.function.Function;
import com.tommo.stream.function.Predicate;

@RunWith(JUnit4.class)
public class StreamTest {
	
	private final Integer[] defaultIntArray = new Integer[] { 1, 2, 3, 4 };
	int i;
	
	@Test
	public void fromArrayTest() {
		i = 1;
		
		Stream<Integer> stream = Stream.fromArray(defaultIntArray);
		stream.listen(new Function<Integer>() {
			@Override
			public void operate(Integer data) {
				Assert.assertTrue(StreamTest.this.i == data);
				StreamTest.this.i++;
			}
		});
	}
	
	@Test
	public void fromIterableTest() {
		i = 1;
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		
		Stream<Integer> stream = Stream.fromIterable(list);
		stream.listen(new Function<Integer>() {
			@Override
			public void operate(Integer data) {
				Assert.assertTrue(StreamTest.this.i == data);
				StreamTest.this.i++;
			}
		});
	}
	
	@Test
	public void fromValueTest() {
		Stream<String> stream = Stream.fromValue("hi");
		stream.listen(new Function<String>() {
			@Override
			public void operate(String data) {
				Assert.assertTrue(data.equals("hi"));
			}
		});
	}
	
	@Test
	public void firstValueTest() {
		Stream<Integer> stream = Stream.newStream();
		stream.single().then(new Function<Integer>() {
			@Override
			public void operate(Integer v) {
				Assert.assertTrue(v == 69);
			}
		});
		
		stream.write(69);
		stream.write(100);
	}
	
	@Test
	public void lengthTest() {
		Stream<Integer> stream = Stream.fromArray(defaultIntArray);
		Assert.assertTrue(stream.length().get() == defaultIntArray.length);
	}
	
	@Test
	public void isBroadcastTest() {
		Stream<Integer> stream = Stream.newStream();
		Assert.assertFalse(stream.isBroadcast());
	}
	
	@Test
	public void singleTest() {
		Stream<Integer> stream = Stream.fromArray(defaultIntArray);
		Assert.assertTrue(stream.length().get() == defaultIntArray.length);
		Assert.assertTrue(stream.single().get() == defaultIntArray[0]);
		Assert.assertTrue(stream.length().get() == defaultIntArray.length - 1);
	}

	@Test
	public void whereTest() {
		Stream<Integer> stream = Stream.fromArray(defaultIntArray);
		stream.where(new Predicate<Integer>() {
			@Override
			public boolean test(Integer t) {
				return t == 69;
			}
		}).listen(new Function<Integer>() {
			@Override
			public void operate(Integer v) {
				Assert.assertTrue(v == 69);
			}
		});
		
		stream.write(1);
		stream.write(69);
		stream.write(13);
	}
	
	@Test
	public void takeTest() {
		Stream<Integer> stream = Stream.fromArray(defaultIntArray);
		Assert.assertEquals((int) 2, (int) stream.take(2).length().get());
		Assert.assertEquals((int) 1, (int) stream.take(2).where(new Predicate<Integer>() {
			@Override
			public boolean test(Integer t) {
				return t == 1;
			}
		}).length().get());
	}
	
	@Test
	public void anyTest() {
		Stream<Integer> stream = Stream.fromArray(defaultIntArray);
		
		Assert.assertTrue(stream.any(new Predicate<Integer>() {
			@Override
			public boolean test(Integer t) {
				return t % 2 == 0;
			}
		}).get());
	}
	
}
