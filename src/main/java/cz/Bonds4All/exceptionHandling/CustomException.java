package cz.Bonds4All.exceptionHandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CustomException extends Exception {

    private final ResponseError.ErrorType type;
    private final String message;
}