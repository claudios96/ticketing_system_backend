package com.isssr.ticketing_system.entity.SoftDelete;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@FilterDef(name = "deleted_filter", parameters = {@ParamDef(name = "value", type = "boolean")})
@Filter(name = "deleted_filter", condition = "deleted = :value")
public class SoftDeletableEntity implements SoftDeletable {
    @JsonIgnore
    private boolean deleted;

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
