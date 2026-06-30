# InvenTrack Backend — Local Setup

## Prerequisites

- Java 21
- Maven
- MySQL 8+ running locally

## First-time setup

1. Copy the environment template:

   ```powershell
   Copy-Item .env.example .env
   ```

2. Open `.env` and fill in your real values:
   - `SPRING_DATASOURCE_URL`: your local MySQL database URL. The existing project data is in `inventory_db`.
   - `SPRING_DATASOURCE_USERNAME`: usually `root`.
   - `SPRING_DATASOURCE_PASSWORD`: your MySQL password.
   - `JWT_SECRET`: a long random string of at least 32 characters.
   - `GMAIL_APP_PASSWORD`: the Gmail app password described below. Leave it empty if email alerts are not needed locally.

3. If you do not already have the existing database, create it:

   ```sql
   CREATE DATABASE inventory_db;
   ```

4. Run the app from this directory:

   ```powershell
   mvn spring-boot:run
   ```

5. Confirm startup by finding the `INVENTTRACK STARTUP DATA HEALTH CHECK` block in the terminal or visiting [http://localhost:8083/api/health](http://localhost:8083/api/health).

## Gmail app password (optional)

1. Go to your Google Account → Security and enable 2-Step Verification.
2. Open App Passwords, create one named `InvenTrack`, and copy its 16-character password.
3. Put it in `.env` as `GMAIL_APP_PASSWORD`.

Inventory features still work when mail is not configured. Email delivery failures are logged and do not propagate into inventory transactions.

The same Gmail app password powers low-stock alerts and password-reset emails. After setting it, restart the backend and confirm the missing-password startup warning disappears. Test password recovery from `/forgot-password`; reset links expire after 30 minutes and can only be used once.

## Troubleshooting

- `Could not resolve placeholder`: ensure the checked-in properties still use `${VARIABLE:default}` syntax; `.env` is only needed for real local credentials.
- Database connection failure: verify MySQL is running and the URL, username, and password in `.env` are correct.
- `Unknown column`: `spring.jpa.hibernate.ddl-auto=update` is already enabled for local schema evolution; restart the backend.
- Empty frontend while the backend starts: inspect the startup data counts or `/api/health`. Non-zero counts indicate a frontend API/authentication issue, not data loss.
- Unexpectedly empty counts: verify that `SPRING_DATASOURCE_URL` points to `inventory_db`, not a newly created schema.
