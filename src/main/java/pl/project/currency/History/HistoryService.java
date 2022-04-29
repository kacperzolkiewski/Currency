package pl.project.currency.History;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.project.currency.Account.Account;
import pl.project.currency.types.Currency;
import pl.project.currency.types.Transaction;
import pl.project.currency.User.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class HistoryService {

    private HistoryRepository historyRepository;

    public void saveTransaction(User user, Account account, Transaction transaction){
        History history = new History();
        history.setTransaction(transaction);
        history.setAccount(account);
        history.setUser(user);
        history.setDate(LocalDate.now());
        history.setCurrency(account.getCurrency());

        historyRepository.save(history);
    }

    public List<History> getHistories(){
        List<History> histories = new ArrayList<>();
        historyRepository.findAll().forEach(histories::add);
        return histories;
    }

    public List<History> getHistoriesByTransaction(Transaction transaction){
        List<History> histories = new ArrayList<>();
        historyRepository.findAllByTransaction(transaction).forEach(histories::add);
        return histories;
    }

    public List<History> getHistoriesByDates(LocalDate fromDate, LocalDate toDate){
        List<History> histories = getHistories();
        histories.removeIf(history
                -> (history.getDate().isBefore(fromDate) && history.getDate().isAfter(toDate)));

        return histories;
    }

    public List<History> getHistoriesByCurrency(Currency currency){
        List<History> histories = new ArrayList<>();
        historyRepository.findAllByCurrency(currency).forEach(histories::add);
        return histories;
    }

}
