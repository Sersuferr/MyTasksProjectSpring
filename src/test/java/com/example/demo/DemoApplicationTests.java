package com.example.demo;

import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testBookCreationAndRetrieval() {
        Author author = new Author("Тестовый Автор");
        author = authorRepository.save(author);

        Book book = new Book();
        book.setTitle("Тестовая книга");
        book.setIsbn("TEST-123");
        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("Тестовая книга");

        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("Тестовая книга");
    }

    @Test
    void testPagination() {
        Author author = authorRepository.save(new Author("Автор для пагинации"));

        for (int i = 1; i <= 15; i++) {
            Book book = new Book();
            book.setTitle("Книга " + i);
            book.setIsbn("ISBN-" + i);
            book.setAuthor(author);
            bookRepository.save(book);
        }

        Page<Book> firstPage = bookRepository.findAll(
                PageRequest.of(0, 5, Sort.by("title").ascending())
        );

        assertThat(firstPage.getContent()).hasSize(5);
        assertThat(firstPage.getTotalElements()).isGreaterThanOrEqualTo(15);
        assertThat(firstPage.getTotalPages()).isGreaterThanOrEqualTo(3);
    }

    @Test
    void testUpdateBook() {
        Author author = authorRepository.save(new Author("Автор для обновления"));
        Book book = bookRepository.save(new Book("Старое название", "OLD-ISBN", author));

        book.setTitle("Новое название");
        book.setIsbn("NEW-ISBN");
        Book updatedBook = bookRepository.save(book);

        assertThat(updatedBook.getTitle()).isEqualTo("Новое название");
        assertThat(updatedBook.getIsbn()).isEqualTo("NEW-ISBN");
    }

    @Test
    void testDeleteBook() {
        Author author = authorRepository.save(new Author("Автор для удаления"));
        Book book = bookRepository.save(new Book("Книга для удаления", "DELETE-ISBN", author));

        Long bookId = book.getId();

        bookRepository.deleteById(bookId);

        Optional<Book> deletedBook = bookRepository.findById(bookId);
        assertThat(deletedBook).isEmpty();
    }
}
