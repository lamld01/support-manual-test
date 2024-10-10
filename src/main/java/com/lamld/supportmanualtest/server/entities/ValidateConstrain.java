package com.lamld.supportmanualtest.server.entities;

import com.lamld.supportmanualtest.server.data.type.StatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "validate_constrain", schema = "support-manual-test-db")
@AttributeOverrides({
    @AttributeOverride(name = "createdAt", column = @Column(name = "created_at")),
    @AttributeOverride(name = "updatedAt", column = @Column(name = "updated_at"))
})
public class ValidateConstrain extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Size(max = 255)
  @Column(name = "constrain_name")
  private String constrainName;

  @Size(max = 1000)
  @Column(name = "description", length = 1000)
  private String description;

  @Column(name = "regex_value")
  private String regexValue;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private StatusEnum status = StatusEnum.ACTIVE;

}