# F1 Betting — Simplified (90‑minute version)

## What it does
- `GET /events` — Lists events from OpenF1 and attaches a simple driver market (random odds 2/3/4)
- `POST /bets` — Places a bet `{ userId, eventId, driverId, amount }`
- `POST /events/{eventId}/outcome` — Settles all bets for the event

## Run
```bash
mvn spring-boot:run
# Swagger: http://localhost:8080/swagger
# H2 console: http://localhost:8080/h2-console (jdbc:h2:mem:f1simple, user: sa, no password)
```
Three users with **100 EUR** each are pre-seeded: `id=1, 2, 3`.


### Extra endpoints
- `GET /users/{id}` → returns `{ id, balanceEur }`
- `GET /bets?userId=` → list bets (all, or filtered by user)
