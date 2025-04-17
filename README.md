# TetTicket

`TetTicket is an online ticket management platform designed for Tet events. This project aims to high traffic system.`

❗Note: this is a personal project on my career journey to become a Senior Backend Developer. So this project probably won't be the most optimal because I'm working on it and studying at the same time.

![GitHub license](https://img.shields.io/github/license/TaiTitans/TetTicket) ![GitHub stars](https://img.shields.io/github/stars/TaiTitans/TetTicket) ![GitHub forks](https://img.shields.io/github/forks/TaiTitans/TetTicket)
## 🚀 Key Features
### Distributed systems are subject to high loads >20,000 req/s
![{126430C9-ACE5-414A-90F1-42EC96697A47}](https://github.com/user-attachments/assets/cc13bf64-4c54-4a82-b5b1-47e1db33c496)

-> Defense Line 1: RateLimiter - Circuit Breaker (Resilience4j)

-> Defense Line 2: Distributed Cache -> Redisson.

-> Defense Line 3: Local Cache (Guava)

---
**Deploy**: Docker Compose

**Logging**: Prometheus + Grafana (Spring Boot, MySQL, Redis, Node Exporter)

**Stack**: ELK - Elasticsearch - LogStash - Kibana

**Testing Performance**: Vegeta, WRK.

Setup Proxy **Nginx for Load Balancer**

*Expected to be implement.*:

**Message Queue**: Kafka Cluster.

**Keycloak** for **Authentication and Authorization**.
...
## 🛠️ Technology Stack

### Backend:
- **Language**: Java 23
- **Framework**: Spring Boot (Domain Driven Design)
- **Database**:
  - MySQL: Used for storing user, event, and ticket data.
  - Redis: Used for temporary storage of caching.
- **Security**: Keycloak for Authentication and Authorization.

### Deployment:
- **Docker**: Containerized services.
- **Prometheus**: Tracing.
- **Grafana**: Logging.

## ⚙️ Installation and Setup

### System Requirements
- [Java 23](https://www.oracle.com/java/technologies/downloads/#java23)
- [Docker](https://www.docker.com/)

### Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/TaiTitans/TetTicket.git
   cd TetTicket/tetticket.com/environtment
   ```

2. Start the backend services with Docker Compose:
   ```bash
   docker-compose up -d
   ```


3. Access the application at `http://localhost:1212`.

## 🗂️ Project Structure

```plaintext
TetTicket/
├── tetticket.com/                # Backend with DDD
│   ├── tetticket-application/       
│   ├── tetticket-domain/     
│   ├── tetticket-infrastructure/     
|   ├── tetticket-controller/ 
│   └── tetticket-start
|    ├── environtment/               # Data project
|        ├── environtment/docker-conpose-dev.yml    # Docker Compose configuration 
|        ├── environtment/docker-conpose-broker-kafka.yml  
└── README.md               # Project documentation
```

## 📚 Usage Guide

1. Register an account or log in.
2. Browse the list of events and select one you want to join.
3. Choose ticket type, quantity, and proceed with payment.
4. Check your email for the e-ticket.

## ✨ Contribution

If you'd like to contribute to the project:
1. Fork this repository.
2. Create a new branch:
   ```bash
   git checkout -b feature/feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m "Short description of your changes"
   ```
4. Push your branch:
   ```bash
   git push origin feature/feature-name
   ```
5. Open a Pull Request.

## 📄 License

This project is licensed under the [MIT License](LICENSE).

## 🌐 Contact

- **Author**: [TaiTitans](https://github.com/TaiTitans)
- **Email**: taititansofficial@gmail.com

