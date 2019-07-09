package com.isssr.ticketing_system.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.response_entity.JsonViews;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter

@NoArgsConstructor

@Entity
@Table(name = "chat_members")

public class ChatMember {

    @JsonView(JsonViews.IdentifierOnly.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    @Column(name = "chat_id")
    private Long chatId;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    @Column(name = "user_id")
    private Long userId;

    public ChatMember(Long chatId, Long userId) {
        this.chatId = chatId;
        this.userId = userId;
    }
}
