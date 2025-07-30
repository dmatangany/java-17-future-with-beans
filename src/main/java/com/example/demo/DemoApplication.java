package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.*;
import java.util.function.Supplier;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //runWhileFunction();
        runFutureFunction();
        System.out.println("Global thread ends.");
    }

    public  void runWhileFunction() throws Exception {
        System.out.println("runWhileFunction starts.");
        String test = "";
        try {
            System.out.println("...WAITING - BLOCKING");
            while(test.length()<1000000000){
                test = test.concat("a");
            }
            System.out.println("...WAIT-OVER");
        } finally {

        }
        while(test.length()<10000000){
            Thread.sleep(100);
            System.out.println("WHILE: Other tasks can execute here: " + test.length());
        }
    }

    public  void runFutureFunction() throws Exception {
        CompletableFuture<Boolean> future = checkState();
        System.out.println("runFutureFunction starts.");
        future.thenAccept(result -> {System.out.println("Result: " + result);});
        while (!future.isDone()) {
            Thread.sleep(100);
            System.out.println("FUTURE: Other tasks can execute here.");
        }
    }

    public CompletableFuture<Boolean> checkState() throws Exception {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        Supplier<Boolean> supplier = () -> {
            // Simulate some work
            boolean testState = (Boolean) false;
            System.out.println("Starting future in thread: " + Thread.currentThread().getName());
            try {
				String test = "";
                System.out.println("...WAITING: NON BLOCKING");
                while(test.length()<1000000){
					test = test.concat("a");
                    System.out.println("...PROCESSING: NON BLOCKING: "  +test.length());
				}
                System.out.println("...WAIT-OVER");
            } finally {
                return (Boolean) testState;
            }
        };
        System.out.println("ACTIVATE future in thread: " + Thread.currentThread().getName());
        future.completeAsync(supplier);
        return future;
    }
}
