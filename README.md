### Graylog server
First, you need an running instance of the Graylog server. If you don't have any, you can
start one dockerized by calling 

```
docker-compose up
```

If this is a fresh instance, don't forget to start an GELF HTTP input

### Start the client
From command line call 

```
mvn spring-boot:run
```

or from your IDE, you can start the `GraylogDemoApplication.main` method.

The app will start, read all 100 messages in the `sample-messages.txt` file, transform
them into GELF messages and one by one, synchronously, send them to the Graylog server.
For the sake of code demo and simplicity, the are no queues, no repeated sends in case of 
failure. Any exception thrown will interrupt the whole process and the app terminates.

### Logging
By default logging is configured to the `ÌNFO` level. If you need more details, set `logging.level.root` 
property in the `application.properties` to `DEBUG`

### Tests

Run `mvn test`, there are two unit tests for the parser and serializer of messages and one
integration end-to-end test that will start a mock-server, perform all the message parsing and sending. 
Then the test verifies that messages have been transported, validating also some of their properties. 