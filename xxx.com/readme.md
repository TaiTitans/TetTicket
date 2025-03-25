### Distributed systems are subject to high loads >100,000 req/s

-> Defense Line 1: RateLimiter - Circuit Breaker (Resilience4j)

-> Defense Line 2: Distributed Cache -> Redisson. + Redis Lock

-> Defense Line 3: Local Cache (Guava)

### Get Ticket Detail

a. Khi user lấy dữ liệu lần đầu
    
Không tìm thấy trong local cache.

Không tìm thấy trong Redis cache.

Truy xuất từ cơ sở dữ liệu, sau đó lưu vào Redis và local cache.

b. Khi user lấy dữ liệu với phiên bản không khớp
    
Dữ liệu từ local cache bị lỗi thời (phiên bản thấp hơn).

Truy xuất từ Redis cache.

Cập nhật lại dữ liệu vào local cache.

c. Khi user đặt vé (ghi dữ liệu)
    
Xóa dữ liệu khỏi local cache và Redis cache.

Dữ liệu mới sẽ được cập nhật khi user lấy lại thông tin.

Setup Circuit Breaker để phòng khi redis hoặc db gặp sự cố.

Khi lấy dữ liệu từ db buộc sử dụng Lock để đảm bảo chỉ 1 instance query db.


---
Deploy: Docker Compose

Logging: Prometheus + Grafana (Spring Boot, MySQL, Redis, Node Exporter)
LOGs: ELK - Elasticsearch - LogStash - Kibana
Testing Performance: Vegeta, WRK.

Setup Proxy Nginx for Load Balancing

---
### StockDeduction
⚠️ Warning if dont solve problem:

1. Over Selling (vé trong kho hết người bên ngoài báo còn)

2. Under Selling (vé trong kho còn nhưng bên ngoài đã báo hết)

3. Bottleneck 

=> Xài Service + DB dành riêng cho ngày BlackFriday.

=> Lưu số lượng lên cache (FE lưu lên CDN).-> Sau đó trừ vào DB sau (nguy cơ mất dữ liệu),(Sử dụng LUA tối ưu hơn GET,SET, INCR, DECR)

=> Sử dụng CAS cho câu UPDATE hoặc POST trong MySQL
