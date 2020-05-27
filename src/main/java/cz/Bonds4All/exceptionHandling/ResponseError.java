package cz.Bonds4All.exceptionHandling;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ResponseError implements Serializable {

    private static final long serialVersionUID = 2L;

    public enum ErrorType {

        BOND_NOT_FOUND,
        EXCEEDED_MAX_SOLD_PER_DAY,
        ILLEGAL_MIDNIGHT_TIME_OPERATION,
        INTERNAL_SERVER_ERROR,
        INVALID_AMOUNT_VALUE,
        INVALID_TERM_VALUE,
        MISSING_MANDATORY_FIELD,
        USER_NOT_FOUND
    }

    private final String type;
    private final String message;
}