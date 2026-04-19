1. What does DevPulse do?
DevPulse is a real-time API health monitoring platform. You register 
URLs you want to track, and it automatically pings them every 30 seconds, 
records response times and status codes, and lets you query the history 
and uptime percentage through a REST API.

2. Why did you use Go for the monitor and Java for the API?
I used Java with Spring Boot for the management API because I had 
professional experience with Java and Spring Boot's ecosystem — JPA, 
Security, validation — saves significant development time for CRUD 
and auth work. I used Go for the monitor because it runs one tight 
loop making concurrent HTTP calls, and Go goroutines handle that 
kind of I/O-bound concurrency more cheaply than Java threads. 
Each language is doing what it's genuinely better at.

3. What's one thing that broke during the build and how did you fix it?
One issue was a schema validation error — Hibernate 7 maps Java's Long 
to BIGINT in PostgreSQL, but I had created the tables using SERIAL which 
is an alias for INTEGER. The types didn't match so Spring refused to start. 
I fixed it by dropping and recreating the tables using BIGSERIAL instead. 
The lesson: when using ddl-auto=validate, your SQL types need to match 
exactly what the ORM expects — it's stricter than update mode which 
would have silently fixed it.