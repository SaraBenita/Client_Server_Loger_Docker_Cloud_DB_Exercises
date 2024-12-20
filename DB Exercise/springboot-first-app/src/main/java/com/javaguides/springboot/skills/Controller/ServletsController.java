package com.javaguides.springboot.skills.Controller;

import com.javaguides.springboot.skills.entities.BookMongoDB;
import com.javaguides.springboot.skills.entities.BookPostgresSQL;
import com.javaguides.springboot.skills.model.Book;
import com.javaguides.springboot.skills.model.Genre;
import com.javaguides.springboot.skills.model.ServerResponse;
import com.javaguides.springboot.skills.services.MongoDBService;
import com.javaguides.springboot.skills.services.PostgresSQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@RestController
public class ServletsController {
    ServerUtils serverUtils = new ServerUtils(); //contain all books in the server and index++.

    @Autowired
    private MongoDBService mongoDBService;
    @Autowired
    private PostgresSQLService postgresSQLService;


    @GetMapping("/books/health")
    public String checkingIfServerIsUp() {
        return "OK";
    }
    @PostMapping(value = "/book", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ServerResponse> createBook(@RequestBody Book bookRequest) {
        // Validate if the year is within the accepted range
        if (bookRequest.getYear() < 1940 || bookRequest.getYear() > 2100) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ServerResponse(null, "Error: Can’t create new Book that its year [" + bookRequest.getYear() + "] is not in the accepted range [1940 -> 2100]"));
        }

        // Validate if the price is positive
        if (bookRequest.getPrice() < 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ServerResponse(null, "Error: Can’t create new Book with negative price"));
        }

        // Save to both databases (Postgres and Mongo)
        try {

            boolean bookExistsInPostgres = postgresSQLService.existsByTitleIgnoreCase(bookRequest.getTitle());
            boolean bookExistsInMongo = mongoDBService.existsByTitleIgnoreCase(bookRequest.getTitle());

            if (bookExistsInPostgres || bookExistsInMongo) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ServerResponse(null, "Error: Book with the title [" + bookRequest.getTitle() + "] already exists in the system"));
            }
            // שלב 1: קבלת ה-ID האחרון מה-Postgres
            Integer lastId = postgresSQLService.getLastBookId();

            // שלב 2: הגדרת ה-ID של הספר החדש
            bookRequest.setId(lastId + 1);

            // המרה לשני סוגי הספרים ושמירה בכל מסד נתונים
            BookPostgresSQL bookPostgresSQL = postgresSQLService.convertToPostgresSQL(bookRequest);
            BookMongoDB bookMongoDB = mongoDBService.convertToMongo(bookRequest);

            postgresSQLService.saveBook(bookPostgresSQL);
            mongoDBService.saveBook(bookMongoDB);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ServerResponse(null, "Error saving book in both databases: " + e.getMessage()));
        }

        return ResponseEntity.ok().body(new ServerResponse(bookRequest.getId(), null));
    }


    @GetMapping("/books/total")
    public ResponseEntity<ServerResponse> getTotalBooks(@RequestParam(required = false) String author,
                                                        @RequestParam(name = "price-bigger-than", required = false) Integer priceBiggerThan,
                                                        @RequestParam(name = "price-less-than", required = false) Integer priceLessThan,
                                                        @RequestParam(name = "year-bigger-than", required = false) Integer yearBiggerThan,
                                                        @RequestParam(name = "year-less-than", required = false) Integer yearLessThan,
                                                        @RequestParam(required = false) String genres,
                                                        @RequestParam String persistenceMethod) {
        // Validate genres
        if (genres != null) {
            for (String genre : genres.split(",")) {
                if (Genre.getGenre(genre) == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ServerResponse(null, null));
                }
            }
        }

        long totalCount = 0;

        if (persistenceMethod.equals("POSTGRES")) {
            totalCount = postgresSQLService.getBooksCount(author, priceBiggerThan, priceLessThan, yearBiggerThan, yearLessThan, genres);
        } else if (persistenceMethod.equals("MONGO")) {
            totalCount = mongoDBService.countBooksWithFilters(author, priceBiggerThan, priceLessThan, yearBiggerThan, yearLessThan, genres);
        }

        return ResponseEntity.ok().body(new ServerResponse((int)totalCount, null));
    }


    @GetMapping("/books")
    public ResponseEntity<ServerResponse> getBooksData(@RequestParam(required = false) String author,
                                                       @RequestParam(name = "price-bigger-than", required = false) Integer priceBiggerThan,
                                                       @RequestParam(name = "price-less-than", required = false) Integer priceLessThan,
                                                       @RequestParam(name = "year-bigger-than", required = false) Integer yearBiggerThan,
                                                       @RequestParam(name = "year-less-than", required = false) Integer yearLessThan,
                                                       @RequestParam(required = false) String genres,
                                                       @RequestParam String persistenceMethod) {
        // Validate genres
        if (genres != null) {
            for (String genre : genres.split(",")) {
                if (Genre.getGenre(genre) == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ServerResponse(null, "Error: Bad Request")); // לבדוק אם צריך לחזור
                }
            }
        }

        List<Book> books = new ArrayList<>();

        if (persistenceMethod.equals("POSTGRES")) {
            books = postgresSQLService.getBooks(author, priceBiggerThan, priceLessThan, yearBiggerThan, yearLessThan, genres);
        } else if (persistenceMethod.equals("MONGO")) {
            books = mongoDBService.getBooks(author, priceBiggerThan, priceLessThan, yearBiggerThan, yearLessThan, genres);
        }

        // Sort the filtered list by the book's title (case-insensitive)
        books.sort(Comparator.comparing(book -> book.getTitle().toLowerCase()));

        return ResponseEntity.ok().body(new ServerResponse(books, null));
    }


    @GetMapping("/book")
    public ResponseEntity<ServerResponse> getSingleBookData(@RequestParam Integer id, @RequestParam String persistenceMethod) {
        Book book = null;
        Optional<BookPostgresSQL> bookPostgresSQL = null;
        Optional<BookMongoDB> bookMongoDB = null;


        if (persistenceMethod.equals("POSTGRES")) {
            bookPostgresSQL = postgresSQLService.getBookById(id);
            book = postgresSQLService.convertToBook(bookPostgresSQL);

        } else if (persistenceMethod.equals("MONGO")) {
            bookMongoDB = mongoDBService.getBookByRawid(id);
            book = mongoDBService.convertToBook(bookMongoDB);
        }

        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ServerResponse(null, "Error: no such Book with id " + id));
        }

        return ResponseEntity.ok().body(new ServerResponse(book, null));
    }


    @PutMapping("/book")
    public ResponseEntity<ServerResponse> updateBooksPrice(@RequestParam Integer id, @RequestParam Integer price) {

        Optional<BookPostgresSQL> optionalBookPostgresSQL = postgresSQLService.getBookById(id);
        Book book = postgresSQLService.convertToBook(optionalBookPostgresSQL);

        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ServerResponse(null, "Error: no such Book with id " + id));
        }

        if (price <= 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ServerResponse(null, "Error: price update for book [" + id + "] must be a positive integer"));
        }

        // Update price in both databases
        int oldPrice = book.getPrice();
        book.setPrice(price);
        BookPostgresSQL bookPostgresSQL = postgresSQLService.convertToPostgresSQL(book);
        BookMongoDB bookMongoDB = mongoDBService.convertToMongo(book);

        postgresSQLService.updateBook(bookPostgresSQL.getId(), bookPostgresSQL);
        mongoDBService.updateBook(bookMongoDB.getRawid(), bookMongoDB);

        return ResponseEntity.ok().body(new ServerResponse(oldPrice, null));
    }

    @DeleteMapping("/book")
    public ResponseEntity<ServerResponse> deleteBook(@RequestParam Integer id) {
        Optional<BookPostgresSQL> optionalBookPostgresSQL = postgresSQLService.getBookById(id);
        Book book = postgresSQLService.convertToBook(optionalBookPostgresSQL);

        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ServerResponse(null, "Error: no such Book with id " + id));
        }

        // Delete from both databases

        postgresSQLService.deleteBook(id);
        mongoDBService.deleteBook(id);
        int totalBooksInStoreAfterDelete = postgresSQLService.getAllBooks().size();

        return ResponseEntity.ok().body(new ServerResponse(totalBooksInStoreAfterDelete, null));
    }

    // Helper method to check if a book contains any of the given genres
    private boolean containsAnyGenre(Book book, String genres) {
        for (String genreName : genres.split(",")) {
            Genre genre = Genre.getGenre(genreName.trim()); // Trim to remove any leading/trailing whitespace
            if (genre != null && book.getGenres().contains(genre)) {
                return true;
            }
        }
        return false;
    }

}
