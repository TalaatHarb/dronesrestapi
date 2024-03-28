package com.mohamedsamir1495.dronesrestapi.domain.medication.entity;


import com.mohamedsamir1495.dronesrestapi.domain.medication.enums.MedicationState;
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
@Table("Medication")
@NoArgsConstructor
@AllArgsConstructor
public class Medication {
    @Id
    private Long id;
    private String name;
    private double weight;
    private String code;
    private String image;

    private MedicationState state;
    private Long droneId;
}
