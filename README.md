# ✂️🪨📄 SSP - Scissors, Stone, Paper

**SSP** is a full-stack web application where users can play *Scissors, Stone, Paper*  either **against the machine** or **in real-time multiplayer mode** using WebSockets. Users also have access to a personalized statistics page to track their performance and habits.

---

## 🚀 Features

- 🎮 **Singleplayer** mode against the computer.
- 🧑‍🤝‍🧑 **Multiplayer** mode using WebSockets.
- 📊 **User statistics page**:
    - Total games played
    - Games won
    - Win/loss ratio
    - Most frequently chosen option (scissors, stone, or paper)

---

## 🛠️ Tech Stack

### Frontend
- **Angular 20**
- **PrimeNG**

### Backend
- **Spring Boot 3.5**
- **Java 21**

### Database
- **PostgreSQL 15**

### DevOps
- **Docker & Docker Compose**
- **Ngrok (for external access to multiplayer)**

---

## 🧩 Architecture

```text
[ Angular (Frontend) ] <---> [ Spring Boot (Backend) ] <---> [ PostgreSQL (DB) ]
```

---

## 🧪 Getting Started
### Prerequisites
- Docker

- Docker Compose

- [Ngrok](https://dashboard.ngrok.com) (optional for multiplayer)

---

## 🔧 Installation
### Clone the repo

```bash
git clone https://github.com/purquijo/ssp.git
cd ssp
```

### (Optional) Expose to the internet using Ngrok
```bash
ngrok http 80
```
Use the Ngrok URL to allow external players to connect to your multiplayer game. Change the app.cors.allowed-origins prop to the url provided by ngrok.


### Start the full stack
```bash
docker-compose up --build
```

---

## 🧾 Environment Configuration (.env)
This project uses a .env file to securely manage environment variables such as database credentials. These values are automatically loaded by Docker Compose when the services are started.

### 🔐 Why use .env?
- Keeps sensitive information (like passwords) out of the codebase.

- Makes the app easier to configure in different environments (local, staging, production).

- Improves security and maintainability.

### 📁 Setup
Copy the example environment file:

```bash
cp .env.example .env
```

Open .env and fill in your configuration:

```
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your-secure-password
POSTGRES_DB=gamesdb
```

## 📂 Project Structure

```text
ssp/
├── docker-compose.yml
├── .env
├── ssp-frontend/      # Angular 20 + PrimeNG
└── ssp-backend/       # Spring Boot 3.5 + Java 21
```

## 📄 License

This project is licensed under the [MIT License](LICENSE).