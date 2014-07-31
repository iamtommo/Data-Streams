Data Streams
=========

Lightweight data flow library for Java, heavily inspired by [Dart Streams](https://www.dartlang.org/docs/tutorials/streams/)

Samples
----
Make sure to check out the test cases for more samples.

##### Creating a simple stream

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


