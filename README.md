# Close To Us - Asteroid Radar API ☄️

"Close To Us" is a backend REST API designed to monitor and analyze Near-Earth Objects (NEOs) using official telemetry from NASA's NeoWs (Near Earth Object Web Service) API. 

Recently overhauled, the project was migrated from an anemic, reactive architecture to a **Pragmatic Imperative Clean Architecture**. It features an encapsulated Rich Domain Model, automated testing invariants, and a dedicated Anti-Corruption Layer (ACL) to protect the core business rules from external API structural changes.

---

## 🏗️ Architectural Evolution & Design Patterns

The core engine of this application was rewritten to showcase high-level software engineering best practices:

* **Rich Domain Model over Anemic Objects:** Transformed plain data holders into immutable **Java Records** (`Asteroid`, `CloseApproachData`, `EstimatedDiameter`). Business behaviors like `isDangerous()` and `isFast()` are now safely encapsulated inside the domain domain logic rather than leaking into procedural services.
* **Anti-Corruption Layer (ACL):** Implemented a strict mapping boundary (`AsteroidMapper`). The application isolates raw, complex JSON schemas delivered by NASA (`jsonfields`) into clean, decoupled domain representations, ensuring high maintainability.
* **From Reactive to Imperative (RestClient):** Removed the accidental complexity, high cognitive load, and overhead of Spring WebFlux (Netty, Mono/Flux) in favor of Spring's modern, synchronous, and thread-safe **RestClient**.
* **Fail-Fast & Global Exception Handling:** Integrated a unified `@RestControllerAdvice` handling domain validations (like `InvalidDateRangeException`) and upstream infrastructure outages (`NasaApiException`), delivering standardized RFC-compliant error payloads (`ApiErrorResponse`).
* **Self-Documenting API:** Fully integrated with **Springdoc OpenAPI / Swagger**, exposing explicit endpoints contracts, parameter examples, and schema types.

---

## 🛠️ Tech Stack

* **Language:** Java 21 (Leveraging modern features like Records and Pattern Matching)
* **Framework:** Spring Boot 4.0.6
    * Spring Web (MVC Pattern)
    * Spring RestClient (HTTP Client Integration)
    * Springdoc OpenAPI Starter WebMVC UI (Swagger Documentation)
* **Build Tool:** Maven
* **Testing Suite:** JUnit 5, Mockito, and AssertJ (100% focused on Domain Invariants and Use Case orchestration)
* **External Provider:** NASA NeoWs API

---
