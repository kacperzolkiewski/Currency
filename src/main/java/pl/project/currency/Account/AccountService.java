package pl.project.currency.Account;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.project.currency.types.Currency;
import pl.project.currency.History.HistoryService;
import pl.project.currency.types.Transaction;
import pl.project.currency.User.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    private HistoryService historyService;

    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();

        accountRepository.findAll().forEach(accounts::add);
        return accounts;
    }

    public Account getAccountById(Long id) {
        Optional<Account> account = accountRepository.findById(id);

        if (account.isPresent())
            return account.get();

        return null;
    }

    public List<Account> createUserAccounts(User user) {
        List<Account> accounts = new ArrayList<>();

        accounts.add(new Account(0.0, Currency.PLN, user));
        accounts.add(new Account(0.0, Currency.EUR, user));
        accounts.add(new Account(0.0, Currency.USD, user));

        return accounts;
    }

    public void paymentOnAccount(Long id, double money) throws Exception {
        if (money < 0)
            throw new Exception("Money is less than zero");

        Account account = this.getAccountById(id);

        if (account == null)
            throw new Exception("There is no account in database with id=" + id.toString());

        account.setMoney(money);
        accountRepository.save(account);

        historyService.saveTransaction(account.getUser(), account, Transaction.PAYMENT);
    }

    public double withdrawMoneyFromAccount(Long id, double money) throws Exception {
        if (money < 0)
            throw new Exception("Money is less than zero");

        Account account = this.getAccountById(id);

        if (account == null)
            throw new Exception("There is no account in database with id=" + id.toString());

        double withdrawMoney = 0.0;

        if (account.getMoney() <= money) {
            withdrawMoney = account.getMoney();
            account.setMoney(0.0);
        } else {
            withdrawMoney = money;
            account.setMoney(account.getMoney() - withdrawMoney);
        }

        accountRepository.save(account);
        historyService.saveTransaction(account.getUser(), account, Transaction.WITHDRAW);

        return withdrawMoney;
    }

    public List<Account> getUserAccounts(Long userID) {
        List<Account> accounts = getAllAccounts();

        accounts.removeIf(account -> account.getUser().getId() != userID);
        return accounts;
    }

    public Account getUserAccountByCurrency(Long userID, Currency currency) {
        List<Account> accounts = this.getUserAccounts(userID);

        for (Account account : accounts) {
            if (account.getCurrency() == currency) {
                return account;
            }
        }
        return null;
    }

    public double getAccountBalance(Long id) throws Exception {
        Account account = this.getAccountById(id);

        if (account == null)
            throw new Exception("There is no account in database with id=" + id.toString());

        return account.getMoney();
    }
}
