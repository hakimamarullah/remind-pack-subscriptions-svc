package com.starline.subscriptions.config;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;

public class DefaultKey implements Serializable {

    /**
     * An empty key.
     */
    public static final DefaultKey EMPTY = new DefaultKey();


    private final Object[] params;

    // Effectively final, just re-calculated on deserialization
    private transient int hashCode;


    /**
     * Create a new {@link org.springframework.cache.interceptor.SimpleKey} instance.
     * @param elements the elements of the key
     */
    public DefaultKey(Object... elements) {
        Assert.notNull(elements, "Elements must not be null");
        this.params = elements.clone();
        // Pre-calculate hashCode field
        this.hashCode = Arrays.deepHashCode(this.params);
    }


    @Override
    public boolean equals(@Nullable Object other) {
        return (this == other || (other instanceof DefaultKey that && Arrays.deepEquals(this.params, that.params)));
    }

    @Override
    public final int hashCode() {
        // Expose pre-calculated hashCode field
        return this.hashCode;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(this.params);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        // Re-calculate hashCode field on deserialization
        this.hashCode = Arrays.deepHashCode(this.params);
    }

}

