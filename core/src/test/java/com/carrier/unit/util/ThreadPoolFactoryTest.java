package com.carrier.unit.util;

import com.carrier.connect.observer.interfaces.ThreadPoolObserver;
import com.carrier.connect.observer.interfaces.objects.ThreadPoolStatus;
import com.carrier.util.NamedThreadFactory;
import com.carrier.util.ThreadPoolFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ThreadPoolFactoryTest {
    private ThreadPoolFactory threadPoolFactory;
    private ExecutorService executorService;

    @BeforeEach
    void setUp() {
        NamedThreadFactory namedThreadFactory = new NamedThreadFactory("Test");
        ThreadPoolObserver threadPoolObserver = new ThreadPoolObserver() {
            @Override
            public void update(ThreadPoolStatus threadPoolStatus) {
                System.out.println("ThreadPoolStatus: " + threadPoolStatus);
            }
        };

        threadPoolFactory = ThreadPoolFactory.builder()
                .withNamedThreadFactory(namedThreadFactory)
                .withThreadPoolObserver(threadPoolObserver)
                .withPoolSize(4)
                .withMonitorIntervalSeconds(2)
                .build();

        executorService = threadPoolFactory.createFixedThreadPool();
    }

    @Test
    void testExecuteTasks() {
        AtomicBoolean task1Executed = new AtomicBoolean(false);
        AtomicBoolean task2Executed = new AtomicBoolean(false);

        executorService.submit(() -> {
            task1Executed.set(true);
            System.out.println("Task 1 executed.");
        });

        executorService.submit(() -> {
            task2Executed.set(true);
            System.out.println("Task 2 executed.");
        });

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(task1Executed.get(), "Task 1 should be executed.");
        assertTrue(task2Executed.get(), "Task 2 should be executed.");
    }

    @AfterEach
    void tearDown() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
