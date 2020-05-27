package cz.Bonds4All.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;

    @OneToMany(mappedBy = "user")
    private List<Bond> bonds;
}