package com.loadtest.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loadtest.config.DelayConfig;
import com.loadtest.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class KafkaMessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageConsumer.class);

    private final MessageService messageService;
    private final DelayConfig delayConfig;
    private final ObjectMapper objectMapper;

    public KafkaMessageConsumer(MessageService messageService, DelayConfig delayConfig) {
        this.messageService = messageService;
        this.delayConfig = delayConfig;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Слушает топик Kafka. Многопоточность обеспечивается настройкой
     * spring.kafka.listener.concurrency=3 (по числу партиций).
     * Каждый поток обрабатывает свою партицию параллельно.
     */
    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String rawMessage) {
        // Время вычитки сообщения из Kafka (UNIX timestamp в секундах)
        long readTime = Instant.now().getEpochSecond();

        log.info("[Read from Kafka] {}", rawMessage);

        try {
            JsonNode json = objectMapper.readTree(rawMessage);

            String msgUuidStr = json.get("msg_uuid").asText();
            UUID msgUuid = UUID.fromString(msgUuidStr);

            String head = json.get("head").asText(); // "true" или "false"

            // Задержка перед записью в БД (динамически настраиваемая)
            long delay = delayConfig.getDelayMs();
            if (delay > 0) {
                Thread.sleep(delay);
            }

            messageService.saveMessage(msgUuid, head, readTime);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("[Interrupted] Поток прерван во время задержки");
        } catch (Exception e) {
            log.error("[Error] Не удалось обработать сообщение: {} | Ошибка: {}", rawMessage, e.getMessage());
        }
    }
}
