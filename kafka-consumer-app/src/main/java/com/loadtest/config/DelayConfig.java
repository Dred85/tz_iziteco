package com.loadtest.config;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Хранит текущее значение задержки (в мс) перед записью в БД.
 * Потокобезопасный — можно менять на лету без перезапуска.
 */
@Component
public class DelayConfig {

    private final AtomicLong delayMs = new AtomicLong(1000); // по умолчанию 1000 мс

    public long getDelayMs() {
        return delayMs.get();
    }

    public void setDelayMs(long newDelay) {
        delayMs.set(newDelay);
    }
}
