# RustOptionResult
Port and adaptation of Rust's Option and Result types to Java.

Both types contain behaviour and comments taken out from the Rust Programming Language.

## Option&lt;V&gt;
Represents an optional value using two states: Some if containing a value, or None if there is no value, you can generate one through:

| **Constructor/Method** | **Description** |
| --- | --- |
| **Option(value)** | Generates an Option as Some(value). |
| _**some(value)**_ | Generates an Option as Some(value). |
| _**empty()**_ | Generates an Option as None. |

This class implements:

| **Operation**                      | **Result** | **Description** |
|------------------------------------| --- | --- |
| **isSome()**                       | boolean | Returns true if the option is a Some value. |
| **isSomeAnd(predicate)**           | boolean | Returns true if the option is a Some and the value inside of it matches a predicate. |
| **isNone()**                       | boolean | Returns true if the option is a None value. |
| **map(mapper)**                    | Option\<U\> | Maps an Option\<T\> to Option\<U\> by applying a function to a contained value (if Some) or returns None (if None). |
| **mapOr(defaultValue,mapper)**     | U | Returns the provided default result (if none), or applies a function to the contained value (if any). |
| **mapOrElse(defaultValue,mapper)** | U | Computes a default function result (if none), or applies a different function to the contained value (if any). |
| **okOr(error)**                    | Result\<T, E\> | Transforms the Option\<T\> into a Result\<T, E\>, mapping Some(v) to Ok(v) and None to Err(err). |
| **okOrElse(error)**                | Result\<T, E\> | Transforms the Option\<T\> into a Result\<T, E\>, mapping Some(v) to Ok(v) and None to Err(err). |
| **inspect(inspector)**             | | Calls the provided inspector on the contained value (if Some). |
| **unwrap()**                       | T | Returns the contained Some value. |
| **expect(errorMessage)**           | T | Returns the contained Some value. |
| **unwrapOr(defaultValue)**         | T | Returns the contained Some value or a provided default. |
| **unwrapOrElse(defaultValue)**     | T | Returns the contained Some value or computes it from the parameter. |
| **filter(predicate)**              | Option\<T\> | Returns None if the option is None, otherwise calls predicate with the wrapped value and returns: <br> - Some(T) if predicate returns true (where T is the wrapped value). <br> - None if predicate returns false. |
| **and(res)**                       | Option\<T\> | Returns None if the option is None, otherwise returns res. |
| **andThen(res)**                   | Option\<T\> | Returns None if the option is None, otherwise calls the parameter with the wrapped value and returns the result. |
| **or(res)**                        | Option\<T\> | Returns the option if it contains a value, otherwise returns res. |
| **orElse(res)**                    | Option\<T\> | Returns the option if it contains a value, otherwise calls the parameter and returns its result. |
| **xor(res)**                       | Option\<T\> | Returns Some if exactly one of self, res is Some, otherwise returns None. |
| **insert(value)**                  | T | Inserts value into the option, then returns a reference to it. |
| **getOrInsert(value)**             | T | Inserts value into the option if it is None, then returns a reference to the contained value. |
| **getOrInsertWith(value)**         | T | Inserts a value computed from parameter function into the option if it is None, then returns a reference to the contained value. |
| **take()**                         | T | Takes the value out of the option, leaving a None in its place. |
| **takeIf(predicate)**              | T | Takes the value out of the option, but only if the predicate evaluates to true on the value. <br> In other words, replaces self with None if the predicate returns true, this method operates similar to take(), but conditional. |
| **replace(T value)**               | Option\<T\> | Replaces the actual value in the option by the value given in parameter, returning the old value if present, leaving a Some in its place without de-initializing either one. |

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
| _**from(Callable\<T\> valueGetter)**_ | Generates a Result\<T,Exception\> which will be Ok(Value) if valueGetter gives a value, or Err(Exception) if an exception is thrown while trying to get said value. |
| _**ok(value)**_ | Generates a Result as Ok(value). |
| _**err(error)**_ | Generates a Result as Err(error). |

This class implements:

| **Operation** | **Result**         | **Description**                                                                                                                                            |
| --- |----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| **isOk()** | boolean        | Returns true if the result is Ok.                                                                                                                      |
| **isOkAnd(predicate)** | boolean        | Returns true if the result is Ok and the value inside of it matches a predicate.                                                                       |
| **isErr()** | boolean        | Returns true if the result is Err.                                                                                                                     |
| **isErrAnd(predicate)** | boolean        | Returns true if the result is Err and the value inside of it matches a predicate.                                                                      |
| **ok()** | Option\<T\>    | Converts from Result\<T, E\> to Option\<T\>.                                                                                                           |
| **err()** | Option\<E\>    | Converts from Result\<T, E\> to Option\<E\>.                                                                                                           |
| **map(mapper)** | Result\<U, E\> | Maps a Result\<T, E\> to Result\<U, E\> by applying a function to a contained Ok value, leaving an Err value untouched.                                |
| **mapOr(defaultValue, mapper)** | U              | Returns the provided default (if Err), or applies a function to the contained value (if Ok).                                                           |
| **mapOrElse(defaultValue, mapper)** | U              | Maps a Result&lt;T, E&gt; to U by applying fallback function default to a contained Err value, or function defaultValueGetter to a contained Ok value. |
| **mapError(errorMapper)** | Result\<T, O\> | Maps a Result&lt;T, E&gt; to Result&lt;T, F&gt; by applying a function to a contained Err value, leaving an Ok value untouched.                        |
| **inspect(inspector)** |                |  Calls the provided functionon the contained value (if Ok).            |
| **inspectErr(inspector)** |                | Calls the provided consumer function on the contained error (if Err). |
| **unwrap()** | T              | Returns the contained Ok value.                                                                                                                        |
| **expect(errorMessage)** | T              | Returns the contained Ok value.                                                                                                                        |
| **unwrapOr(defaultValue)** | T              | Returns the contained Ok value or a provided default.                                                                                                  |
| **unwrapOrElse(defaultValue)** | T              | Returns the contained Ok value or computes it from the parameter function.                                                                             |
| **unwrapErr()** | E              | Returns the contained Err value.                                                                                                                       |
| **expectErr(errorMessage)** | E              | Returns the contained Err value.                                                                                                                       |
| **and(res)** | Result\<U, E\> | Returns res if the result is Ok, otherwise returns the Err value of this Result.                                                                       |
| **andThen(res)** | Result\<U, E\> | Returns the resulting of calling op over the value if Ok, otherwise returns the Err value of this Result.                                              |
| **or(res)** | Result\<T, O\> | Returns res if the result is Err, otherwise returns the Ok value of this Result.                                                                       |
| **orElse(res)** | Result\<T, O\> | Returns the resulting of calling op over the value if Err, otherwise returns the Ok value of self.                                                     |

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