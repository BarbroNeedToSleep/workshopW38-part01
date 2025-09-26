package se.lexicon.workshopweek38part01.repo;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import se.lexicon.workshopweek38part01.entity.AppUser;
import se.lexicon.workshopweek38part01.entity.Book;
import se.lexicon.workshopweek38part01.entity.BookLoan;
import se.lexicon.workshopweek38part01.entity.Details;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan("se.lexicon.workshopweek38part01.entity")
@ActiveProfiles("test")
class BookLoanRepositoryTest {

    @Autowired
    private BookLoanRepository repository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager entityManager;

    // Helper to create AppUser with Details
    private AppUser createUser(String username, String email, String name, LocalDate birthDate) {
        Details details = Details.builder()
                .email(email)
                .name(name)
                .birthDate(birthDate)
                .build();
        AppUser user = new AppUser(username, "pass123", details);
        appUserRepository.save(user);
        return user;
    }

    // Helper to create Book with all required fields
    private Book createBook(String isbn, String title, int maxLoanDays) {
        Book book = new Book(isbn, title, maxLoanDays);
        bookRepository.save(book);
        return book;
    }

    @Test
    void findByBorrower_Id() {
        AppUser borrower = createUser("john", "john@example.com", "John Doe", LocalDate.of(1990, 1, 1));
        Book book = createBook("123-456-789", "Java 101", 14);

        BookLoan loan = new BookLoan();
        loan.setBorrower(borrower);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(7));
        loan.setReturned(false);
        repository.save(loan);

        List<BookLoan> result = repository.findByBorrower_Id(borrower.getId());
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(borrower.getId(), result.get(0).getBorrower().getId());
    }

    @Test
    void findByBook_Id() {
        AppUser borrower = createUser("jane", "jane@example.com", "Jane Doe", LocalDate.of(1992, 5, 10));
        Book book = createBook("987-654-321", "Spring Boot", 21);

        BookLoan loan = new BookLoan();
        loan.setBorrower(borrower);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(7));
        loan.setReturned(false);
        repository.save(loan);

        List<BookLoan> result = repository.findByBook_Id(book.getId());
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(book.getId(), result.get(0).getBook().getId());
    }

    @Test
    void findByReturnedIsFalse() {
        AppUser borrower = createUser("mike", "mike@example.com", "Mike Smith", LocalDate.of(1985, 3, 20));
        Book book = createBook("555-666-777", "Hibernate", 14);

        BookLoan loan = new BookLoan();
        loan.setBorrower(borrower);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(7));
        loan.setReturned(false);
        repository.save(loan);

        List<BookLoan> result = repository.findByReturnedIsFalse();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertFalse(result.get(0).isReturned());
    }

    @Test
    void findByDueDateBefore() {
        AppUser borrower = createUser("sara", "sara@example.com", "Sara Connor", LocalDate.of(1995, 7, 15));
        Book book = createBook("222-333-444", "Data Structures", 10);

        BookLoan loan = new BookLoan();
        loan.setBorrower(borrower);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now().minusDays(10));
        loan.setDueDate(LocalDate.now().minusDays(2));
        loan.setReturned(false);
        repository.save(loan);

        List<BookLoan> result = repository.findByDueDateBefore(LocalDate.now());
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void findByLoanDateBetween() {
        AppUser borrower = createUser("emma", "emma@example.com", "Emma Watson", LocalDate.of(1993, 8, 25));
        Book book = createBook("111-222-333", "Algorithms", 14);

        BookLoan loan = new BookLoan();
        loan.setBorrower(borrower);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now().minusDays(3));
        loan.setDueDate(LocalDate.now().plusDays(4));
        loan.setReturned(false);
        repository.save(loan);

        List<BookLoan> result = repository.findByLoanDateBetween(
                LocalDate.now().minusDays(5),
                LocalDate.now()
        );
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void markAsReturned() {
        // Create borrower and book
        AppUser borrower = createUser("lucas", "lucas@example.com", "Lucas Gray", LocalDate.of(1988, 2, 14));
        Book book = createBook("444-555-666", "Spring Data JPA", 21);

        // Create and save loan
        BookLoan loan = new BookLoan();
        loan.setBorrower(borrower);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(7));
        loan.setReturned(false);
        repository.save(loan);

        // Run the update query
        repository.markAsReturned(loan.getId());

        // Flush and clear the persistence context to reload the updated entity
        entityManager.flush();
        entityManager.clear();

        // Verify the update
        Optional<BookLoan> updated = repository.findById(loan.getId());
        assertTrue(updated.isPresent());
        assertTrue(updated.get().isReturned());
    }
}

