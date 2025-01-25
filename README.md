# TetTicket

TetTicket is an online ticket management platform designed for Tet events. This project aims to high traffic system.

## 🚀 Key Features

- Event Management:
  - Add, edit, and delete events.
  - View detailed event information.
- Ticket Management:
  - Create and manage ticket types (e.g., regular, VIP, combo).
  - Monitor ticket availability.
- User Support:
  - Register and log in (with OAuth integration).
  - Purchase tickets and make online payments.
  - Send transaction confirmation and e-tickets via email.
- Reports and Analytics:
  - View event revenue statistics.
  - Generate reports on tickets sold.

## 🛠️ Technology Stack

### Backend:
- **Language**: Java 23
- **Framework**: Spring Boot (Domain Driven Design)
- **Database**:
  - MySQL: Used for storing user, event, and ticket data.
  - Redis: Used for temporary storage of shopping cart data and caching.
- **Security**: JWT Authentication.

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
   cd TetTicket/xxx.com/environtment
   ```

2. Start the backend services with Docker Compose:
   ```bash
   docker-compose up -d
   ```


3. Access the application at `http://localhost:1212`.

## 🗂️ Project Structure

```plaintext
TetTicket/
├── xxx.com/                # Backend with DDD
│   ├── xxx-application/       
│   ├── xxx-domain/     
│   ├── xxx-infrastructure/     
|   ├── xxx-controller/ 
│   └── xxx-start
|    ├── environtment/               # Data project
|        ├── environtment/docker-conpose-dev.yml      # Docker Compose configuration
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

