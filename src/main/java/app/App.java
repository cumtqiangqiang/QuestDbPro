package app;

import app.consumer.ConsumerTask;

public class App {
    public static void main(String[] args) {

        final ConsumerTask consumerTask = new ConsumerTask();
        consumerTask.run();

    }
}
