package se.lexicon.workshopweek38part01.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import se.lexicon.workshopweek38part01.entity.Book;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest

@EntityScan("se.lexicon.workshopweek38part01.entity")
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void testFindByIsbnIgnoreCase() {
        Book book = new Book("12345", "Spring Boot Guide", 14);
        bookRepository.save(book);

        // Search for ISBN
        List<Book> found = bookRepository.findByIsbnIgnoreCase("12345");
        assertFalse(found.isEmpty());
        assertEquals("Spring Boot Guide", found.get(0).getTitle());

        // Case-insensitive test
        found = bookRepository.findByIsbnIgnoreCase("12345".toUpperCase());
        assertFalse(found.isEmpty());
        assertEquals("Spring Boot Guide", found.get(0).getTitle());

        // Optional: add a second book with same ISBN to test multiple results
        Book book2 = new Book("12345", "Another Book", 10);
        bookRepository.save(book2);

        found = bookRepository.findByIsbnIgnoreCase("12345");
        assertEquals(2, found.size());
    }


    @Test
    void testFindByTitleContainingIgnoreCase() {
        Book book1 = new Book("111", "Java Basics", 10);
        Book book2 = new Book("222", "Advanced Java", 15);
        bookRepository.save(book1);
        bookRepository.save(book2);

        List<Book> found = bookRepository.findByTitleContainingIgnoreCase("java");
        assertEquals(2, found.size());

        found = bookRepository.findByTitleContainingIgnoreCase("advanced");
        assertEquals(1, found.size());
        assertEquals("Advanced Java", found.get(0).getTitle());
    }

    @Test
    void testFindByMaxLoanDaysLessThan() {
        Book book1 = new Book("111", "Short Loan", 7);
        Book book2 = new Book("222", "Medium Loan", 14);
        Book book3 = new Book("333", "Long Loan", 21);
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);

        List<Book> shortBooks = bookRepository.findByMaxLoanDaysLessThan(15);
        assertEquals(2, shortBooks.size());
        assertTrue(shortBooks.stream().anyMatch(b -> b.getTitle().equals("Short Loan")));
        assertTrue(shortBooks.stream().anyMatch(b -> b.getTitle().equals("Medium Loan")));
    }
}
