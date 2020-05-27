package cz.Bonds4All.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class BondVersion {

    public enum OperationType {

        BOND_CREATED,
        TERM_EXTENDED,
        TERM_REDUCED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long versionId;

    @ManyToOne
    @JoinColumn(name = "bond_id")
    private Bond bond;

    private Integer term;
    private Integer amount;
    private String operationDate;
    private String operationType;
}