package com.carrier.connect.observer.interfaces;

import com.carrier.connect.observer.interfaces.objects.ThreadPoolStatus;

public interface ThreadPoolObserver {
    /**
     * 当线程池状态发生变化时，该方法将被调用。
     *
     * @param threadPoolStatus 线程池状态对象，包含了线程池的相关信息。
     */
    void update(ThreadPoolStatus threadPoolStatus);
}
