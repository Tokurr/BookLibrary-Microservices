package com.library_book.library_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionMessage {

    String timestamp;
    int status;
    String error;
    String message;
    String path;


}
