package se.lexicon.workshopweek38part01.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import se.lexicon.workshopweek38part01.entity.Book;
import se.lexicon.workshopweek38part01.entity.BookLoan;

import java.util.List;
import java.util.Optional;

public interface BookLoanRepository extends JpaRepository<BookLoan, Integer> {


    List<BookLoan> findByBorrowerId(Integer borrowerId);

    List<BookLoan>findByBookId(Integer id);

//    List<BookLoan>


}
