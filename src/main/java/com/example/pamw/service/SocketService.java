package com.example.pamw.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
public class SocketService {
    private static final String WS_MESSAGE_TRANSFER_DESTINATION = "/queue/reply";
    private final SimpMessagingTemplate simpMessagingTemplate;

    public SocketService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendMessage(String username, String message) {
        simpMessagingTemplate.convertAndSendToUser(username, WS_MESSAGE_TRANSFER_DESTINATION,
                message);
    }
}