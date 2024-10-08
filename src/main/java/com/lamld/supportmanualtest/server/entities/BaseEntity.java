package com.lamld.supportmanualtest.server.entities;

import com.lamld.supportmanualtest.server.utils.DateUtils;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@MappedSuperclass
public class BaseEntity {

  @Column(updatable = false)
  private Long createdAt;

  private Long updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = DateUtils.getNowMillisAtUtc(); //get current milliseconds
    updatedAt =  DateUtils.getNowMillisAtUtc(); // get current milliseconds when updated
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt =  DateUtils.getNowMillisAtUtc(); // get current milliseconds when updated
  }
}
