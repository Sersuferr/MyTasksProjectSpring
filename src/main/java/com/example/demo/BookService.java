package com.example.demo;

import org.springframework.stereotype.Service;
import java.util.List;

@Service // Помечаем как сервисный компонент Spring
public class BookService {

    private final BookRepository bookRepository;

    // Конструктор для внедрения зависимости (Dependency Injection)
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Создание новой книги
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    // Получение книги по ID
    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена с id: " + id));
    }

    // Получение всех книг
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Обновление книги
    public Book updateBook(Long id, Book bookDetails) {
        Book book = getBook(id); // Получаем существующую книгу
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setPublicationYear(bookDetails.getPublicationYear());
        return bookRepository.save(book);
    }

    // Удаление книги
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}