package dev.mayutama.smartbook.initializer;

import dev.mayutama.smartbook.service.InitializerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final InitializerService initializerService;

    @Override
    public void run(String... args) throws Exception {
        initializerService.initData();
    }
}
