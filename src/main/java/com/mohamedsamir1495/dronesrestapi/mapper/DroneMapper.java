package com.mohamedsamir1495.dronesrestapi.mapper;

import com.mohamedsamir1495.dronesrestapi.dto.DroneDTO;
import com.mohamedsamir1495.dronesrestapi.domain.drone.entity.Drone;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { MedicationMapper.class })
public interface DroneMapper {

    DroneDTO toDTO(Drone drone);

    Drone toEntity(DroneDTO droneDTO);
}
