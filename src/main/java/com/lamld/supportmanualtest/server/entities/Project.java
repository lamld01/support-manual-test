package com.lamld.supportmanualtest.server.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "project", schema = "support-manual-test-db")
@AttributeOverrides({
    @AttributeOverride(name = "createdAt", column = @Column(name = "created_at")),
    @AttributeOverride(name = "updatedAt", column = @Column(name = "updated_at"))
})
public class Project extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotNull
  @Column(name = "account_id", nullable = false)
  private Integer accountId;

  @Column(name = "parent_project_id", nullable = false)
  private Integer parentProjectId = 0;

  @Column(name = "root_project_id", nullable = false)
  private Integer rootProjectId = 0;

  @Size(max = 255)
  @Column(name = "project_name")
  private String projectName;

  @Size(max = 255)
  @Column(name = "api_base_url")
  private String apiBaseUrl;

  @Size(max = 1000)
  @Column(name = "token", length = 1000)
  private String token;

  @Size(max = 1000)
  @Column(name = "description", length = 1000)
  private String description;

}