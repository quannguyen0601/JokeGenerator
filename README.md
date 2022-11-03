# JokeGenerator

## Description

A backend API for getting jokes from https://api.chucknorris.io/.
Project is implemented by using **Dropwizard** Framework and **Guice** DI. 

1. Joke REST API:

   URL: ``http://localhost:8081/api/joke/generate?query=gun``

2.  Returned jokes should contain only the full match of keywords.

   Please check ```IJokeService <- JokeService <- JokeServiceImpl```

3. Rate Limit:

   Create a new annotation with name ```@RateLimiterByQueryRequired```.
   Change the following parameters in annotation as per your need:
   1. **parameter**: name of parameter in query param
   2. **rateLimit**: Overall request count allowed in time limit.
   3. **timeLimit**: Overall time rate limit (Second).

   About algorithm of rate limit, use Sliding Window. Check out ```SlidingWindowRateLimitservice```. This is subclass of ``RateLimitService``


How to start the JokeGenerator application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/JokeGenerator-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8081`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

Unit Test
---
Run `mvn clean test`

  


    



