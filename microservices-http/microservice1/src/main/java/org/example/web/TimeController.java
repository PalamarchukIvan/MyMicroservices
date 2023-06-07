package org.example.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.crypto.Data;
import java.sql.Date;
import java.time.Instant;

@RestController
public class TimeController {
    @GetMapping("/currentTime")
    public String getCurrentTime() {
        return Date.from(Instant.now()).toString();
    }

}
