package io.vepo.clone;

public abstract class CloneFactory {
    public static CloneFactory eager() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static CloneFactory lazy() {
        return new LazyCloneFactory();
    }

    CloneFactory() {
    }

    public abstract <T> T clone(T obj);

    public abstract <T> T[] clone(T[] obj);
}
