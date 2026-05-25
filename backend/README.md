# Veltroz Chatbot Backend

This Spring Boot backend provides REST APIs for storing conversations and messages for the Veltroz chatbot frontend.

## Endpoints

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/conversations`
- `POST /api/conversations`
- `GET /api/conversations/{conversationId}/messages`
- `POST /api/conversations/{conversationId}/messages`
- `PATCH /api/conversations/{conversationId}`
- `DELETE /api/conversations`
- `POST /api/schedule`

## Run locally

1. Install Java 17 and Maven.
2. From the `backend` folder:
   ```bash
   mvn spring-boot:run
   ```
3. Open the backend on `https://veltroz-production.up.railway.app`.
4. If you want to inspect the H2 database, open `https://veltroz-production.up.railway.app/h2-console`.

## Notes

- Authentication now uses JWT tokens. Register at `/api/auth/register` and login at `/api/auth/login`.
- User-specific conversation history is stored by email and returned only to the authenticated user.
- The backend automatically replies using a simple Veltroz-specific assistant response model.
