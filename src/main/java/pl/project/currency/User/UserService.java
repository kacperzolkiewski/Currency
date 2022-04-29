package pl.project.currency.User;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.project.currency.Account.Account;
import pl.project.currency.Account.AccountService;
import pl.project.currency.types.Currency;
import pl.project.currency.History.HistoryService;
import pl.project.currency.types.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    private HistoryService historyService;

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public void addUser(User user) {
        user.setAccounts(accountService.createUserAccounts(user));
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent())
            return user.get();

        return null;
    }

    public void sendMoneyToUser(Long senderId, Long receiverId, Currency currency, double money) throws Exception {
        if (money < 0)
            throw new Exception("Money is less than zero");

        if (getUserById(senderId) == null)
            throw new Exception("Sender is not in the database");

        if (getUserById(receiverId) == null)
            throw new Exception("Receiver is not in the database");

        Account senderAccount = accountService.getUserAccountByCurrency(senderId, currency);
        Account receiverAccount = accountService.getUserAccountByCurrency(receiverId, currency);

        double withdrawMoney = accountService.withdrawMoneyFromAccount(senderAccount.getId(), money);
        accountService.paymentOnAccount(receiverAccount.getId(), withdrawMoney);

        historyService.saveTransaction(senderAccount.getUser(), senderAccount, Transaction.TRANSFER);
        historyService.saveTransaction(receiverAccount.getUser(), receiverAccount, Transaction.TRANSFER);
    }

    public void exchangeMoneyToCurrency(
            Long userId, double money, Currency fromCurrency, Currency toCurrency) throws Exception {
        if (money < 0)
            throw new Exception("Money is less than zero");

        Account fromAccount = accountService.getUserAccountByCurrency(userId, fromCurrency);
        Account toAccount = accountService.getUserAccountByCurrency(userId, toCurrency);

        double withdrawMoney = accountService.withdrawMoneyFromAccount(fromAccount.getId(), money);

        switch (fromCurrency) {
            case PLN:
                withdrawMoney = toCurrency.getValue() + withdrawMoney;
                break;
            default:
                withdrawMoney = Currency.PLN.getValue() / fromCurrency.getValue() * withdrawMoney * toCurrency.getValue();
                break;
        }

        accountService.paymentOnAccount(toAccount.getId(), withdrawMoney);
    }
}
