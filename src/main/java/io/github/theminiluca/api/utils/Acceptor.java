package io.github.theminiluca.utils;

public abstract class Acceptor<T> implements Runnable {
    private Consumer<T> consumer;

    public Acceptor(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    public abstract T getValue();

    public void run() {
        this.consumer.accept(this.getValue());
    }
}
