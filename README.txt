3 profiles
- test
  - in memory database
  - integration tests
- development
  - MySQL
- production
  - MySQL
  - SSL
  - port changed automatically 8080->8443 for user
  - Please note browser will warn about the certificate.

Security:
- Password encryption with BCrypt.
- HTTP Basic login
- SSL and CSRF in production profile
- Users can have different authorization levels.
- Method security to keep some areas admin only.

User action logging with Aspect Oriented Programming.

Front-end done with React, React-Router v4 and React-Bootstrap.

How to use:
1. install UI, in project directory run commands:
npm install
npm run-script watch
2. start server (will copy UI to correct location). Run command
mvn spring-boot:run
3. Application is served on address: http://localhost:8080/

Or import Maven project to IDE of your choosing, you know what to do with it.

