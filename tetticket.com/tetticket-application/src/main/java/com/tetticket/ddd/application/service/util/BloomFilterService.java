package com.tetticket.ddd.application.service.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class BloomFilterService {

    private final RedissonClient redissonClient;

    private static final String USERNAME_FILTER_NAME = "usernameFilter";

    private static final long EXPECTED_INSERTIONS = 1000000;

    private static final double FALSE_POSITIVE_PROBABILITY = 0.01;

    private static final long EXPIRE_TIME = 24; // 1 day in seconds

    private static final TimeUnit TIME_UNIT = TimeUnit.HOURS;
    /**
     * Retrieves the Bloom filter used for managing usernames. The filter is initialized if it is not already.
     *
     * @return the RBloomFilter instance configured for username management.
     */
    public RBloomFilter<String> getUsernameFilter() {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(USERNAME_FILTER_NAME);

        if (bloomFilter.tryInit(EXPECTED_INSERTIONS, FALSE_POSITIVE_PROBABILITY)) {
            // Set expiration only when the filter is newly initialized
            bloomFilter.expire(EXPIRE_TIME, TIME_UNIT);
            log.info("Initialized new Bloom filter with TTL of {} {}", EXPIRE_TIME, TIME_UNIT);
        }

        return bloomFilter;
    }
    /**
     * Adds a username to the Bloom filter for tracking purposes.
     *
     * @param username the username to be added to the Bloom filter
     */
    public void addUsernameToFilter(String username) {
        RBloomFilter<String> filter = getUsernameFilter();
        filter.add(username);
        // Reset expiration time after each add operation
        filter.expire(EXPIRE_TIME, TIME_UNIT);
    }
    /**
     * Checks if a username might exist in the Bloom filter.
     *
     * @param username the username to check for existence in the Bloom filter
     * @return true if the username might exist, false otherwise
     */
    public boolean mightExist(String username) {
        return getUsernameFilter().contains(username);
    }

}
