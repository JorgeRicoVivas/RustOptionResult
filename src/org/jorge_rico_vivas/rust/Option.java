package org.jorge_rico_vivas.rust;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Represents an optional value using two states: Some if containing a value, or None if there is no value.
 * <br><br>
 * This is a port and Java adaptation of the Rust's <a href="https://doc.rust-lang.org/stable/std/option/index.html">org.jorge_rico_vivas.rust.Option Type</a>.
 *
 * @param <T> Value type.
 * @author Jorge Rico Vivas
 */
public class Option<T> {

    private T value;

    /**
     * Generates an org.jorge_rico_vivas.rust.Option as Some(value).
     *
     * @param value value contained on Some(value).
     */
    public Option(T value) {
        this.value = value;
    }

    /**
     * Generates an org.jorge_rico_vivas.rust.Option as Some(value).
     *
     * @param value value contained on Some(value).
     * @param <T>   Value type.
     * @return org.jorge_rico_vivas.rust.Option as Some(value).
     */
    public static <T> Option<T> some(T value) {
        return new Option<>(value);
    }

    /**
     * Generates an org.jorge_rico_vivas.rust.Option as None.
     *
     * @param <T> Value type.
     * @return org.jorge_rico_vivas.rust.Option as None.
     */
    public static <T> Option<T> empty() {
        return new Option<>(null);
    }

    /**
     * Returns true if the option is a Some value.
     *
     * @return true if the option is a Some value.
     */
    public boolean isSome() {
        return value != null;
    }

    /**
     * Returns true if the option is a Some and the value inside of it matches a predicate.
     *
     * @param predicate predicated tested against the value if value is Some.
     * @return true if the option is a Some and the value inside of it matches a predicate.
     */
    public boolean isSomeAnd(Predicate<T> predicate) {
        return value != null && predicate.test(value);
    }

    /**
     * Returns true if the option is a None value.
     *
     * @return true if the option is a None value.
     */
    public boolean isNone() {
        return value == null;
    }

    /**
     * Maps an org.jorge_rico_vivas.rust.Option&lt;T&gt; to org.jorge_rico_vivas.rust.Option&lt;U&gt; by applying a function to a contained value (if Some) or returns None (if None).
     *
     * @param mapper Maps the original value to another value.
     * @param <U>    Type T transforms to.
     * @return org.jorge_rico_vivas.rust.Option&lt;T&gt; transformed to org.jorge_rico_vivas.rust.Option&lt;U&gt;, where T transformed into U using mapper.
     */
    public <U> Option<U> map(Function<T, U> mapper) {
        return value != null ? Option.some(mapper.apply(value)) : Option.empty();
    }

    /**
     * Returns the provided default result (if none), or applies a function to the contained value (if any).
     * <p>
     * Arguments passed to mapOr are eagerly evaluated; if you are passing the result of a function call, it is
     * recommended to use mapOrElse, which is lazily evaluated.
     *
     * @param defaultValue a provided default which will be returned if this org.jorge_rico_vivas.rust.Option is None.
     * @param mapper       Maps the original value to another value as a return result.
     * @param <U>          Type T transforms to as a return result.
     * @return Value of the transformation from T to U if org.jorge_rico_vivas.rust.Option is Some(value), otherwise, it returns the default
     * value.
     */
    public <U> U mapOr(U defaultValue, Function<T, U> mapper) {
        return map(mapper).unwrapOr(defaultValue);
    }

    /**
     * Computes a default function result (if none), or applies a different function to the contained value (if any).
     *
     * @param defaultValue a provided supplier which results in default value which will be calculated and returned if this org.jorge_rico_vivas.rust.Option is None.
     * @param mapper       Maps the original value to another value as a return result.
     * @param <U>          Type T transforms to as a return result.
     * @return Value of the transformation from T to U if org.jorge_rico_vivas.rust.Option is Some(value), otherwise, it calculates and returns
     * the default value from the supplier.
     */
    public <U> U mapOrElse(Supplier<U> defaultValue, Function<T, U> mapper) {
        return map(mapper).unwrapOr(defaultValue.get());
    }

    /**
     * Transforms the org.jorge_rico_vivas.rust.Option&lt;T&gt; into a org.jorge_rico_vivas.rust.Result&lt;T, E&gt;, mapping Some(v) to Ok(v) and None to Err(err).
     * <p>
     * Arguments passed to okOr are eagerly evaluated; if you are passing the result of a function call, it is
     * recommended to use okOrElse, which is lazily evaluated.
     *
     * @param error error to transform into org.jorge_rico_vivas.rust.Result.Error if this org.jorge_rico_vivas.rust.Option is None
     * @param <E>   Error type parameter.
     * @return org.jorge_rico_vivas.rust.Result.Ok(value) if this option is Some(value), otherwise it returns org.jorge_rico_vivas.rust.Result.Error(error), where error is a parameter.
     */
    public <E> Result<T, E> okOr(E error) {
        return value != null ? Result.ok(value) : Result.err(error);
    }

    /**
     * Transforms the org.jorge_rico_vivas.rust.Option&lt;T&gt; into a org.jorge_rico_vivas.rust.Result&lt;T, E&gt;, mapping Some(v) to Ok(v) and None to Err(err()).
     *
     * @param error error to transform into org.jorge_rico_vivas.rust.Result.Error if this org.jorge_rico_vivas.rust.Option is None, this is a {@link Supplier}, meaning it
     *              is only calculated if this org.jorge_rico_vivas.rust.Option is None.
     * @param <E>   Error type parameter.
     * @return org.jorge_rico_vivas.rust.Result.Ok(value) if this option is Some(value), otherwise it returns org.jorge_rico_vivas.rust.Result.Error(error), where error is
     * a parameter whose value gets calculated (Only if org.jorge_rico_vivas.rust.Option is None).
     */
    public <E> Result<T, E> okOrElse(Supplier<E> error) {
        return value != null ? Result.ok(value) : Result.err(error.get());
    }

    /**
     * Calls the provided {@link Consumer} on the contained value (if Some).
     *
     * @param inspector consumer function to trigger on the contained value (if Some).
     */
    public void inspect(Consumer<T> inspector) {
        if (value != null) {
            inspector.accept(value);
        }
    }

    /**
     * Returns the contained Some value.
     * <p>
     * Because this function may throw a RuntimeException, its use is generally discouraged. Instead, prefer to use
     * pattern matching and handle the None case explicitly, or call either unwrap_or or unwrap_or_else.
     *
     * @return the contained Some value.
     * @throws RuntimeException if the value is None.
     */
    public T unwrap() throws RuntimeException {
        if (value != null) {
            return value;
        } else {
            throw new RuntimeException("called `org.jorge_rico_vivas.rust.Option::unwrap()` on a `None` value");
        }
    }

    /**
     * Returns the contained Some value.
     * <p>
     * Throws a RuntimeException if the value is a None with a custom panic message provided by errorMessage.
     *
     * @param errorMessage Error message to include on the Runtime Exception on case it is triggered.
     * @return the contained Some value
     * @throws RuntimeException if the value is a None, the message error will include an error message provided and the
     *                          passed error message.
     */
    public T expect(String errorMessage) throws RuntimeException {
        if (value != null) {
            return value;
        } else {
            throw new RuntimeException(errorMessage + System.lineSeparator() + "called `org.jorge_rico_vivas.rust.Option::unwrap()` on a `None` value");
        }
    }

    /**
     * Returns the contained Some value or a provided default.
     * <p>
     * Arguments passed to unwrapOr are eagerly evaluated; if you are passing the result of a function call, it is
     * recommended to use unwrapOrElse, which is lazily evaluated.
     *
     * @param defaultValue a provided default which will be returned if this org.jorge_rico_vivas.rust.Option is None.
     * @return the contained Some value or a provided default.
     */
    public T unwrapOr(T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * Returns the contained Some value or computes it from a {@link Supplier}.
     *
     * @param defaultValue a provided default value getter whose value will be calculated and returned if this org.jorge_rico_vivas.rust.Option is
     *                     None.
     * @return contained value if org.jorge_rico_vivas.rust.Option is Some(value), otherwise, calculates and returns the default value from the
     * supplier.
     */
    public T unwrapOrElse(Supplier<T> defaultValue) {
        return value != null ? value : defaultValue.get();
    }

    /**
     * Returns None if the option is None, otherwise calls predicate with the wrapped value and returns:
     * <p>
     * - Some(T) if predicate returns true (where T is the wrapped value).
     * - None if predicate returns false.
     * <p>
     * This function works similar to Rust's Iterator::filter(). You can imagine the org.jorge_rico_vivas.rust.Option&lt;T&gt; being an iterator over
     * one or zero elements. filter() lets you decide which elements to keep.
     *
     * @param predicate Condition this Some(value) has to match in order to return itself
     * @return returns this if is Some(value) and the value matches the predicate, otherwise, it returns None.
     */
    public Option<T> filter(Predicate<T> predicate) {
        return isNone() || !predicate.test(value) ? Option.empty() : this;
    }

    /**
     * Returns None if the option is None, otherwise returns res.
     * <p>
     * Arguments passed to and are eagerly evaluated; if you are passing the result of a function call, it is
     * recommended to use and_then, which is lazily evaluated.
     *
     * @param res The other org.jorge_rico_vivas.rust.Option whose contents are returned if this org.jorge_rico_vivas.rust.Option is Some.
     * @param <U> Value type of the latter org.jorge_rico_vivas.rust.Option.
     * @return res if the org.jorge_rico_vivas.rust.Option is Some, otherwise None.
     */
    public <U> Option<U> and(Option<U> res) {
        return value != null ? res : Option.empty();
    }

    /**
     * Returns None if the option is None, otherwise calls f with the wrapped value and returns the result.
     * <p>
     * Some languages call this operation flatmap.
     *
     * @param res Generates an org.jorge_rico_vivas.rust.Option&lt;U&gt; from the T value.
     * @param <U> Value type of the produced org.jorge_rico_vivas.rust.Option if this org.jorge_rico_vivas.rust.Option is Some.
     * @return org.jorge_rico_vivas.rust.Result with mapped value if org.jorge_rico_vivas.rust.Option was Some, otherwise None.
     */
    public <U> Option<U> andThen(Function<T, Option<U>> res) {
        return value != null ? res.apply(value) : Option.empty();
    }

    /**
     * Returns the option if it contains a value, otherwise returns res.
     * <p>
     * Arguments passed to or are eagerly evaluated; if you are passing the result of a function call, it is recommended
     * to use orElse, which is lazily evaluated.
     *
     * @param res The other org.jorge_rico_vivas.rust.Option whose contents are returned if this org.jorge_rico_vivas.rust.Option is None.
     * @return This option if it contains a value, otherwise returns res.
     */
    public Option<T> or(Option<T> res) {
        return value != null ? this : res;
    }

    /**
     * Returns the option if it contains a value, otherwise calls the {@link Supplier} and returns the result.
     *
     * @param res Supplier resolving in another org.jorge_rico_vivas.rust.Option whose contents are returned if this org.jorge_rico_vivas.rust.Option is None.
     * @return this option if it contains a value, otherwise calls {@link Supplier} and returns the result.
     */
    public Option<T> orElse(Supplier<Option<T>> res) {
        return value != null ? this : res.get();
    }

    /**
     * Returns Some if exactly one of self, res is Some, otherwise returns None.
     *
     * @param res The other org.jorge_rico_vivas.rust.Option whose contents are returned if this org.jorge_rico_vivas.rust.Option is None and res is Some.
     * @return Some if exactly one of self, res is Some, otherwise returns None.
     */
    public Option<T> xor(Option<T> res) {
        if (value != null && res.isNone()) {
            return this;
        } else {
            return value == null && res.value != null ? res : Option.empty();
        }
    }

    /**
     * Inserts value into the option, then returns a reference to it.
     * <p>
     * If the option already contains a value, the old value is dropped.
     * <p>
     * See also getOrInsert, which doesn't update the value if the option already contains Some.
     *
     * @param value The new value.
     * @return The new value.
     */
    public T insert(T value) {
        this.value = value;
        return this.value;
    }

    /**
     * Inserts value into the option if it is None, then returns a reference to the contained value.
     * <p>
     * See also org.jorge_rico_vivas.rust.Option::insert, which updates the value even if the option already contains Some.
     *
     * @param value The new value (if this org.jorge_rico_vivas.rust.Option is None).
     * @return The new value if this org.jorge_rico_vivas.rust.Option is None, otherwise, the old value.
     */
    public T getOrInsert(T value) {
        if (this.value == null) {
            this.value = value;
        }
        return this.value;
    }

    /**
     * Inserts a value computed from the {@link Supplier} into the option if it is None, then returns a reference to the
     * contained value.
     *
     * @param value Supplier giving the new value (if this org.jorge_rico_vivas.rust.Option is None).
     * @return The new value if this org.jorge_rico_vivas.rust.Option is None, otherwise, the old value.
     */
    public T getOrInsertWith(Supplier<T> value) {
        if (this.value == null) {
            this.value = value.get();
        }
        return this.value;
    }

    /**
     * Takes the value out of the option, leaving a None in its place.
     *
     * @return Old value (Might be null).
     */
    public T take() {
        T value = this.value;
        this.value = null;
        return value;
    }

    /**
     * Takes the value out of the option, but only if the predicate evaluates to true on the value.
     * <p>
     * In other words, replaces self with None if the predicate returns true. This method operates similar to
     * {@link Option#take()}, but conditional.
     *
     * @param predicate Predicate tested against the value (if Some), if true, it will take the value out of the option.
     * @return Old value (If matches predicate) (Might be null).
     */
    public T takeIf(Predicate<T> predicate) {
        if (value != null && predicate.test(value)) {
            T value = this.value;
            this.value = null;
            return value;
        }
        return null;
    }

    /**
     * Replaces the actual value in the option by the value given in parameter, returning the old value if present,
     * leaving a Some in its place without de-initializing either one.
     *
     * @param value new value replacing the old one.
     * @return Old value (Might be None).
     */
    public Option<T> replace(T value) {
        T oldValue = this.value;
        this.value = value;
        return new Option<>(oldValue);
    }

}
