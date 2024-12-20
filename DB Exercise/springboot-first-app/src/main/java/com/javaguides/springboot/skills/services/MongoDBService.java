package com.javaguides.springboot.skills.services;
import com.javaguides.springboot.skills.entities.BookMongoDB;
import com.javaguides.springboot.skills.model.Book;
import com.javaguides.springboot.skills.model.Genre;
import com.javaguides.springboot.skills.repositories.BookMongoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MongoDBService{

    @Autowired
    private BookMongoDBRepository mongoBookRepository;

    // יצירת ספר חדש
    public BookMongoDB createBook(BookMongoDB book) {
        return mongoBookRepository.save(book);
    }

    public BookMongoDB saveBook(BookMongoDB book) {
        // שמירה במסד הנתונים
        return mongoBookRepository.save(book); // שמירה ב-MongoDB
    }

    // קבלת כל הספרים
    public List<BookMongoDB> getAllBooks() {
        return mongoBookRepository.findAll();
    }

    // חיפוש ספר לפי ID
    public Optional<BookMongoDB> getBookByRawid(Integer rawid) {
        return mongoBookRepository.findByRawid(rawid);
    }

    // עדכון ספר
    public BookMongoDB updateBook(Integer rawid, BookMongoDB bookDetails) {
        Optional<BookMongoDB> optionalBook = mongoBookRepository.findByRawid(rawid);
        if (optionalBook.isPresent()) {
            BookMongoDB book = optionalBook.get();
            book.setRawid(bookDetails.getRawid()); // עדכון ה rawid
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setYear(bookDetails.getYear());
            book.setPrice(bookDetails.getPrice());
            book.setGenres(bookDetails.getGenres());
            return mongoBookRepository.save(book);
        }
        return null; // אם לא נמצא הספר
    }

    // מחיקת ספר לפי ID
    public boolean deleteBook(Integer rawid) {
        Optional<BookMongoDB> optionalBook = mongoBookRepository.findByRawid(rawid);
        if (optionalBook.isPresent()) {
            mongoBookRepository.delete(optionalBook.get());
            return true;
        }
        return false; // אם לא נמצא הספר למחיקה
    }
    public boolean existsByTitleIgnoreCase(String title) {
        return mongoBookRepository.existsByTitleIgnoreCase(title);
    }

    // המרה לBOOK
    public Book convertToBook(Optional<BookMongoDB> optionalBookMongoDB) {
        // אם ה-Optional ריק, נחזיר null
        if (!optionalBookMongoDB.isPresent()) {
            return null;
        }

        // אם ה-Optional לא ריק, מבצעים את ההמרה
        BookMongoDB bookMongoDB = optionalBookMongoDB.get();
        Book book = new Book();
        book.setId(bookMongoDB.getRawid());
        book.setTitle(bookMongoDB.getTitle());
        book.setAuthor(bookMongoDB.getAuthor());
        book.setYear(bookMongoDB.getYear());
        book.setPrice(bookMongoDB.getPrice());

        // המרת genres מרשימה של String לרשימה של Genre (Enum)
        if (bookMongoDB.getGenres() != null && !bookMongoDB.getGenres().isEmpty()) {
            List<Genre> genres = bookMongoDB.getGenres().stream()
                    .map(genre -> Genre.valueOf(genre.trim())) // המרת כל String לערך Enum
                    .collect(Collectors.toList());
            book.setGenre(genres);
        }

        return book;
    }

    // המרה ל BookMongoDB
    public BookMongoDB convertToMongo(Book book) {
        BookMongoDB bookMongoDB = new BookMongoDB();
        bookMongoDB.setRawid(book.getId());
        bookMongoDB.setTitle(book.getTitle());
        bookMongoDB.setAuthor(book.getAuthor());
        bookMongoDB.setYear(book.getYear());
        bookMongoDB.setPrice(book.getPrice());

        // המרת רשימת Genre (Enum) לרשימה של מחרוזות
        if (book.getGenres() != null && !book.getGenres().isEmpty()) {
            List<String> genres = book.getGenres().stream()
                    .map(Genre::name) // המרת כל ערך Enum למחרוזת
                    .collect(Collectors.toList());
            bookMongoDB.setGenres(genres); // שמירת הרשימה ב-BookMongoDB
        } else {
            bookMongoDB.setGenres(Collections.emptyList()); // אם אין ג'אנרים, שמור רשימה ריקה
        }

        return bookMongoDB;
    }
    public long countBooksWithFilters(String author, Integer priceBiggerThan, Integer priceLessThan,
                                      Integer yearBiggerThan, Integer yearLessThan, String genres) {
        List<BookMongoDB> books = new ArrayList<>();

        // אם כל הפרמטרים הם null, אז נבצע חיפוש ללא סינון
        if (author == null && priceBiggerThan == null && priceLessThan == null
                && yearBiggerThan == null && yearLessThan == null && genres == null) {
            // קריאה למסד הנתונים כדי לקבל את כל הספרים
            books.addAll(mongoBookRepository.findAll());
        } else {
            // חיפוש לפי פרמטרים
            if (author != null && !author.isEmpty()) {
                books.addAll(mongoBookRepository.findByAuthorLike(author));
            }
            if (priceBiggerThan != null) {
                books.addAll(mongoBookRepository.findByPriceGreaterThan(priceBiggerThan));
            }
            if (priceLessThan != null) {
                books.addAll(mongoBookRepository.findByPriceLessThan(priceLessThan));
            }
            if (yearBiggerThan != null) {
                books.addAll(mongoBookRepository.findByYearGreaterThan(yearBiggerThan));
            }
            if (yearLessThan != null) {
                books.addAll(mongoBookRepository.findByYearLessThan(yearLessThan));
            }
            if (genres != null && !genres.isEmpty()) {
                // המרת המערך List<String> שנוצר מהפיצול
                List<String> genreList = Arrays.asList(genres.split(","));
                books.addAll(mongoBookRepository.findByGenresIn(genreList));
            }
        }

        return books.stream()
                .filter(book -> author == null || book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .filter(book -> priceBiggerThan == null || book.getPrice() > priceBiggerThan)
                .filter(book -> priceLessThan == null || book.getPrice() < priceLessThan)
                .filter(book -> yearBiggerThan == null || book.getYear() > yearBiggerThan)
                .filter(book -> yearLessThan == null || book.getYear() < yearLessThan)
                .filter(book -> genres == null || book.getGenres().stream()
                        .anyMatch(genre -> genre.toLowerCase().contains(genres.toLowerCase())))
                .count();
    }


    public List<Book> getBooks(String author, Integer priceBiggerThan, Integer priceLessThan,
                               Integer yearBiggerThan, Integer yearLessThan, String genres) {

        // נניח שברשותנו חיפוש עבור כל קריטריון שנמצא
        List<BookMongoDB> books = new ArrayList<>();

        // אם כל הפרמטרים הם null, אז נבצע חיפוש ללא סינון
        if (author == null && priceBiggerThan == null && priceLessThan == null
                && yearBiggerThan == null && yearLessThan == null && genres == null) {
            // קריאה למסד הנתונים כדי לקבל את כל הספרים
            books.addAll(mongoBookRepository.findAll());
        } else {

            if (author != null && !author.isEmpty()) {
                books.addAll(mongoBookRepository.findByAuthorLikeIgnoreCase(author));
            }

            if (priceBiggerThan != null && priceLessThan != null) {
                books.addAll(mongoBookRepository.findByPriceGreaterThanAndPriceLessThan(priceBiggerThan, priceLessThan));
            }

            if (yearBiggerThan != null && yearLessThan != null) {
                books.addAll(mongoBookRepository.findByYearGreaterThanAndYearLessThan(yearBiggerThan, yearLessThan));
            }

            if (genres != null && !genres.isEmpty()) {
                books.addAll(mongoBookRepository.findByGenresLikeIgnoreCase(genres));
            }
        }

        // מיפוי אובייקטים ל-Book
        return books.stream()
                .map(bookMongoDB -> convertToBook(Optional.ofNullable(bookMongoDB)))
                .collect(Collectors.toList());
    }
}