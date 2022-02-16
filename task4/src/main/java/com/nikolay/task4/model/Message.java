package com.nikolay.task4.model;

import javax.persistence.*;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sender, receiver, subject,  full_text;

    public Message() {
    }

    public Message(String sender, String receiver, String subject, String full_text) {
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.full_text = full_text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFull_text() {
        return full_text;
    }

    public void setFull_text(String full_text) {
        this.full_text = full_text;
    }
}

//public class Message {
//    private String full_text;
//    private String receiver;
//
//    public Message() {
//    }
//
//    public Message(String full_text, String receiver) {
//        this.full_text = full_text;
//        this.receiver = receiver;
//    }
//
//    public String getReceiver() {
//        return receiver;
//    }
//
//    public void setReceiver(String receiver) {
//        this.receiver = receiver;
//    }
//
//    public String getFull_text() {
//        return full_text;
//    }
//
//    public void setFull_text(String full_text) {
//        this.full_text = full_text;
//    }
//}
