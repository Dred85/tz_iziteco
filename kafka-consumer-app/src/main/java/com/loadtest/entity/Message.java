package com.loadtest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @Column(name = "msguuid")
    private UUID msgUuid;

    @Column(name = "head")
    private String head;

    @Column(name = "timerq", nullable = false)
    private Timestamp timeRq;

    public Message() {
    }

    public Message(UUID msgUuid, String head, Timestamp timeRq) {
        this.msgUuid = msgUuid;
        this.head = head;
        this.timeRq = timeRq;
    }

    public UUID getMsgUuid() {
        return msgUuid;
    }

    public void setMsgUuid(UUID msgUuid) {
        this.msgUuid = msgUuid;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Timestamp getTimeRq() {
        return timeRq;
    }

    public void setTimeRq(Timestamp timeRq) {
        this.timeRq = timeRq;
    }
}
