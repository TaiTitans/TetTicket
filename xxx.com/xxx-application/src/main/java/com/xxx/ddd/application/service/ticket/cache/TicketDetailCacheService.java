package com.xxx.ddd.application.service.ticket.cache;

import com.xxx.ddd.domain.model.entity.TicketDetail;
import com.xxx.ddd.domain.service.TicketDetailDomainService;
import com.xxx.ddd.infrastructure.cache.redis.RedisInfrasService;
import com.xxx.ddd.infrastructure.distributed.redisson.RedisDistributedLocker;
import com.xxx.ddd.infrastructure.distributed.redisson.RedisDistributedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TicketDetailCacheService {
    @Autowired
    private RedisDistributedService redisDistributedService;
    @Autowired
    private RedisInfrasService redisInfrasService;
    @Autowired
    private TicketDetailDomainService ticketDetailDomainService;

    public TicketDetail getTicketDefaultCacheNormal(Long id, Long version){
        // 1. get ticket item by Redis
        TicketDetail ticketDetail = redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);
        // 2. YES -> Hit cache
        if(ticketDetail != null){
            log.info("FROM CACHE {}, {}, {}", id, version, ticketDetail);
            return ticketDetail;
        }

        // 3. If NO -> Missing cache
        // 4. Get data from DBS
        ticketDetail = ticketDetailDomainService.getTicketDetailById(id);
        log.info("FROM DBS {}, {}, {}", id, version, ticketDetail);

        // 5. check ticketItem
        if(ticketDetail != null){
            // Code nay co van de -> Gia su ticketItem lay ra tu dbs null thi sao, query mãi
            // 6. set cache
            redisInfrasService.setObject(genEventItemKey(id), ticketDetail);
        }
        return ticketDetail;
    }

    // Non VIP

    public TicketDetail getTicketDefaultCacheVip(Long id, Long version){
        log.info("Implement getTicketDefaultCacheVip->, {}, {}", id, version);
        TicketDetail ticketDetail = ticketDetailDomainService.getTicketDetailById(id);
        //redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);
        // 2. YES
        if(ticketDetail != null){
            return ticketDetail;
        }
        //        log.info("CACHE NO EXIST, START GET DB AND SET CACHE->, {}, {} ", id, version);
        // Create lock process with KEY
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock("PRO_LOCK_KEY_ITEM"+id);
        try{
            // 1 - Create lock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            // Lưu ý: Cho dù thành công hay không cũng phải unLock, bằng mọi giá.
            if(!isLock){
                return ticketDetail;
            }
            //Get cache
            ticketDetail = redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);
            // 2. YES
            if(ticketDetail != null){
                 log.info("FROM CACHE {}, {}, {}", id, version, ticketDetail);
                return ticketDetail;
            }
            // 3. Query DBS
            ticketDetail = ticketDetailDomainService.getTicketDetailById(id);
            log.info("FROM DBS -> {}, {}", ticketDetail, version);
            if(ticketDetail == null){
                log.info("TICKET NOT EXITS....{}", version);
                // set
                redisInfrasService.setObject(genEventItemKey(id), ticketDetail);
                return ticketDetail;
            }
            redisInfrasService.setObject(genEventItemKey(id),ticketDetail);
            return ticketDetail;
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            locker.unlock();
        }

    }

    private String genEventItemKey(Long itemId) {
        return "PRO_TICKET:ITEM:" + itemId;
    }
}
