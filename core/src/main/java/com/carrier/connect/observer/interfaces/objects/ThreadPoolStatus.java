package com.carrier.connect.observer.interfaces.objects;

public class ThreadPoolStatus {
    private int activeThreads;
    private long completedTasks;
    private long totalTasks;
    private int pendingTasks;

    public ThreadPoolStatus(int activeThreads, long completedTasks, long totalTasks, int pendingTasks) {
        this.activeThreads = activeThreads;
        this.completedTasks = completedTasks;
        this.totalTasks = totalTasks;
        this.pendingTasks = pendingTasks;
    }

    public int getActiveThreads() {
        return activeThreads;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }

    public long getTotalTasks() {
        return totalTasks;
    }

    public int getPendingTasks() {
        return pendingTasks;
    }
}
