package com.carrier.util;

import com.carrier.connect.observer.interfaces.ThreadPoolObserver;
import com.carrier.connect.observer.interfaces.objects.ThreadPoolStatus;

import java.util.concurrent.*;

/**
 * ThreadPoolFactory 类负责根据提供的参数创建线程池。
 * 创建的线程池具有自定义的线程名和线程池观察者，以便于问题排查和扩展。
 */
public class ThreadPoolFactory {
    private final NamedThreadFactory namedThreadFactory;
    private final ThreadPoolObserver threadPoolObserver;
    private final int poolSize;
    private final int monitorIntervalSeconds;

    private ThreadPoolFactory(NamedThreadFactory namedThreadFactory, ThreadPoolObserver threadPoolObserver, int poolSize, int monitorIntervalSeconds) {
        this.namedThreadFactory = namedThreadFactory;
        this.threadPoolObserver = threadPoolObserver;
        this.poolSize = poolSize;
        this.monitorIntervalSeconds = monitorIntervalSeconds;
    }

    public static ThreadPoolFactoryBuilder builder() {
        return new ThreadPoolFactoryBuilder();
    }

    /**
     * 创建一个具有固定线程池大小的 ExecutorService。
     *
     * @return 创建的 ExecutorService
     */
    public ExecutorService createFixedThreadPool() {
        ExecutorService threadPool = Executors.newFixedThreadPool(this.poolSize, this.namedThreadFactory);

        ScheduledExecutorService monitorThreadPool = Executors.newScheduledThreadPool(1);
        monitorThreadPool.scheduleAtFixedRate(() -> {
            int activeThreads = ((ThreadPoolExecutor) threadPool).getActiveCount();
            long completedTasks = ((ThreadPoolExecutor) threadPool).getCompletedTaskCount();
            long totalTasks = ((ThreadPoolExecutor) threadPool).getTaskCount();
            int pendingTasks = (int) (totalTasks - completedTasks);

            ThreadPoolStatus threadPoolStatus = new ThreadPoolStatus(activeThreads, completedTasks, totalTasks, pendingTasks);

            this.threadPoolObserver.update(threadPoolStatus);
        }, 0, this.monitorIntervalSeconds, TimeUnit.SECONDS);

        return threadPool;
    }

    /**
     * ThreadPoolFactoryBuilder 类用于构建 ThreadPoolFactory 对象。
     */
    public static class ThreadPoolFactoryBuilder {
        private NamedThreadFactory namedThreadFactory;
        private ThreadPoolObserver threadPoolObserver;
        private int poolSize;
        private int monitorIntervalSeconds;

        public ThreadPoolFactoryBuilder withNamedThreadFactory(NamedThreadFactory namedThreadFactory) {
            this.namedThreadFactory = namedThreadFactory;
            return this;
        }

        public ThreadPoolFactoryBuilder withThreadPoolObserver(ThreadPoolObserver threadPoolObserver) {
            this.threadPoolObserver = threadPoolObserver;
            return this;
        }

        public ThreadPoolFactoryBuilder withPoolSize(int poolSize) {
            this.poolSize = poolSize;
            return this;
        }

        public ThreadPoolFactoryBuilder withMonitorIntervalSeconds(int monitorIntervalSeconds) {
            this.monitorIntervalSeconds = monitorIntervalSeconds;
            return this;
        }

        public ThreadPoolFactory build() {
            return new ThreadPoolFactory(this.namedThreadFactory, this.threadPoolObserver, this.poolSize, this.monitorIntervalSeconds);
        }
    }
}
