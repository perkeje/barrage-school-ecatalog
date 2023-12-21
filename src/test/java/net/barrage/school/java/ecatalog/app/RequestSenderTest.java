package net.barrage.school.java.ecatalog.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("fake")
@AutoConfigureObservability
public class RequestSenderTest {

    @Value("${loader.endpoint}")
    private String endpoint;

    @Value("${loader.requestsPerMinute}")
    private int requestsPerMinute;

    @Test
    public void sendRequests() throws InterruptedException {
        TestRestTemplate restTemplate = new TestRestTemplate();

        try {
            for (int i = 0; i < requestsPerMinute; i++) {
                restTemplate.getForObject("http://localhost:" + 8080 + endpoint, String.class);
                Thread.sleep(60000 / requestsPerMinute);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
