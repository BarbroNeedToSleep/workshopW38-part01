package se.lexicon.workshopweek38part01.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import se.lexicon.workshopweek38part01.entity.Details;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EntityScan("se.lexicon.workshopweek38part01.entity")
@ActiveProfiles("test")
class DetailsRepositoryTest {

    @Autowired
    private DetailsRepository detailsRepository;

    @Test
    void testFindByEmail() {
        Details details = Details.builder()
                .email("alice@example.com")
                .name("Alice Wonderland")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();

        detailsRepository.save(details);

        Optional<Details> found = detailsRepository.findByEmail("alice@example.com");
        assertTrue(found.isPresent());
        assertEquals("Alice Wonderland", found.get().getName());
    }

    @Test
    void testFindByNameContainsIgnoreCase() {
        Details details1 = Details.builder()
                .email("bob@example.com")
                .name("Bob Builder")
                .birthDate(LocalDate.of(1985, 5, 10))
                .build();

        Details details2 = Details.builder()
                .email("bobby@example.com")
                .name("Bobby Brown")
                .birthDate(LocalDate.of(1992, 7, 15))
                .build();

        detailsRepository.save(details1);
        detailsRepository.save(details2);

        List<Details> results = detailsRepository.findByNameContainsIgnoreCase("bob");
        assertEquals(2, results.size());
    }

    @Test
    void testFindByNameIgnoreCase() {
        Details details = Details.builder()
                .email("charlie@example.com")
                .name("Charlie Chaplin")
                .birthDate(LocalDate.of(1975, 3, 20))
                .build();

        detailsRepository.save(details);

        List<Details> results = detailsRepository.findByNameIgnoreCase("CHARLIE CHAPLIN");
        assertEquals(1, results.size());
        assertEquals("charlie@example.com", results.get(0).getEmail());
    }

}