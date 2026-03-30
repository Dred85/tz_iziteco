package com.loadtest.service;

import com.loadtest.entity.Message;
import com.loadtest.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Service
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Сохраняет сообщение в БД и логирует событие записи.
     *
     * @param msgUuid UUID сообщения
     * @param head    значение head (true/false)
     * @param readTime момент вычитки из Kafka (UNIX timestamp)
     */
    public void saveMessage(UUID msgUuid, String head, long readTime) {
        Timestamp timeRq = new Timestamp(readTime * 1000); // UNIX seconds -> milliseconds

        Message message = new Message(msgUuid, head, timeRq);
        messageRepository.save(message);

        log.info("[Write to DB] {{ \"msgUuid\": \"{}\", \"head\": {}, \"timeRq\": \"{}\" }}",
                msgUuid, head, readTime);
    }
}
