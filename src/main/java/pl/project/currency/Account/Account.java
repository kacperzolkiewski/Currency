package pl.project.currency.Account;

import pl.project.currency.types.Currency;
import pl.project.currency.History.History;
import pl.project.currency.User.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double money;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    private List<History> history;

    public Account() {}

    public Account(Double money) {
        this.money = money;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account(Double money, Currency currency, User user) {
        this.money = money;
        this.currency = currency;
        this.user = user;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
