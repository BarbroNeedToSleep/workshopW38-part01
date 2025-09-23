package se.lexicon.workshopweek38part01.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.lexicon.workshopweek38part01.entity.AppUser;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    Optional<AppUser> findByUsername(String username);

    @Query("SELECT u FROM AppUser u WHERE u.regDate BETWEEN :startDate AND :endDate")
    List<AppUser> findByRegDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    Optional<AppUser> findByUserDetails_Id(Integer detailsId);

    @Query("SELECT u FROM AppUser u WHERE LOWER(u.userDetails.email) = LOWER(:email)")
    Optional<AppUser> findByUserDetails_EmailIgnoreCase(@Param("email") String email);
}
