package cz.Bonds4All.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BondVersion implements Serializable {

    private static final long serialVersionUID = 4L;

    private Integer term;
    private Integer amount;
    private String operationDate;
    private String operationType;
}
