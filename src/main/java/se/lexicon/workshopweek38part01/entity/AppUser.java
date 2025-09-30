package se.lexicon.workshopweek38part01.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, updatable = false)
    private LocalDate regDate;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "details_id", nullable = false, unique = true)
    private Details userDetails;

    @OneToMany(mappedBy = "borrower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookLoan> bookLoans = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        if (regDate == null) {
            regDate = LocalDate.now();
        }
    }

    public AppUser(String username, String password, Details userDetails) {
        this.username = username;
        this.password = password;
        this.userDetails = userDetails;
    }

    public void addBookLoan(BookLoan loan) {
        bookLoans.add(loan);
        loan.setBorrower(this);
    }

    public void removeBookLoan(BookLoan loan) {
        bookLoans.remove(loan);
        loan.setBorrower(null);
    }



}
