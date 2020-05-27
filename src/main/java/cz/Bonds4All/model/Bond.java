package cz.Bonds4All.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Bond {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "bond")
    private List<BondVersion> versions;
}
