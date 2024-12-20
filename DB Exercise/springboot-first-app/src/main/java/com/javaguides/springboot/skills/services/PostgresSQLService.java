package com.javaguides.springboot.skills.services;
import com.javaguides.springboot.skills.entities.BookMongoDB;
import com.javaguides.springboot.skills.entities.BookPostgresSQL;
import com.javaguides.springboot.skills.model.Book;
import com.javaguides.springboot.skills.model.Genre;
import com.javaguides.springboot.skills.repositories.BookPostgresSQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostgresSQLService {

    @Autowired
    private BookPostgresSQLRepository postgresBookRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // יצירת ספר חדש
    public BookPostgresSQL createBook(BookPostgresSQL book) {
        return postgresBookRepository.save(book);
    }
    public BookPostgresSQL saveBook(BookPostgresSQL book) {
        // שמירה במסד הנתונים
        return postgresBookRepository.save(book); // זהו המהדר שיבצע את השמירה בפועל
    }

    // קבלת כל הספרים
    public List<BookPostgresSQL> getAllBooks() {
        return postgresBookRepository.findAll();
    }

    // חיפוש ספר לפי ID
    public Optional<BookPostgresSQL> getBookById(int rawid) {
        return postgresBookRepository.findById(rawid);
    }

    // עדכון ספר
    public BookPostgresSQL updateBook(int rawid, BookPostgresSQL bookDetails) {
        Optional<BookPostgresSQL> optionalBook = postgresBookRepository.findById(rawid);
        if (optionalBook.isPresent()) {
            BookPostgresSQL book = optionalBook.get();
            book.setId(bookDetails.getId());
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setYear(bookDetails.getYear());
            book.setPrice(bookDetails.getPrice());
            book.setGenres(bookDetails.getGenres());
            return postgresBookRepository.save(book);
        }
        return null; // אם לא נמצא הספר
    }

    // מחיקת ספר לפי ID
    public boolean deleteBook(int rawid) {
        Optional<BookPostgresSQL> optionalBook = postgresBookRepository.findById(rawid);
        if (optionalBook.isPresent()) {
            postgresBookRepository.delete(optionalBook.get());
            return true;
        }
        return false; // אם לא נמצא הספר למחיקה
    }
    public boolean existsByTitleIgnoreCase(String title) {
        return postgresBookRepository.existsByTitleIgnoreCase(title);
    }
    public Integer getLastBookId() {
        String sql = "SELECT MAX(rawid) FROM books";  // החלף ב-name של טבלת הספרים שלך
        try {
            // ביצוע השאילתה ב-Postgres
            Integer lastId = jdbcTemplate.queryForObject(sql, Integer.class);
            return (lastId == null) ? 0 : lastId;  // אם לא נמצא ספרים, נחזיר 0
        } catch (Exception e) {
            // טיפול בשגיאות
            return 0;  // אם הייתה שגיאה, נחזיר 0
        }
    }

    // המרה לBookPostgresSQL
    public BookPostgresSQL convertToPostgresSQL(Book book) {
        BookPostgresSQL bookPostgresSQL = new BookPostgresSQL();
        bookPostgresSQL.setId(book.getId());
        bookPostgresSQL.setTitle(book.getTitle());
        bookPostgresSQL.setAuthor(book.getAuthor());
        bookPostgresSQL.setYear(book.getYear());
        bookPostgresSQL.setPrice(book.getPrice());

        if (book.getGenres() != null && !book.getGenres().isEmpty()) {
            // המרת רשימת הג'אנרים (Enum) למחרוזת אחת מופרדת בפסיקים
            String genresString = "[" + book.getGenres().stream()
                    .map(genre -> "\"" + genre + "\"")  // המרת כל Genre למחרוזת עם גרשיים
                    .collect(Collectors.joining(",")) + "]";
            bookPostgresSQL.setGenres(genresString);
        } else {
            bookPostgresSQL.setGenres("[]"); // אם אין ג'אנרים, שמור מחרוזת JSON ריקה
        }

        return bookPostgresSQL;
    }

    // המרה ל Book
    public Book convertToBook(Optional<BookPostgresSQL> optionalBookPostgresSQL) {
        // אם ה-Optional ריק, נחזיר null
        if (!optionalBookPostgresSQL.isPresent()) {
            return null;
        }

        // אם ה-Optional לא ריק, מבצעים את ההמרה
        BookPostgresSQL bookPostgresSQL = optionalBookPostgresSQL.get();
        Book book = new Book();

        book.setId(bookPostgresSQL.getId()); // השמת ID
        book.setTitle(bookPostgresSQL.getTitle());
        book.setAuthor(bookPostgresSQL.getAuthor());
        book.setYear(bookPostgresSQL.getYear());
        book.setPrice(bookPostgresSQL.getPrice());

        // המרת genres ממחרוזת מופרדת בפסיקים לרשימה של Genre (Enum)
        if (bookPostgresSQL.getGenres() != null && !bookPostgresSQL.getGenres().isEmpty()) {
            // פיצול המחרוזת לפי פסיקים, הסרת הסוגריים המרובעים והמרת כל חלק ל-Enum המתאים
            List<Genre> genres = Arrays.stream(bookPostgresSQL.getGenres().split(","))
                    .map(genre -> Genre.valueOf(genre.replaceAll("[\\[\\]\"]", "").trim()))  // הסרת הסוגריים המרובעים
                    .collect(Collectors.toList()); // איסוף לרשימה של Genre
            book.setGenre(genres);
        }

        return book;
    }

    public int getBooksCount(String author, Integer priceBiggerThan, Integer priceLessThan,
                             Integer yearBiggerThan, Integer yearLessThan, String genres) {
        // אם לא עברו פרמטרים מסוימים, תחזור על כל הספרים
        return postgresBookRepository.countBooksWithFilters(author, priceBiggerThan, priceLessThan, yearBiggerThan, yearLessThan, genres);
    }

    public List<Book> getBooks(String author, Integer priceBiggerThan, Integer priceLessThan,
                               Integer yearBiggerThan, Integer yearLessThan, String genres) {
        // קריאה לפונקציה בריפוזיטורי עם כל הקריטריונים
        List<BookPostgresSQL> books = postgresBookRepository.findBooksByFilters(
                author,
                priceBiggerThan,
                priceLessThan,
                yearBiggerThan,
                yearLessThan,
                genres
        );

        // המרה מ-BookPostgresSQL ל-Book
        return books.stream()
                .map(book -> convertToBook(Optional.ofNullable(book)))  // עוטפת כל אובייקט ב-Optional
                .collect(Collectors.toList());
    }
}
