package com.mohamedsamir1495.dronesrestapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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
public class MedicationDTO {
    @Pattern(
            regexp = "[A-Z0-9_]+",
            message = "only upper case letters, underscore and numbers allowed"
    )
    @Schema(description = "Medication code", example = "FARA123")
    private String code;
    @Pattern(
            regexp = "[a-zA-Z_0-9-]+",
            message = "only letters, numbers, underscore and hyphen allowed"
    )
    @Schema(description = "Medication trade name", example = "FARACETAMOL")
    private String name;

    @Positive
    @Schema(description = "Medication weight in grams", example = "2")
    private Double weight;

    @Schema(description = "Medication image path on S3", example = "s3://mybucket/myfolder/myimage.jpg")
    private String image;
}
