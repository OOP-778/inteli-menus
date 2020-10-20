package com.oop.intelimenus.util;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InteliOptional<T> {

    private static final InteliOptional<?> EMPTY = new InteliOptional<>();
    private final T value;

    private InteliOptional() {
        this.value = null;
    }

    public static <T> InteliOptional<T> empty() {
        @SuppressWarnings("unchecked")
        InteliOptional<T> t = (InteliOptional<T>) EMPTY;
        return t;
    }

    private InteliOptional(T value) {
        this.value = Objects.requireNonNull(value);
    }

    public static <T> InteliOptional<T> of(T value) {
        return new InteliOptional<>(value);
    }

    public static <T> InteliOptional<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }

    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public OrElse<T> ifPresent(Consumer<? super T> consumer) {
        if (value != null) {
            consumer.accept(value);
            return new OrElse<>(this, true);
        } else
            return new OrElse<>(this, false);
    }

    public InteliOptional<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent())
            return this;
        else
            return predicate.test(value) ? this : empty();
    }

    public <U> InteliOptional<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent())
            return empty();
        else {
            return InteliOptional.ofNullable(mapper.apply(value));
        }
    }

    public <U> InteliOptional<U> flatMap(Function<? super T, InteliOptional<U>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent())
            return empty();
        else {
            return Objects.requireNonNull(mapper.apply(value));
        }
    }

    public T orElse(T other) {
        return value != null ? value : other;
    }

    public T orElseGet(Supplier<? extends T> other) {
        return value != null ? value : other.get();
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof InteliOptional)) {
            return false;
        }

        InteliOptional<?> other = (InteliOptional<?>) obj;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value != null
                ? String.format("InteliOptional[%s]", value)
                : "Optional.empty";
    }

    public static class OrElse<T> {
        private InteliOptional<T> optional;
        private boolean value;
        protected OrElse(InteliOptional<T> optional, boolean value) {
            this.optional = optional;
            this.value = value;
        }

        public InteliOptional<T> elseIf(Runnable runnable) {
            if (!value) {
                runnable.run();
                return empty();
            } else
                return optional;
        }
    }
}
