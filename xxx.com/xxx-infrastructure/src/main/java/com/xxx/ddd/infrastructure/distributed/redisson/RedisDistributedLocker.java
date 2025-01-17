package com.xxx.ddd.infrastructure.distributed.redisson;

import java.util.concurrent.TimeUnit;

public interface RedisDistributedLocker {

    boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;

    void lock(long leaseTime, TimeUnit unit);

    void unlock();

    boolean isLocked();

    boolean isHeldbyThread(long threadId);

    boolean isHeldByCurrentThread();

}
