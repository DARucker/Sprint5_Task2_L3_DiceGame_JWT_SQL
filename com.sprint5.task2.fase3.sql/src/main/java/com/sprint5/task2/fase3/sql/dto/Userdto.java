package com.sprint5.task2.fase3.sql.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Userdto {

    @Schema(description = "This is the id of the Player. The id is autogenerated by the database")
    private int id;
    @Schema(description = "This field is the NAME of the Player. If empty, the system will show Anonymous.", example = "Dario", required = false)
    private String name;
    @Schema(description = "This field is the date of registration. The date is autogenerated by the system.", example = "31/01/2023", required = false)
    private Calendar registDate;


}
