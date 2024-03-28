package com.mohamedsamir1495.dronesrestapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DroneDTO {

    @NotBlank
    @Size(max = 100, message = "100 characters max")
    @Schema(description = "Drone Serial Number", example = "AXA123")
    private String serialNumber;

    @Schema(description = "Drone model", example = "LIGHTWEIGHT")
    private String model;

    @NotNull
    @Min(value = 0, message = "0 gr Max")
    @Max(value = 500, message = "500 gr Max")
    @Schema(description = "Drone Max Weight Limit in grams (gr)", example = "5")
    private Double weightLimit;

    @NotNull
    @Schema(description = "Drone current battery capacity", example = "75")
    private Double batteryCapacity;

    @Schema(description = "Drone State", example = "IDLE")
    private String state;
}
