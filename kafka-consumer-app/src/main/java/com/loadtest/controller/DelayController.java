package com.loadtest.controller;

import com.loadtest.config.DelayConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST-контроллер для динамического управления задержкой.
 * Позволяет менять задержку без перезапуска приложения.
 *
 * GET  /api/delay        — текущее значение задержки
 * POST /api/delay?ms=500 — установить новое значение
 */
@RestController
@RequestMapping("/api/delay")
public class DelayController {

    private static final Logger log = LoggerFactory.getLogger(DelayController.class);

    private final DelayConfig delayConfig;

    public DelayController(DelayConfig delayConfig) {
        this.delayConfig = delayConfig;
    }

    @GetMapping
    public Map<String, Long> getDelay() {
        return Map.of("delayMs", delayConfig.getDelayMs());
    }

    @PostMapping
    public Map<String, Object> setDelay(@RequestParam("ms") long ms) {
        long oldDelay = delayConfig.getDelayMs();
        delayConfig.setDelayMs(ms);
        log.info("[Delay changed] {} ms → {} ms", oldDelay, ms);
        return Map.of(
                "oldDelayMs", oldDelay,
                "newDelayMs", ms,
                "status", "OK"
        );
    }
}
