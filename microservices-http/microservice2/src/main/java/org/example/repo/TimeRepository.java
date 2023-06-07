package org.example.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;


@Repository
@RequiredArgsConstructor
public class TimeRepository {

    private final String source = "http://localhost:8088/currentTime";
    private final RestTemplate template;

    public String getServiceCurrentTime() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = template.exchange(source, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

}
