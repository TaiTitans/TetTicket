package com.tetticket.ddd.infrastructure.distributed.redisson;

public interface RedisDistributedService {
RedisDistributedLocker getDistributedLock(String lockKey);
}
