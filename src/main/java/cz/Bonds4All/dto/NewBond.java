package cz.Bonds4All.dto;

import lombok.Data;

@Data
public class NewBond {

    private Long userId;
    private Integer amount;
    private Integer term;
}