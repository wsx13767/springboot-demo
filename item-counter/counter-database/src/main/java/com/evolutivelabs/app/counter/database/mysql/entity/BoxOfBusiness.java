package com.evolutivelabs.app.counter.database.mysql.entity;

import com.evolutivelabs.app.counter.database.mysql.entity.pk.BusinessBoxId;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "box_of_business")
@IdClass(BusinessBoxId.class)
public class BoxOfBusiness {
    @Id
    @Column(name = "business_id")
    private String businessId;
    @Id
    @Column(name = "box_carton")
    private String boxCarton;
}
