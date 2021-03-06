package ru.oskelly.interview.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.oskelly.interview.task.api.dto.CommentDto;
import ru.oskelly.interview.task.api.dto.CreateCommentDto;
import ru.oskelly.interview.task.api.dto.NotificationDto;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test","with-rpc-handler"})
class TaskApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final int COMMENTS_TO_CREATE = 1000;

    private String baseURL;
    private final CountDownLatch latch = new CountDownLatch(COMMENTS_TO_CREATE);
    private final AtomicInteger commentsSubmitted = new AtomicInteger(0);
    private final AtomicInteger commentsCreated = new AtomicInteger(0);

    @BeforeEach
    public void setup() {
        baseURL = "http://localhost:" + port + "/api/";
    }


    @Test
    void testCommentCreation() throws InterruptedException {

        CommentDto[] comments = listComments(1);
        final long maxId = comments.length > 0 ? comments[0].getId() : 0;

        Executor executor = Executors.newFixedThreadPool(20);

        IntStream.range(0,COMMENTS_TO_CREATE).forEach(i -> executor.execute( () -> {
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(baseURL + "comments",
                        new CreateCommentDto("Comment #" + commentsSubmitted.incrementAndGet()), String.class
                );

                if (response.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                    System.err.println("Server failed: " + response.toString());
                }
                else if (!response.getStatusCode().is5xxServerError()) {
                    commentsCreated.incrementAndGet();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                latch.countDown();
            }
        }));

        latch.await();

        Thread.sleep(5000); // wait for last notification delivery

        Set<Long> savedComments = Stream.of(listComments(COMMENTS_TO_CREATE))
                .map(CommentDto::getId)
                .filter(id -> id > maxId)
                .collect(Collectors.toSet());

        int deliveredNotifications = (int) Stream.of(listNotifications(COMMENTS_TO_CREATE))
                .filter( n -> n.isDelivered() && savedComments.contains(n.getCommentId()))
                .count();


        System.out.printf("Submitted comments:        %4d%n", commentsSubmitted.get());
        System.out.printf("Created comments:          %4d%n", commentsCreated.get());
        System.out.printf("Saved in db:               %4d%n", savedComments.size());
        System.out.printf("Delivered Notifications:   %4d%n", deliveredNotifications);

        Assertions.assertEquals(commentsCreated.get(), savedComments.size());

    }

    CommentDto[] listComments(int page) {
        return restTemplate.getForEntity(baseURL + "comments?max={max}",
                CommentDto[].class, Map.of("max", page)
        ).getBody();
    }

    NotificationDto[] listNotifications(int page) {
        return restTemplate.getForEntity(baseURL + "notifications?max={max}",
                NotificationDto[].class, Map.of("max", page)
        ).getBody();
    }

}
