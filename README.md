Data Streams
=========

Lightweight data flow library for Java, heavily inspired by [Dart Streams](https://www.dartlang.org/docs/tutorials/streams/)

Using the library
---
The project is configured to use Maven as the default build tool. Maven will generate a jar, useable in your project, by using this command in the top directory.
```
mvn package
```

Samples
----
Make sure to check out the test cases for more samples.

##### The two types of streams
As in dart streams, there are two flavours, the single subscriber DirectStream, and the multi subscriber BroadcastStream. When a single-subscriber stream receives data but has no subscribers, it will backlog the data until a subscriber is added, then fire all data. On the contrary, a broadcast stream will always immediately fire and dispose of data regardless of how many subscribers it has, and will fire all data to all subscribers.

##### Creating a simple single-subscriber stream

There are several factory methods for creating a new stream, or a stream from an existing data source, these are:

```
Stream.newStream();
Stream.fromValue(Value);
Stream.fromIterable(Iterable);
Stream.fromArray(Array);
```

##### Listening to a stream
```
Stream<Integer> stream = Stream.fromArray(new Integer[] { 1, 2, 3 });
stream.listen(new Function<Integer>() {
    public void operate(Integer data) {
        System.out.println(data);
    }
});
```
This snippet will print out each number in the array in succession.

##### Broadcast streams
To create a broadcast stream, simply call:
```
Stream<Integer> stream = Stream.newBroadcast();
```
You can also broadcast an existing stream, allowing it to essentially act as a broadcast stream:
```
Stream<Integer> stream = Stream.fromArray(new Integer[] { 1, 2, 3 }).asBroadcast();
```

Manipulating data flow
---

##### *Where*
The Stream class contains many filter functions to manipulate data.
The *where* stream will create a sub stream which validates all data with the given predicate before firing events.

```
stream.where(new Predicate<Integer>() {
	@Override
	public boolean test(Integer data) {
		return data > 0;
	}
});
```
We can then listen to this filtered stream by calling the listen method on the returned stream as shown above.

##### *Take*
Next up is the *take* stream, which appropriately takes only a select amount of data from the stream

```
stream.take(5);
```

Putting this together, we can create complex data flow structures such as:

```
Stream<Integer> stream = Stream.fromArray(new Integer[] { 1, 2, 3, 4, 5 });
stream.take(2)
	.where(new Predicate<Integer>() {
		@Override
		public boolean test(Integer t) {
			return t % 2 == 0;
		}
	}).listen(new Function<Integer>() {
		@Override
		public void operate(Integer data) {
			System.out.println(data);
		}
	});
```

This snippet will produce the output:
```
2
```

