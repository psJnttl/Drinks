# Drinks
##### Profiles
- test
  - in memory database
  - integration tests
- development
  - MySQL
- production
  - MySQL
  - SSL
  - CSRF
  - port changed automatically 8080->8443 for user
  - Please note browser will warn about the certificate.

##### Security:
- Password encryption with BCrypt.
- HTTP Basic login
- SSL and CSRF in production profile
- Users can have different authorization levels.
- Method security to keep some areas admin only.
##### Instrumentation
- User action logging with Aspect Oriented Programming.
##### Front-end
- React
- React-Router v4
- React-Bootstrap
- [react-datetime](https://github.com/arqex/react-datetime)
- [react-select](https://jedwatson.github.io/react-select)

###### How to use:
1. install UI, in project directory run commands:
```sh
$ npm install
$ npm run-script watch
```
2. start server (will copy UI to correct location). Run command
```sh
$ mvn spring-boot:run
```
3. Application is served on address: http://localhost:8080/

Or import Maven project to IDE of your choosing, you know what to do with it.
