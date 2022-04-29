package pl.project.currency.History;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.project.currency.types.Currency;
import pl.project.currency.types.Transaction;

import java.util.List;

@Repository
public interface HistoryRepository extends CrudRepository<History, Long> {
    List<History> findAllByTransaction(Transaction transaction);

    List<History> findAllByCurrency(Currency currency);
}
