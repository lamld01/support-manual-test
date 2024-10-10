package com.lamld.supportmanualtest.server.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "test_field", schema = "support-manual-test-db")
@AttributeOverrides({
    @AttributeOverride(name = "createdAt", column = @Column(name = "created_at")),
    @AttributeOverride(name = "updatedAt", column = @Column(name = "updated_at"))
})
public class TestField extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Size(max = 255)
  @Column(name = "field_name")
  private String fieldName;

  @NotNull
  @Column(name = "project_id", nullable = false)
  private Integer projectId;

  @Size(max = 500)
  @Column(name = "description", length = 500)
  private String description;

  @Size(max = 255)
  @NotNull
  @Column(name = "field_code", nullable = false)
  private String fieldCode;

  @Column(name = "validate_constrain_ids")
  @JdbcTypeCode(SqlTypes.JSON)
  private List<Integer> validateConstrainIds;

  @NotNull
  @Column(name = "account_id", nullable = false)
  private Integer accountId;

}