# 🎮 Fusion2048 – Full Stack Web Application

*Fusion2048 is a production-ready full-stack web application built using Spring Boot and PostgreSQL with manual JWT-based authentication and a vanilla JavaScript frontend.*
*The system implements secure stateless authentication, persistent score storage, and layered backend architecture.*

# 🌐 Live Application
*https://fusion2048.vercel.app/*

# 🏗 System Architecture

Frontend (HTML/CSS/JS)
        ↓ REST API (JSON)
Spring Boot Backend
  ├── Security Config
  ├── JWT Filter (Custom OncePerRequestFilter)
  ├── Controllers
  ├── Services
  ├── Repositories (JPA)
        ↓
PostgreSQL (Render)

# 🔐 Authentication Flow (Manual JWT Implementation)

*1. User submits login credentials* <br>
*2. Password validated using BCrypt* <br>
*3. JWT token generated using secret key* <br>
*4. Custom JWT filter intercepts every request* <br>
*5. Token validated before accessing protected endpoints* <br>
*6. Stateless authentication*

# 🚀 Tech Stack
# Frontend
HTML
CSS
JavaScript

# Backend
Java
Spring Boot
Spring Security
Custom JWT Filter
JPA / Hibernate

# Database
PostgreSQL (Render)

# Deployment
Frontend deployed on Vercel
Backend & PostgreSQL deployed on Render
Environment variables for secrets

#📡 Core Features
User Registration
Encrypted Password Storage (BCrypt)
Manual JWT Authentication
Stateless Session Management
Persistent Score Tracking
RESTful API Architecture
Production Deployment

#🗄 Database Schema Overview
User
id
username (unique)
password (encrypted)

Score
id
user_id (foreign key)
score
timestamp

# 📸 Application Screenshots
<img width="1919" height="876" alt="Screenshot 2026-02-18 202958" src="https://github.com/user-attachments/assets/1ac727a8-9bce-4aed-9d99-bef528a57cc3" />
<img width="1919" height="881" alt="Screenshot 2026-02-18 203209" src="https://github.com/user-attachments/assets/10e98fa4-2ca0-41ee-9dd4-6f784f838d65" />
<img width="1919" height="884" alt="Screenshot 2026-02-18 203349" src="https://github.com/user-attachments/assets/343c49b1-e5ef-4dc1-8ce9-d9b3f50acc62" />

# ⚙️ Run Locally
Backend
mvn clean install
mvn spring-boot:run

frontend
Open index.html
