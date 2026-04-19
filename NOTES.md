1. What does DevPulse do?
It is basically an API manager or more specifically an API monitor which keeps on checking the health of the registered APIs.

2. Why did you use Go for the monitor and Java for the API?
I had previously worked as a Software Engineer where I had used Java so I had professional experience of using Java but I didn't got a chance to learn and use Go so as a learning project I wanted to use in this so that I can build my own skills in Go and sharpen my Java knowledge.

3. What's one thing that broke during the build and how did you fix it?
There were a few thing which got broked while building this on of which was BIGSERIAL mismatch. I got in this Schema validation error where there was a mismatch of data type. 
Hibernate 7 maps Long in Java to BIGINT in PostgreSQL. But we created the tables with SERIAL which is an alias for INTEGER. They don't match, so validation fails.
So to fix this I dropped and recreated the tables with BIGSERIAL instead of SERIAL as it was database issue.