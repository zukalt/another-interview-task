#How to...

### Preconditions
Make sure you have `docker` installed and service running.

### Build & Run

    # cd [project dir]
    docker-compose build
    docker-compose up

### Access API documentation

[Swagger Docs](http://localhost:8888/swagger-ui.html#/) are available at 
    
    http://localhost:8888/swagger-ui.html

### Run tests

 `*` requires openjdk 13 (others not tested)
 
Run rabbitmq

    # cd [project dir]
    docker-compose up -d rabbitmq 

Once ready

    # cd [project dir]
    ./gradlew test

Sample output

    .
    .
    .
    2021-03-06 20:00:10.832  INFO 29608 --- [tContainer#1-13] r.o.i.t.s.ext.RemoteCommentsRPCHandler   : Delivering notification #1665
    2021-03-06 20:00:10.833  WARN 29608 --- [nPool-worker-11] r.o.i.t.s.comments.CommentServiceImpl    : Notification delivery failed for comment #1637
    2021-03-06 20:00:11.323  INFO 29608 --- [tContainer#1-17] r.o.i.t.s.ext.RemoteCommentsRPCHandler   : Delivering notification #1705
    2021-03-06 20:00:11.381  INFO 29608 --- [tContainer#1-27] r.o.i.t.s.ext.RemoteCommentsRPCHandler   : Delivering notification #1695
    Submitted comments:        1000
    Created comments:           707
    Saved in db:                707
    Delivered Notifications:    637

    BUILD SUCCESSFUL in 48s
    4 actionable tasks: 2 executed, 2 up-to-date
