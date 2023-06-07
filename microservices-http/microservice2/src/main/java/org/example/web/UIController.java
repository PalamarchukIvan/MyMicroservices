package org.example.web;

import lombok.RequiredArgsConstructor;
import org.example.repo.TimeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UIController {

    private final TimeRepository repository;
    @GetMapping("/time")
    public String getCurrentTime() {
        return "UI server: " + repository.getServiceCurrentTime();
    }
}
