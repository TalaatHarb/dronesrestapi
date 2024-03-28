package com.mohamedsamir1495.dronesrestapi.domain.drone.entity;

import com.mohamedsamir1495.dronesrestapi.domain.drone.enums.DroneModel;
import com.mohamedsamir1495.dronesrestapi.domain.drone.enums.DroneState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@Builder
@Table("Drone")
@NoArgsConstructor
@AllArgsConstructor
public class Drone {
    @Id
    private Long id;

    private String serialNumber;

    private Double weightLimit;

    private Double batteryCapacity;

    private DroneState state;

    private DroneModel model;

    private double currentLoadWeight;

}
