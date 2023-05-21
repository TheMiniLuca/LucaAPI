package io.github.theminiluca.api.utils;

public abstract class Acceptor<T> implements Runnable {
    private Consumer<T> consumer;

    public Acceptor(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    public abstract T getValue();

    public void run() {
        new Thread(() -> this.consumer.accept(this.getValue())).start();
    }
}
