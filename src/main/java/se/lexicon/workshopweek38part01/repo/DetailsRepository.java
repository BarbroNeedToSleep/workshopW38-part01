package se.lexicon.workshopweek38part01.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.lexicon.workshopweek38part01.entity.Details;

import java.util.List;
import java.util.Optional;

public interface DetailsRepository extends JpaRepository<Details, Integer> {

    Optional<Details>findByEmail(String email);

//    List<Details>findByNameContains(String name);

    @Query("SELECT d FROM Details d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Details> findByNameContainsIgnoreCase(@Param("name") String name);

    @Query("SELECT d FROM Details d WHERE LOWER(d.name) = LOWER(:name)")
    List<Details> findByNameIgnoreCase(@Param("name") String name);


}
