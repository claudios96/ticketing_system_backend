package com.isssr.ticketing_system.logger.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

//import org.json.JSONObject;

/**
 * This class represents the payload informations of a logged operation
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Payload {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String json;

    private String objectId;

    @Column(columnDefinition = "TEXT")
    private String type;

    //l'oggetto è di input o di output
    @Column(columnDefinition = "TEXT")
    private String classType;

    @ManyToOne
    private Record record;

    public Payload(String json, String objectId, String type, String classType, Record record) {
        this.json = json;
        this.objectId = objectId;
        this.type = type;
        this.classType = classType;
        this.record = record;
    }

}
