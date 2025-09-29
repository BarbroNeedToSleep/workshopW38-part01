package se.lexicon.workshopweek38part01.repo;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import se.lexicon.workshopweek38part01.entity.Author;
import se.lexicon.workshopweek38part01.entity.Book;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan("se.lexicon.workshopweek38part01.entity")
@ActiveProfiles("test")
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    private Author author1;
    private Author author2;
    private Book book1;

    @BeforeEach
    void setUp() {
        authorRepository.deleteAll();
        bookRepository.deleteAll();

        // Create books manually
        book1 = Book.builder()
                .isbn("1234567890")
                .title("Java Basics")
                .maxLoanDays(14)
                .build();
        bookRepository.save(book1); // save first since no cascade

        // Create authors
        author1 = Author.builder().firstName("John").lastName("Doe").build();
        author2 = Author.builder().firstName("Jane").lastName("Smith").build();

        // Associate authors with book manually
        author1.getWrittenBooks().add(book1);
        author2.getWrittenBooks().add(book1);

        // Save authors manually
        authorRepository.save(author1);
        authorRepository.save(author2);
    }

    @Autowired
    private EntityManager entityManager;  // <--- inject Hibernate’s EntityManager

    @AfterEach
    void clearPersistenceContext() {
        entityManager.clear(); // clears Hibernate’s first-level cache
    }

    @Test
    void testFindByFirstNameContainingIgnoreCase() {
        List<Author> result = authorRepository.findByFirstNameContainingIgnoreCase("john");
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void testFindByLastNameContainingIgnoreCase() {
        List<Author> result = authorRepository.findByLastNameContainingIgnoreCase("smith");
        assertEquals(1, result.size());
        assertEquals("Smith", result.get(0).getLastName());
    }

    @Test
    void testFindByFirstNameOrLastNameContainingIgnoreCase() {
        List<Author> result = authorRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("jane", "doe");
        assertEquals(2, result.size());
    }

    @Test
    void testFindByWrittenBooksId() {
        List<Author> result = authorRepository.findByWrittenBooks_Id(book1.getId());
        assertEquals(2, result.size());
    }

    @Test
    void testUpdateNameById() {
        int updated = authorRepository.updateNameById(author1.getId(), "Johnny", "DoeUpdated");
        assertEquals(1, updated);

        Optional<Author> updatedAuthor = authorRepository.findById(author1.getId());
        assertTrue(updatedAuthor.isPresent());
        assertEquals("Johnny", updatedAuthor.get().getFirstName());
        assertEquals("DoeUpdated", updatedAuthor.get().getLastName());
    }

    @Test
    void testDeleteByIdCustom() {
        int deleted = authorRepository.deleteByIdCustom(author2.getId());
        assertEquals(1, deleted);

        Optional<Author> deletedAuthor = authorRepository.findById(author2.getId());
        assertFalse(deletedAuthor.isPresent());
    }
}
