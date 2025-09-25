package se.lexicon.workshopweek38part01.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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



}
