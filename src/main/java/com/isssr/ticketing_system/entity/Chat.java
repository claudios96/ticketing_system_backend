package com.isssr.ticketing_system.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.enumeration.ChatType;
import com.isssr.ticketing_system.response_entity.JsonViews;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor

@Getter
@Setter

@Entity
@Table(name = "chats")
public class Chat {

    @JsonView(JsonViews.ChatMessagesLoadInfo.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    private ChatType type;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    @Column(name = "subject_id")
    private Long subjectId;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    @Column(name = "slack_board_id")
    private String slackBoardId;

    @JsonView(JsonViews.MembersOnly.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "chat_id")
    List<ChatMember> chatMembers;

    @JsonView(JsonViews.MessagesOnly.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "chat_id")
    List<ChatMessage> messages;

    public Chat(ChatType type, Long subjectId, String slackBoardId) {
        this.type = type;
        this.subjectId = subjectId;
        this.slackBoardId = slackBoardId;
    }
}
