package com.tommo.stream.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.tommo.stream.DirectStream;
import com.tommo.stream.Stream;
import com.tommo.stream.function.Function;

@RunWith(JUnit4.class)
public class BroadcastStreamTest {
	
	private final Integer[] defaultIntArray = new Integer[] { 1, 2, 3, 4 };
	private int i = 0;
	private Function<Integer> defaultIncFunction = new Function<Integer>() {
		@Override
		public void operate(Integer v) {
			BroadcastStreamTest.this.i++;
		}
	};
	
	@Test
	public void testBroadcast() {
		i = 0;
		Stream<Integer> stream = new DirectStream<Integer>().asBroadcast();
		stream.listen(defaultIncFunction);
		stream.listen(defaultIncFunction);
		stream.write(defaultIntArray);
		Assert.assertEquals(8, i);
	}

}
