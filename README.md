Data Streams
=========

Lightweight data flow library for Java, heavily inspired by [Dart Streams](https://www.dartlang.org/docs/tutorials/streams/)

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
You can also broadcast an existing stream, allowing it to essentially act as a broadcast stream, by using this factory call:
```
Stream<Integer> stream = Stream.broadcast(Stream.fromArray(new Integer[] { 1, 2, 3 }));
```
