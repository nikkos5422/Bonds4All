package cz.Bonds4All.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserHistory implements Serializable {

    private static final long serialVersionUID = 6L;

    private Long userId;
    private List<BondHistory> bonds;
}
