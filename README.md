# RustOptionResult
Port and adaptation of Rust's Option and Result types to Java.

Both types contain behaviour and comments taken out from the Rust Programming Language.

## Option&lt;V&gt;
Represents an optional value using two states: Some if containing a value, or None if there is no value, you can generate one through:

| **Constructor/Method** | **Description** |
| --- | --- |
| **Option(value)** | _Generates an Option as Some(value)._ |
| **some(value)** | _Generates an Option as Some(value)._ |
| **empty()** | _Generates an Option as None._ |

This class implements:

| **Operation**                      | **Result** | **_Description_** |
|------------------------------------| --- | --- |
| **isSome()**                       | boolean | _Returns true if the option is a Some value._ |
| **isSomeAnd(predicate)**           | boolean | _Returns true if the option is a Some and the value inside of it matches a predicate._ |
| **isNone()**                       | boolean | _Returns true if the option is a None value._ |
| **map(mapper)**                    | Option\<U\> | _Maps an Option\<T\> to Option\<U\> by applying a function to a contained value (if Some) or returns None (if None)._ |
| **mapOr(defaultValue,mapper)**     | U | _Returns the provided default result (if none), or applies a function to the contained value (if any)._ |
| **mapOrElse(defaultValue,mapper)** | U | _Computes a default function result (if none), or applies a different function to the contained value (if any)._ |
| **okOr(error)**                    | Result\<T, E\> | _Transforms the Option\<T\> into a Result\<T, E\>, mapping Some(v) to Ok(v) and None to Err(err)._ |
| **okOrElse(error)**                | Result\<T, E\> | _Transforms the Option\<T\> into a Result\<T, E\>, mapping Some(v) to Ok(v) and None to Err(err)._ |
| **inspect(inspector)**             | | _Calls the provided inspector on the contained value (if Some)._ |
| **unwrap()**                       | T | _Returns the contained Some value._ |
| **expect(errorMessage)**           | T | _Returns the contained Some value._ |
| **unwrapOr(defaultValue)**         | T | _Returns the contained Some value or a provided default._ |
| **unwrapOrElse(defaultValue)**     | T | _Returns the contained Some value or computes it from the parameter._ |
| **filter(predicate)**              | Option\<T\> | _Returns None if the option is None, otherwise calls predicate with the wrapped value and returns: <br> - Some(T) if predicate returns true (where T is the wrapped value). <br> - None if predicate returns false._ |
| **and(res)**                       | Option\<T\> | _Returns None if the option is None, otherwise returns res._ |
| **andThen(res)**                   | Option\<T\> | _Returns None if the option is None, otherwise calls the parameter with the wrapped value and returns the result._ |
| **or(res)**                        | Option\<T\> | _Returns the option if it contains a value, otherwise returns res._ |
| **orElse(res)**                    | Option\<T\> | _Returns the option if it contains a value, otherwise calls the parameter and returns its result._ |
| **xor(res)**                       | Option\<T\> | _Returns Some if exactly one of self, res is Some, otherwise returns None._ |
| **insert(value)**                  | T | _Inserts value into the option, then returns a reference to it._ |
| **getOrInsert(value)**             | T | _Inserts value into the option if it is None, then returns a reference to the contained value._ |
| **getOrInsertWith(value)**         | T | _Inserts a value computed from parameter function into the option if it is None, then returns a reference to the contained value._ |
| **take()**                         | T | _Takes the value out of the option, leaving a None in its place._ |
| **takeIf(predicate)**              | T | _Takes the value out of the option, but only if the predicate evaluates to true on the value. <br> In other words, replaces self with None if the predicate returns true, this method operates similar to take(), but conditional._ |
| **replace(T value)**               | Option\<T\> | _Replaces the actual value in the option by the value given in parameter, returning the old value if present, leaving a Some in its place without de-initializing either one._ |

### Example:


```java
public class MyClass {
    public static void main(String[] args) {
        Option<Integer> myOptionalNumber = Option.some(7);
        Option<String> myOptionAsString = myOptionalNumber.map(Object::toString);
        String unwrappedValue = myOptionAsString.unwrapOr("No number found");
        System.out.println(unwrappedValue);
    }
}
```

## Result&lt;T, E&gt;


A type used for returning and propagating errors. It represents two possible states: <br>
- Ok(T), representing success and containing a value. <br>
- Err(E), representing error and containing an error value.

You can generate one through:

| **Constructor/Method** | **Description** |
| --- | --- |
| **from(Callable\<T\> valueGetter)** | _Generates a Result\<T,Exception\> which will be Ok(Value) if valueGetter gives a value, or Err(Exception) if an exception is thrown while trying to get said value._ |
| **ok(value)** | _Generates a Result as Ok(value)._ |
| **err(error)** | _Generates a Result as Err(error)._ |

This class implements:

| Operation | Result         | _Description_                                                                                                                                            |
| --- |----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| isOk() | boolean        | _Returns true if the result is Ok._                                                                                                                      |
| isOkAnd(predicate) | boolean        | _Returns true if the result is Ok and the value inside of it matches a predicate._                                                                       |
| isErr() | boolean        | _Returns true if the result is Err._                                                                                                                     |
| isErrAnd(predicate) | boolean        | _Returns true if the result is Err and the value inside of it matches a predicate._                                                                      |
| ok() | Option\<T\>    | _Converts from Result\<T, E\> to Option\<T\>._                                                                                                           |
| err() | Option\<E\>    | _Converts from Result\<T, E\> to Option\<E\>._                                                                                                           |
| map(mapper) | Result\<U, E\> | _Maps a Result\<T, E\> to Result\<U, E\> by applying a function to a contained Ok value, leaving an Err value untouched._                                |
| mapOr(defaultValue, mapper) | U              | _Returns the provided default (if Err), or applies a function to the contained value (if Ok)._                                                           |
| mapOrElse(defaultValue, mapper) | U              | _Maps a Result&lt;T, E&gt; to U by applying fallback function default to a contained Err value, or function defaultValueGetter to a contained Ok value._ |
| mapError(errorMapper) | Result\<T, O\> | _Maps a Result&lt;T, E&gt; to Result&lt;T, F&gt; by applying a function to a contained Err value, leaving an Ok value untouched._                        |
| inspect(inspector) |                |  _Calls the provided functionon the contained value (if Ok)._            |
| inspectErr(inspector) |                | _Calls the provided consumer function on the contained error (if Err)._ |
| unwrap() | T              | _Returns the contained Ok value._                                                                                                                        |
| expect(errorMessage) | T              | _Returns the contained Ok value._                                                                                                                        |
| unwrapOr(defaultValue) | T              | _Returns the contained Ok value or a provided default._                                                                                                  |
| unwrapOrElse(defaultValue) | T              | _Returns the contained Ok value or computes it from the parameter function._                                                                             |
| unwrapErr() | E              | _Returns the contained Err value._                                                                                                                       |
| expectErr(errorMessage) | E              | _Returns the contained Err value._                                                                                                                       |
| and(res) | Result\<U, E\> | _Returns res if the result is Ok, otherwise returns the Err value of this Result._                                                                       |
| andThen(res) | Result\<U, E\> | _Returns the resulting of calling op over the value if Ok, otherwise returns the Err value of this Result._                                              |
| or(res) | Result\<T, O\> | _Returns res if the result is Err, otherwise returns the Ok value of this Result._                                                                       |
| orElse(res) | Result\<T, O\> | _Returns the resulting of calling op over the value if Err, otherwise returns the Ok value of self._                                                     |

### Example:

```java
public class MyClass {
    public static void main(String[] args) {
        //Generates a result from an operation that throws an exception, resulting in Result being Err.
        Result<Integer, Exception> myOverflowedValue = Result.from(() -> Math.addExact(Integer.MAX_VALUE, 1));
        Result<Integer, Exception> myReplacedValue = myOverflowedValue.or(Result.ok(5));
        Result<String, Exception> myMappedValue = myReplacedValue.map(Object::toString);
        System.out.println(myMappedValue.unwrap());
    }
}
```