package se.lexicon.workshopweek38part01.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import se.lexicon.workshopweek38part01.entity.Author;
import java.util.List;


public interface AuthorRepository extends JpaRepository<Author, Integer> {

    List<Author> findByFirstNameContainingIgnoreCase(String firstName);

    List<Author> findByLastNameContainingIgnoreCase(String lastName);

    List<Author>
        findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase
            (String firstName, String lastName);

    List<Author> findByWrittenBooks_Id(Integer bookId);

    @Transactional
    @Modifying(clearAutomatically = true)  //For testing purples
    @Query("UPDATE Author a SET a.firstName = :firstName, a.lastName = :lastName WHERE a.id = :id")
    int updateNameById(Integer id, String firstName, String lastName);

    @Transactional
    @Modifying(clearAutomatically = true) // For testing purples
    @Query("DELETE FROM Author a WHERE a.id = :id")
    int deleteByIdCustom(Integer id);

}
