package pl.project.currency.Account;

import org.springframework.data.repository.CrudRepository;
import pl.project.currency.User.User;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, Long> {
    List<Account> findAllByUser(User user);
}
