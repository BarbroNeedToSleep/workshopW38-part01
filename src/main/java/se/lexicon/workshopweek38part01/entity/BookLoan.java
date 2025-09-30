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

public class BookLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, updatable = false)
    private LocalDate loanDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private boolean returned;

    @ManyToOne
    @JoinColumn(name = "borrower_id", nullable = false)
    private AppUser borrower;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public BookLoan(LocalDate dueDate, boolean returned, AppUser borrower, Book book) {
        this.dueDate = dueDate;
        this.returned = returned;
        this.borrower = borrower;
        this.book = book;
    }

    // --- Constructor that calculates dueDate automatically ---
    public BookLoan(Book book, AppUser borrower) {
        this.book = book;
        this.borrower = borrower;
        this.loanDate = LocalDate.now();
        this.dueDate = loanDate.plusDays(book.getMaxLoanDays());
        this.returned = false;
    }

    // --- PrePersist hook as a safety net ---
    @PrePersist
    public void onCreate() {
        if (loanDate == null) {
            loanDate = LocalDate.now();
        }
        if (book != null && dueDate == null) {
            dueDate = loanDate.plusDays(book.getMaxLoanDays());
        }
    }

}
