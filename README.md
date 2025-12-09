# Adaptive Load Balancing in Java-Based Microservices Using Real-Time Metrics

> This work is done as part of my CSCE 685 (Directed Studies) coursework under the supervision of Dr. Hank, Associate Dean for Graduate Programs, CSE Department, Texas A&M University. This project implements and evaluates an **adaptive load balancing mechanism** for Java-based microservices using **real-time performance metrics** like latency, queue length, and error rates.

---

## 📚 Table of Contents

- [Overview](#overview)
- [Project Objectives](#project-objectives)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [System Design](#system-design)
- [Setup Instructions](#setup-instructions)
- [Metrics & Monitoring](#metrics--monitoring)
- [Benchmarking Strategy](#benchmarking-strategy)
- [Folder Structure](#folder-structure)
- [Author](#author)
- [License](#license)

---

## 📖 Overview

Traditional load balancing strategies like round-robin and least-connections do not consider real-time system behavior, often leading to service instance overloads and poor performance.

This project proposes an **adaptive, metrics-aware load balancer** implemented as a **custom Spring Cloud Gateway filter** that dynamically routes requests based on:

- Real-time latency
- Queue saturation
- Error rates

All services are containerized using Docker, monitored via Prometheus, and visualized in Grafana.

---

## Project Objectives

1. **Algorithm Development**  
   Create a real-time, adaptive load balancing algorithm using Java and Redis.

2. **System Integration**  
   Integrate the algorithm with a Spring Boot microservices system using Spring Cloud Gateway.

3. **Comparative Analysis**  
   Benchmark adaptive vs. static strategies (e.g., round-robin) using load tests, response time, and fault tolerance.

---

## Features

- Adaptive routing logic based on real-time metrics
- Prometheus integration with Micrometer for collecting:
  - Latency
  - Queue length
  - Error counts
- Redis-based centralized metric cache for fast decision-making
- Grafana dashboards for monitoring
- Docker-based multi-container deployment
- CRON/Auto scheduler to simulate ongoing development (optional)

---

## 🛠Technology Stack

| Component          | Technology           |
|--------------------|----------------------|
| Language           | Java 17              |
| Framework          | Spring Boot 3.x, Spring Cloud Gateway |
| Metrics Collection | Micrometer + Prometheus |
| Monitoring         | Grafana              |
| Caching Layer      | Redis                |
| Containerization   | Docker + Compose     |
| Visualization      | Grafana              |
| Workflow Automation| n8n / GitHub Actions (optional) |

---

## System Design

- **Gateway**: Spring Cloud Gateway with a **custom `GlobalFilter`** for intelligent routing
- **Microservices**: RESTful services exposing their own metrics via `/actuator/prometheus`
- **Redis**: Stores latest service metrics (updated periodically)
- **Scheduler**: Background job collects metrics every few seconds
- **Prometheus**: Scrapes all services and exports data to Grafana

---

## Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/tamu-edu-students/adaptive-load_balancing.git
   cd adaptive-load_balancing
   ```

2. **Build the Project**
   ```bash
   ./mvnw clean install
   ```

   Or, if using system Maven:
   ```bash
   mvn clean install
   ```

3. **Run the Project with Docker Compose**
   ```bash
   docker-compose up --build
   ```

4. **Access Running Services**
   - **Gateway**: [http://localhost:8080](http://localhost:8080)
   - **Prometheus**: [http://localhost:9090](http://localhost:9090)
   - **Grafana**: [http://localhost:3000](http://localhost:3000)  
     *(Default login: `admin` / `admin`)*

---

## Metrics & Monitoring

Prometheus scrapes metrics from:
- Gateway
- Service A
- Service B

Available metrics:
- `http_server_requests_seconds`
- `service_a_queue_length`
- `service_b_error_count`

Grafana dashboards visualize:
- Request latency trends
- Per-service queue lengths
- Error rates over time
- Adaptive routing decisions

---

## Benchmarking Strategy

To compare **adaptive vs. static load balancing**:
- Simulate load using JMeter or Apache Bench
- Measure response time, throughput, 5xx error rates
- Analyze metrics in Grafana
- Switch between:
  - Round-robin (baseline)
  - Custom adaptive logic

---

## Folder Structure

```
adaptive-load-balancer/
├── src/
│   ├── main/java/com/example
│   │   ├── controller/       ← Service endpoints
│   │   ├── filter/           ← Adaptive filter logic
│   │   ├── scheduler/        ← Metric collector job
│   │   └── metrics/          ← Metric aggregation service
│   └── resources/
│       └── application.yml
├── Dockerfile
├── docker-compose.yml
├── prometheus.yml
└── README.md
```

---

## Author

**Vinayaka Hegde**  
CSE Graduate Student
Department of Computer Science, TAMU
Email: `vinayakah@tamu.edu`  

---

## License

This project is licensed under the MIT License.

---

## Contributions

Contributions, ideas, and suggestions are welcome!  
Feel free to fork the repo, create a PR, or open an issue.
