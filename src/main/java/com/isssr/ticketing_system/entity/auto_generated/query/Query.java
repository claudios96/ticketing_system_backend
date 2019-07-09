package com.isssr.ticketing_system.entity.auto_generated.query;

import com.fasterxml.jackson.annotation.JsonView;
import com.isssr.ticketing_system.enumeration.TicketPriority;
import com.isssr.ticketing_system.exception.UpdateException;
import com.isssr.ticketing_system.entity.SoftDelete.SoftDeletable;
import com.isssr.ticketing_system.response_entity.JsonViews;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Observable;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Query extends Observable implements Serializable, SoftDeletable {

    @JsonView(JsonViews.Basic.class)
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Id
    protected Long id;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    protected String description;

    @JsonView(JsonViews.Detailed.class)
    @NonNull
    protected TicketPriority queryPriority;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    protected boolean active = true;

    protected boolean deleted = false;

    @JsonView(JsonViews.Basic.class)
    @NonNull
    protected boolean isEnable;

    @NonNull
    protected String author;

    public Query(String description, TicketPriority queryPriority, boolean isEnable, String author) {
        this.description = description;
        this.queryPriority = queryPriority;
        this.isEnable = isEnable;
        this.author = author;
    }

    public Query(String description, TicketPriority queryPriority, boolean active, boolean deleted, boolean isEnable, String author) {
        this.description = description;
        this.queryPriority = queryPriority;
        this.active = active;
        this.deleted = deleted;
        this.isEnable = isEnable;
        this.author = author;
    }

    public abstract boolean equalsByClass(Query otherQuery);

    public abstract String toMailPrettyString();


    public void updateMe(Query updatedData) throws UpdateException {

        if (this.id.longValue() != updatedData.id.longValue())
            throw new UpdateException("Attempt to update a data base time query record without ID matching");

        this.description = updatedData.description;

        this.queryPriority = updatedData.queryPriority;

        this.isEnable = updatedData.isEnable;

    }

    public void activeMe() {
        this.active = true;
    }

    public boolean isActive() {
        return active;
    }

    public void disableMe() {
        this.active = false;
    }

    public TicketPriority priority() {
        return this.queryPriority;
    }

    @Override
    public void delete() {
        this.deleted = true;
    }

    @Override
    public void restore() {
        this.deleted = false;
    }

    @Override
    public boolean isDeleted() {
        return this.deleted;
    }
}
