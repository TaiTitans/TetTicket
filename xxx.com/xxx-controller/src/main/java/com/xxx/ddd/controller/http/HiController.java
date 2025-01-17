package com.xxx.ddd.controller.http;

import com.xxx.ddd.application.service.event.EventAppService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HiController {
    @Autowired
    private EventAppService eventAppService;
    @GetMapping("/hi")
    @RateLimiter(name = "backendA", fallbackMethod = "fallbackHello")
    public String hello() {
      return eventAppService.sayHi("Hello World");
    }

    public String fallbackHello(Throwable throwable){
        return "Too many request";
    }
    @GetMapping("/hi/v1")
    @RateLimiter(name = "backendB", fallbackMethod = "fallbackHello")
    public String sayHi() {
        return eventAppService.sayHi("Hello VN");
    }

}
