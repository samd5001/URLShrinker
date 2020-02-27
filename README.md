# URLShrinker Application

A simple URL shortening application that interfaces with MongoDB, created using Java and Spring Boot.

Application bootstrapped using [Spring Initilzr](https://start.spring.io/).

## Running the application

This repository contains two shell scripts `start.sh` and `stop.sh`.

To use these scripts you will need Docker installed on your local machine.

The start script builds a docker image for the Java application then uses docker-compose to run an instance of the Mongo database and the app.

The application can also be ran using maven.

## How to use

The application exposes a `/smallurl` endpoint that accepts POST requests with a JSON body in the following format.

```JSON
{ "url", "www.americanexpress.com" }
```

The application returns a text response with a shortened url e.g. `http://localhost/20000`.

To send a post request use the following in the terminal 

```bash
curl -H "Content-Type: application/json" -X POST -d '{"url": "www.americanexpress.com"}'
```

When visiting the returned shortened URL the user will be redirected to the original full URL

### Limitations

- The application uses the Apache Commons UrlValidator for validation and could be better implemented.
- The id generation strategy is a basic counter and could be improved. It is also not concurrently safe and may cause problems if two requests are received at once.
- The shortened URL host is configured by an environment variable and may not necessarily match the actual host name.
- There is no pattern matching on the full length URLs so someone could shorten the same URL twice e.g www.americanexpress.com and americanexpress.com