### Distributed systems are subject to high loads >100,000 req/s

-> Defense Line 1: RateLimiter - Circuit Breaker (Resilience4j)

-> Defense Line 2: Distributed Cache -> Redisson.

-> Defense Line 3: Local Cache (Guava)



---
Deploy: Docker Compose

Logging: Prometheus + Grafana (Spring Boot, MySQL, Redis, Node Exporter)
LOGs: ELK - Elasticsearch - LogStash - Kibana
Testing Performance: Vegeta, WRK.

Setup Proxy Nginx for Load Balancing