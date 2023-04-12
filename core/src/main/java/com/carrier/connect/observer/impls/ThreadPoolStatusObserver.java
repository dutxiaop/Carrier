package com.carrier.connect.observer.impls;

import com.carrier.connect.observer.interfaces.ThreadPoolObserver;
import com.carrier.connect.observer.interfaces.objects.ThreadPoolStatus;

public class ThreadPoolStatusObserver implements ThreadPoolObserver {
    @Override
    public void update(ThreadPoolStatus threadPoolStatus) {
        System.out.println("Thread Pool Status: ");
        System.out.println("  Active threads: " + threadPoolStatus.getActiveThreads());
        System.out.println("  Completed tasks: " + threadPoolStatus.getCompletedTasks());
        System.out.println("  Total tasks: " + threadPoolStatus.getTotalTasks());
        System.out.println("  Pending tasks: " + threadPoolStatus.getPendingTasks());
    }
}
