package com.siseth.weatherStation.component.entity.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.OffsetDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity extends BaseIdEntity {

    @Column(name = "`createdAt`")
    protected OffsetDateTime createdAt;

    @Column(name = "`modifiedAt`")
    protected OffsetDateTime modifiedAt = OffsetDateTime.now();

    @PrePersist
    void setCreatedAt(){
        if(this.createdAt == null)
            this.setCreatedAt(OffsetDateTime.now());
    }

    @PreUpdate
    void setModifiedAt(){
        this.modifiedAt = OffsetDateTime.now();
    }

}
