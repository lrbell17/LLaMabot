# 🦙 LLaMabot

**LLaMabot** is a lightweight, containerized AI chatbot built using modern backend technologies. It uses the [LLaMA model](https://ollama.com/library/llama3.2) via Ollama for local LLM inference and provides a GraphQL API for chat functionality. Users authenticate through Keycloak (SSO) using OAuth2/OpenID Connect.

---

## 🛠️ Technologies Used

- **Spring Boot** – Main backend framework
- **GraphQL (Spring for GraphQL)** – API layer for chat interactions
- **Spring AI** – LLM integration and prompt orchestration
- **Keycloak** – Authentication and identity provider (OIDC)
- **Ollama** – Local LLM
- **Docker & Docker Compose** – Container orchestration
- **PostgreSQL** – Persistence store for chat data and users

---

## 🚀 Getting Started

1. Make sure you have  Docker & Docker Compose installed

2. Add keycloak to your hosts file: `echo "127.0.0.1 keycloak" | sudo tee -a /etc/hosts`

3. To start the entire application (Ollama, Keycloak, Postgres, LLaMabot): `make start`

4. Simple demo UI available at http://localhost:8080
   - default local user credentials: `admin/admin`
   - default SSO user credentials: `john.doe/securepass`

See the [Makefile](Makefile) for other targets for managing containers.

---

## 🔒 Setting up users in Keycloak

1. Access the keycloak admin console at http://keycloak:9090 and login with credentials admin/admin
2. Under "Manage Realms" select "myrealm"
3. Go to "Users" → "Add User", then enter a username and email
4. Go to "Credentials" and set a password
5. Test the login flow by visiting http://localhost:8080 and click "Login with SSO"
