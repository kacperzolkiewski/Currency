package pl.project.currency.History;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.project.currency.Account.Account;
import pl.project.currency.types.Currency;
import pl.project.currency.types.Transaction;
import pl.project.currency.User.User;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Enumerated(EnumType.STRING)
    private Transaction transaction;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToOne
    private User user;

    @ManyToOne
    private Account account;

    LocalDate date;

}
