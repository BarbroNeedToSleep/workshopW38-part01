package se.lexicon.workshopweek38part01.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity

@Getter
@Setter
@NoArgsConstructor


public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "author_id"), // this entity
            inverseJoinColumns = @JoinColumn(name = "book_id") // the other entity
    )
    private Set<Book> writtenBooks = new HashSet<>();

    @Builder
    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void addBook(Book book) {
        writtenBooks.add(book);
        book.getAuthors().add(this);
    }

    public void removeBook(Book book) {
        writtenBooks.remove(book);
        book.getAuthors().remove(this);
    }
}
