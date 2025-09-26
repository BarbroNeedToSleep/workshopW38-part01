package se.lexicon.workshopweek38part01.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.workshopweek38part01.entity.BookLoan;
import java.time.LocalDate;
import java.util.List;


public interface BookLoanRepository extends JpaRepository<BookLoan, Integer> {


    List<BookLoan> findByBorrower_Id(Integer borrowerId);

    List<BookLoan> findByBook_Id(Integer bookId);

    List<BookLoan> findByReturnedIsFalse();

    List<BookLoan> findByDueDateBefore(LocalDate date);

    List<BookLoan> findByLoanDateBetween(LocalDate startDate, LocalDate endDate);

    @Transactional
    @Modifying
    @Query("UPDATE BookLoan b SET b.returned = true WHERE b.id = :id")
    void markAsReturned(@Param("id") Integer id);


}
