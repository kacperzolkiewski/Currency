package pl.project.currency.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.project.currency.Account.Account;
import pl.project.currency.History.History;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String name;
    private String surname;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Account> accounts;

    @OneToMany(cascade = CascadeType.ALL)
    private List<History> history;

    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.accounts = new ArrayList<>();
    }
}
