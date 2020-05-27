package cz.Bonds4All.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BondHistory implements Serializable {

    private static final long serialVersionUID = 3L;

    private Long id;
    private Long user;
    private List<BondVersion> versions;
}
