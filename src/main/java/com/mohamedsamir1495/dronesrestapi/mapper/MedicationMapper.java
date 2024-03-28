package com.mohamedsamir1495.dronesrestapi.mapper;

import com.mohamedsamir1495.dronesrestapi.domain.drone.entity.Drone;
import com.mohamedsamir1495.dronesrestapi.domain.medication.entity.Medication;
import com.mohamedsamir1495.dronesrestapi.dto.MedicationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicationMapper {

    MedicationDTO toDTO(Medication medication);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "droneId", source = "drone.id")
    Medication toEntity(MedicationDTO medicationDTO, Drone drone);
}
