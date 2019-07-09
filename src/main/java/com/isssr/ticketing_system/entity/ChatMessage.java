package com.isssr.ticketing_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isssr.ticketing_system.enumeration.ChatMessageType;
import com.isssr.ticketing_system.response_entity.JsonViews;
import com.isssr.ticketing_system.utils.jacksonComponents.serializer.CreationTimestampSerializer;
import com.isssr.ticketing_system.utils.jacksonComponents.deserializer.CreationTimestampDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@NoArgsConstructor

@Getter
@Setter

@JsonIgnoreProperties({"chatId"})

@Entity
@Table(name = "messages")
public class ChatMessage implements Comparable<ChatMessage> {

    @JsonView(JsonViews.IdentifierOnly.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(JsonViews.MessageInfo.class)
    @NonNull
    private String text;

    @JsonView(JsonViews.MessageInfo.class)
    @NonNull
    @JsonSerialize(using = CreationTimestampSerializer.class)
    @JsonDeserialize(using = CreationTimestampDeserializer.class)
    private Instant timestamp;

    @JsonView(JsonViews.MessageInfo.class)
    @NonNull
    private String username;

    @JsonView(JsonViews.MessageInfo.class)
    @NonNull
    @Column(name = "complete_name")
    private String completeName;

    //@JsonIgnore
    @NonNull
    @Column(name = "chat_id")
    private Long chatId;

    @JsonView(JsonViews.MessageInfo.class)
    @NonNull
    private ChatMessageType type;


    public ChatMessage(Long chatId, String text, Instant timestamp, String username, String completeName, ChatMessageType messageType) {
        this.chatId = chatId;
        this.text = text;
        this.timestamp = timestamp;
        this.username = username;
        this.completeName = completeName;
        this.type = messageType;
    }

    @Override
    public int compareTo(ChatMessage o) {
        return this.getTimestamp().compareTo(o.getTimestamp());
    }
}
