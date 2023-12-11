package com.Mexxar.Test.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse <T>{
    //used to display messages in a standard structure
    private int code;
    private String message;
    private T payload;
}
