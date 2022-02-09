package com.evolutivelabs.app.counter.database.mysql.entity;

import com.evolutivelabs.app.counter.database.mysql.entity.pk.ApiRoleUserId;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@IdClass(ApiRoleUserId.class)
@Table(name = "api_role_user")
public class ApiRoleUser {
    @Id
    @Column(name = "user_account")
    private String account;
    @Id
    @Column(name = "role_id")
    private String roleId;
}
