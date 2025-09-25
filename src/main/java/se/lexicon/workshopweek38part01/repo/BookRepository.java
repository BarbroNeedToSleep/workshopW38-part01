package se.lexicon.workshopweek38part01.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import se.lexicon.workshopweek38part01.entity.Book;


import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByIsbnIgnoreCase(String isbn);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByMaxLoanDaysLessThan(Integer days);

}

