package com.evolutivelabs.app.counter.database.mysql.entity.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiRoleUserId implements Serializable {
    private String account;
    private String roleId;
}
