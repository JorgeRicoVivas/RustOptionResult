package org.jorge_rico_vivas.rust;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A type used for returning and propagating errors. It represents two possible states: <br>
 * - Ok(T), representing success and containing a value. <br>
 * - Err(E), representing error and containing an error value.
 * <br><br>
 * This is a port and Java adaptation of the Rust's <a href="https://doc.rust-lang.org/std/result/index.html">Rust's org.jorge_rico_vivas.rust.Result Type</a>.
 *
 * @author Jorge Rico Vivas
 * @param <T> Type of contained value, if this value is set, then error must be null.
 * @param <E> Type of contained error, if this value is set, then value must be null.
 */
public class Result<T, E> {

    private boolean isOk;
    private T value;
    private E error;

    private Result() {
    }

    /**
     * Generates a org.jorge_rico_vivas.rust.Result&lt;T,Exception&gt; which will be Ok(Value) if valueGetter gives a value, or Err(Exception) if an
     * exception is thrown while trying to get said value.
     * <br><br>
     * Example:
     * <pre>{@code
     * public class Main {
     *     public static void main(String[] args) {
     *         // Tries to read from a file that doesn't exist
     *         org.jorge_rico_vivas.rust.Result<List<String>, Exception> result = org.jorge_rico_vivas.rust.Result.from(
     *             () -> Files.readAllLines(new File("NonExistentFile").toPath())
     *         );
     *         org.jorge_rico_vivas.rust.Result<String, Exception> asString = result.map(Object::toString);
     *         String text = asString.unwrapOr("Path was wrong, please, contact the development team");
     *         System.out.println(text);
     *     }
     * }
     * }</pre>
     *
     * @param valueGetter Supplier giving the value.
     * @param <T>         Value type.
     * @return org.jorge_rico_vivas.rust.Result&lt;T, Exception&gt;.
     */
    public static <T> Result<T, Exception> from(Callable<T> valueGetter) {
        try {
            return Result.ok(valueGetter.call());
        } catch (Exception ex) {
            return Result.err(ex);
        }
    }

    /**
     * Generates a org.jorge_rico_vivas.rust.Result as Ok(value).
     * <p>
     * Note: Value cannot be null.
     *
     * @param value value contained on Ok(value).
     * @param <T>   Value type.
     * @param <E>   Error type.
     * @return org.jorge_rico_vivas.rust.Result as Ok(value).
     */
    public static <T, E> Result<T, E> ok(T value) {
        Result<T, E> res = new Result<>();
        res.value = value;
        res.isOk = true;
        return res;
    }

    /**
     * Generates a org.jorge_rico_vivas.rust.Result as Err(error).
     * <p>
     * Note: Error cannot be null.
     *
     * @param error error contained on Err(error).
     * @param <T>   Value type.
     * @param <E>   Error type.
     * @return org.jorge_rico_vivas.rust.Result as Err(error).
     */
    public static <T, E> Result<T, E> err(E error) {
        Result<T, E> res = new Result<>();
        res.error = error;
        res.isOk = false;
        return res;
    }

    /**
     * Returns true if the result is Ok.
     *
     * @return true if the result is Ok.
     */
    public boolean isOk() {
        return isOk;
    }

    /**
     * Returns true if the result is Ok and the value inside of it matches a predicate.
     *
     * @param predicate predicated tested against the value if the result is Ok.
     * @return true if the result is Ok and the value inside of it matches a predicate.
     */
    public boolean isOkAnd(Predicate<T> predicate) {
        return isOk && predicate.test(value);
    }

    /**
     * Returns true if the result is Err.
     *
     * @return true if the result is Err.
     */
    public boolean isErr() {
        return !isOk;
    }

    /**
     * Returns true if the result is Err and the value inside of it matches a predicate.
     *
     * @param predicate predicated tested against the error if the result is Err.
     * @return true if the result is Err and the value inside of it matches a predicate.
     */
    public boolean isErrAnd(Predicate<E> predicate) {
        return !isOk && predicate.test(error);
    }

    /**
     * Converts from org.jorge_rico_vivas.rust.Result&lt;T, E&gt; to org.jorge_rico_vivas.rust.Option&lt;T&gt;.
     *
     * @return org.jorge_rico_vivas.rust.Option containing value if org.jorge_rico_vivas.rust.Result is Ok, empty otherwise.
     */
    public Option<T> ok() {
        return isOk ? Option.some(value) : Option.empty();
    }

    /**
     * Converts from org.jorge_rico_vivas.rust.Result&lt;T, E&gt; to org.jorge_rico_vivas.rust.Option&lt;E&gt;.
     *
     * @return org.jorge_rico_vivas.rust.Option containing error if org.jorge_rico_vivas.rust.Result is Err, empty otherwise.
     */
    public Option<E> err() {
        return !isOk ? Option.some(error) : Option.empty();
    }

    /**
     * Maps a org.jorge_rico_vivas.rust.Result&lt;T, E&gt; to org.jorge_rico_vivas.rust.Result&lt;U, E&gt; by applying a function to a contained Ok value, leaving an Err
     * value untouched.
     * <p>
     * This function can be used to compose the results of two functions.
     *
     * @param mapper Maps the original value to another value.
     * @param <U>    Type T transforms to.
     * @return org.jorge_rico_vivas.rust.Result&lt;T, E&gt; transformed to org.jorge_rico_vivas.rust.Result&lt;U, E&gt;, where T transformed into U using mapper.
     */
    public <U> Result<U, E> map(Function<T, U> mapper) {
        return isOk ? Result.ok(mapper.apply(value)) : Result.err(error);
    }

    /**
     * Returns the provided default (if Err), or applies a function to the contained value (if Ok).
     * <p>
     * Arguments passed to map_or are eagerly evaluated; if you are passing the result of a function call, it is
     * recommended to use map_or_else, which is lazily evaluated.
     *
     * @param defaultValue a provided default which will be returned if this org.jorge_rico_vivas.rust.Result is Err(error).
     * @param mapper       Maps the original value to another value as a return result.
     * @param <U>          Type T transforms to as a return result.
     * @return Value of the transformation from T to U if org.jorge_rico_vivas.rust.Result is Ok(value), otherwise, it returns the default
     * value.
     */
    public <U> U mapOr(U defaultValue, Function<T, U> mapper) {
        return map(mapper).unwrapOr(defaultValue);
    }

    /**
     * Maps a org.jorge_rico_vivas.rust.Result&lt;T, E&gt; to U by applying fallback function default to a contained Err value, or function
     * defaultValueGetter to a contained Ok value.
     * <p>
     * This function can be used to unpack a successful result while handling an error.
     *
     * @param defaultValue a provided supplier which results in default value which will be calculated and
     *                     returned if this org.jorge_rico_vivas.rust.Result is Err(error).
     * @param mapper       Maps the original value to another value as a return result.
     * @param <U>          Type T transforms to as a return result.
     * @return Value of the transformation from T to U if org.jorge_rico_vivas.rust.Result is Ok(value), otherwise, it calculates and returns
     * the default value from the supplier.
     */
    public <U> U mapOrElse(Supplier<U> defaultValue, Function<T, U> mapper) {
        return map(mapper).unwrapOr(defaultValue.get());
    }

    /**
     * Maps a org.jorge_rico_vivas.rust.Result&lt;T, E&gt; to org.jorge_rico_vivas.rust.Result&lt;T, F&gt; by applying a function to a contained Err value, leaving an Ok value untouched.
     * <p>
     * This function can be used to pass through a successful result while handling an error.
     *
     * @param error_mapper Maps the original error to another error.
     * @param <O>          Type the error E transforms to.
     * @return org.jorge_rico_vivas.rust.Result&lt;T, E&gt; transformed to org.jorge_rico_vivas.rust.Result&lt;T, O&gt;, where the error E is transformed into O using error_mapper.
     */
    public <O> Result<T, O> mapError(Function<E, O> error_mapper) {
        return !isOk ? Result.err(error_mapper.apply(error)) : Result.ok(value);
    }

    /**
     * Calls the provided {@link Consumer}  on the contained value (if Ok).
     *
     * @param inspector consumer function to trigger on the contained value (if Ok).
     */
    public void inspect(Consumer<T> inspector) {
        if (isOk) {
            inspector.accept(value);
        }
    }

    /**
     * Calls the provided consumer function on the contained error (if Err).
     *
     * @param inspector consumer function to trigger on the contained error (if Err).
     */
    public void inspectErr(Consumer<E> inspector) {
        if (!isOk) {
            inspector.accept(error);
        }
    }

    /**
     * Returns the contained Ok value.
     * <p>
     * Because this function may throw a RuntimeException, its use is generally discouraged. Instead, prefer to use
     * pattern matching and handle the Err case explicitly, or call either unwrap_or or unwrap_or_else.
     *
     * @return the contained Ok value.
     * @throws RuntimeException if the value is an Err, with an error message provided by the Err’s value.
     */
    public T unwrap() throws RuntimeException {
        if (isOk) {
            return value;
        } else {
            throw new RuntimeException("called `org.jorge_rico_vivas.rust.Result::unwrap()` on an `Err` value");
        }
    }

    /**
     * Returns the contained Ok value.
     * <p>
     * Because this function may throw a RuntimeException, its use is generally discouraged. Instead, prefer to use
     * pattern matching and handle the Err case explicitly, or call unwrap_or, unwrap_or_else, or unwrap_or_default.
     *
     * @param errorMessage Error message to include on the Runtime Exception on case it is triggered.
     * @return the contained Ok value.
     * @throws RuntimeException if the value is an Err, the message error will include an error message provided by the
     *                          Err’s value and the passed error message.
     */
    public T expect(String errorMessage) throws RuntimeException {
        if (isOk) {
            return value;
        } else {
            throw new RuntimeException(errorMessage + System.lineSeparator() + "called `org.jorge_rico_vivas.rust.Result::unwrap()` on an `Err` value");
        }
    }

    /**
     * Returns the contained Err value.
     *
     * @param errorMessage Error message to include on the Runtime Exception on case it is triggered.
     * @return the contained Err value.
     * @throws RuntimeException if the value is an Ok, with an error message which includes the passed message.
     */
    public E expectErr(String errorMessage) throws RuntimeException {
        if (!isOk) {
            return error;
        } else {
            throw new RuntimeException(errorMessage + System.lineSeparator() + "called `org.jorge_rico_vivas.rust.Result::unwrap()` on an `Ok` value");
        }
    }

    /**
     * Returns the contained Err value.
     *
     * @return the contained Err value.
     * @throws RuntimeException if the value is Ok(value).
     */
    public E unwrapErr() throws RuntimeException {
        if (!isOk) {
            return error;
        } else {
            throw new RuntimeException("called `org.jorge_rico_vivas.rust.Result::unwrap_err()` on an `Ok` value");
        }
    }

    /**
     * Returns the contained Ok value or a provided default.
     * <p>
     * Arguments passed to unwrap_or are eagerly evaluated; if you are passing the result of a function call, it is
     * recommended to use unwrap_or_else, which is lazily evaluated.
     *
     * @param defaultValue a provided default which will be returned if this org.jorge_rico_vivas.rust.Result is Err(error).
     * @return contained value if org.jorge_rico_vivas.rust.Result is Ok(value), otherwise, returns the default value.
     */
    public T unwrapOr(T defaultValue) {
        return isOk ? value : defaultValue;
    }

    /**
     * Returns the contained Ok value or computes it from a {@link Supplier}.
     *
     * @param defaultValue a provided default value getter whose value will be calculated and returned if this org.jorge_rico_vivas.rust.Result is
     *                     Err(error).
     * @return contained value if org.jorge_rico_vivas.rust.Result is Ok(value), otherwise, calculates and returns the default value from the
     * supplier.
     */
    public T unwrapOrElse(Supplier<T> defaultValue) {
        return isOk ? value : defaultValue.get();
    }

    /**
     * Returns res if the result is Ok, otherwise returns the Err value of this org.jorge_rico_vivas.rust.Result.
     * <p>
     * Arguments passed to and are eagerly evaluated; if you are passing the result of a function call, it is
     * recommended to use and_then, which is lazily evaluated.
     *
     * @param res The other org.jorge_rico_vivas.rust.Result whose contents are returned if this org.jorge_rico_vivas.rust.Result is Ok(value).
     * @param <U> Value type of the latter org.jorge_rico_vivas.rust.Result.
     * @return res if the result is Ok, otherwise returns the Err value of this org.jorge_rico_vivas.rust.Result.
     */
    public <U> Result<U, E> and(Result<U, E> res) {
        return isOk ? res : Result.err(error);
    }

    /**
     * Calls op if the result is Ok, otherwise returns the Err value of this org.jorge_rico_vivas.rust.Result.
     * <p>
     * This function can be used for control flow based on org.jorge_rico_vivas.rust.Result values.
     *
     * @param res Generates a org.jorge_rico_vivas.rust.Result&lt;U,E&gt; from the T value.
     * @param <U> Value type of the produced org.jorge_rico_vivas.rust.Result if this org.jorge_rico_vivas.rust.Result is Ok.
     * @return org.jorge_rico_vivas.rust.Result with mapped value if org.jorge_rico_vivas.rust.Result was Ok, otherwise returns the Err value of this org.jorge_rico_vivas.rust.Result.
     */
    public <U> Result<U, E> andThen(Function<T, Result<U, E>> res) {
        return isOk ? res.apply(value) : Result.err(error);
    }

    /**
     * Returns res if the result is Err, otherwise returns the Ok value of this org.jorge_rico_vivas.rust.Result.
     * <p>
     * Arguments passed to or are eagerly evaluated; if you are passing the result of a function call, it is recommended
     * to use or_else, which is lazily evaluated.
     *
     * @param res The other org.jorge_rico_vivas.rust.Result whose contents are returned if this org.jorge_rico_vivas.rust.Result is Err(error).
     * @param <O> Error type of the latter org.jorge_rico_vivas.rust.Result.
     * @return res if the result is Err, otherwise returns the Ok value of this org.jorge_rico_vivas.rust.Result.
     */
    public <O> Result<T, O> or(Result<T, O> res) {
        return isOk ? Result.ok(value) : res;
    }

    /**
     * Calls op if the result is Err, otherwise returns the Ok value of self.
     * <p>
     * This function can be used for control flow based on result values.
     *
     * @param resGetter Generates a org.jorge_rico_vivas.rust.Result&lt;T,O&gt; from the E error.
     * @param <O>       Error type of the latter org.jorge_rico_vivas.rust.Result.
     * @return org.jorge_rico_vivas.rust.Result with mapped error if org.jorge_rico_vivas.rust.Result was Err, otherwise returns the Ok value of this org.jorge_rico_vivas.rust.Result.
     */
    public <O> Result<T, O> orElse(Function<E, Result<T, O>> resGetter) {
        return isOk ? Result.ok(value) : resGetter.apply(error);
    }

}
