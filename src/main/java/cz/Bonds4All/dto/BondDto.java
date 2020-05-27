package cz.Bonds4All.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BondDto implements Serializable {

    private static final long serialVersionUID = 5L;

    private Long id;
    private Integer amount;
    private Integer term;
}
