package com.javaguides.springboot.skills.Controller;

import com.javaguides.springboot.skills.model.Book;
import com.javaguides.springboot.skills.model.Genre;
import com.javaguides.springboot.skills.model.ServerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@RestController
public class ServletsController {
    ServerUtils serverUtils = new ServerUtils(); //contain all books in the server and index++.
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

        // Validate if the book already exists (case insensitive comparison)
        for (Book book : serverUtils.getSetBooks()) {
            if (book.getTitle().equalsIgnoreCase(bookRequest.getTitle())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ServerResponse(null, "Error: Book with the title [" + bookRequest.getTitle() + "] already exists in the system"));
            }
        }

        bookRequest.setId(serverUtils.getId() + 1);
        serverUtils.setId(serverUtils.getId() + 1);
        serverUtils.getSetBooks().add(bookRequest);
        return ResponseEntity.ok().body(new ServerResponse((serverUtils.getId()), null));
    }
    @GetMapping("/books/total")
    public ResponseEntity<ServerResponse> getTotalBooks(@RequestParam(required = false) String author,
                                                        @RequestParam(name = "price-bigger-than", required = false) Integer priceBiggerThan,
                                                        @RequestParam(name = "price-less-than", required = false) Integer priceLessThan,
                                                        @RequestParam(name = "year-bigger-than", required = false) Integer yearBiggerThan,
                                                        @RequestParam(name = "year-less-than", required = false) Integer yearLessThan,
                                                        @RequestParam(required = false) String genres) {
        // Validate genres
        if (genres != null) {
            for (String genre : genres.split(",")) {
                if (Genre.getGenre(genre) == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ServerResponse(null, null));
                }
            }
        }

        int totalCount = 0;

        // Iterate through all books and apply filters
        for (Book book : serverUtils.getSetBooks()) {
            if ((author == null || book.getAuthor().equalsIgnoreCase(author))
                    && (priceBiggerThan == null || book.getPrice() >= priceBiggerThan)
                    && (priceLessThan == null || book.getPrice() <= priceLessThan)
                    && (yearBiggerThan == null || book.getYear() >= yearBiggerThan)
                    && (yearLessThan == null || book.getYear() <= yearLessThan)
                    && (genres == null || containsAnyGenre(book, genres))) {
                totalCount++;
            }
        }

        // If all filter variables are null, return a list containing all books in the system
        if (author == null && priceBiggerThan == null && priceLessThan == null && yearBiggerThan == null && yearLessThan == null && genres == null) {
            totalCount = serverUtils.getSetBooks().size();
        }

        return ResponseEntity.ok().body(new ServerResponse(totalCount, null));
    }

    @GetMapping("/books")
    public ResponseEntity<ServerResponse> getBooksData(@RequestParam(required = false) String author,
                                                       @RequestParam(name = "price-bigger-than", required = false) Integer priceBiggerThan,
                                                       @RequestParam(name = "price-less-than", required = false) Integer priceLessThan,
                                                       @RequestParam(name = "year-bigger-than", required = false) Integer yearBiggerThan,
                                                       @RequestParam(name = "year-less-than", required = false) Integer yearLessThan,
                                                       @RequestParam(required = false) String genres) {
        // Validate genres
        if (genres != null) {
            for (String genre : genres.split(",")) {
                if (Genre.getGenre(genre) == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ServerResponse(null, "Error: Bad Request")); // לבדוק אם צריך לחזור
                }
            }
        }

        // Iterate through all books and apply filters
        List<Book> filteredBooks = new ArrayList<>();
        for (Book book : serverUtils.getSetBooks()) {
            if ((author == null || book.getAuthor().equalsIgnoreCase(author))
                    && (priceBiggerThan == null || book.getPrice() >= priceBiggerThan)
                    && (priceLessThan == null || book.getPrice() <= priceLessThan)
                    && (yearBiggerThan == null || book.getYear() >= yearBiggerThan)
                    && (yearLessThan == null || book.getYear() <= yearLessThan)
                    && (genres == null || containsAnyGenre(book, genres))) {

                filteredBooks.add(book);
            }
        }

        // If all filter variables are null, return a list containing all books in the system
        if (author == null && priceBiggerThan == null && priceLessThan == null && yearBiggerThan == null && yearLessThan == null && genres == null) {
            filteredBooks.clear();
            filteredBooks.addAll(serverUtils.getSetBooks());
        }

        // Sort the filtered list by the book's title (case-insensitive)
        filteredBooks.sort(Comparator.comparing(book -> book.getTitle().toLowerCase()));

        return ResponseEntity.ok().body(new ServerResponse(filteredBooks, null));
    }

    @GetMapping("/book")
    public ResponseEntity<ServerResponse> getSingleBookData(@RequestParam Integer id) {

        Book optionalBook = null;
        for (Book book : serverUtils.getSetBooks()) {
            if (book.getId() == id) {
                optionalBook = book; // Book found
            }
        }

        if(optionalBook == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ServerResponse(null, "Error: no such Book with id " + id));
        else
            return ResponseEntity.ok().body(new ServerResponse(optionalBook, null));

    }

    @PutMapping("/book")
    public ResponseEntity<ServerResponse> updateBooksPrice(@RequestParam Integer id, @RequestParam Integer price){

        Book optionalBook = null;
        for (Book book : serverUtils.getSetBooks()) {
            if (book.getId() == id) {
                optionalBook = book; // Book found
            }
        }

        if(optionalBook == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ServerResponse(null, "Error: no such Book with id " + id));
        else if(price <= 0)
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ServerResponse(null, "Error: price update for book [" + id + "] must be a positive integer"));
        else{
            int oldPrice = optionalBook.getPrice();
            optionalBook.setPrice(price);
            return ResponseEntity.ok().body(new ServerResponse(oldPrice, null));
        }
    }

    @DeleteMapping("/book")
    public ResponseEntity<ServerResponse> deleteBook(@RequestParam Integer id) {

        Iterator<Book> iterator = serverUtils.getSetBooks().iterator();
        boolean bookFound = false;

        while (iterator.hasNext()) {
            Book book = iterator.next();
            if (book.getId() == id) {
                iterator.remove(); // Remove the book from the list
                bookFound = true;
                break;
            }
        }

        if (!bookFound) {
            // Book not found, return 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ServerResponse(null, "Error: no such Book with id " + id));
        } else {
            // Book deleted successfully, return the updated count of books
            int totalBooksInStoreAfterDelete = serverUtils.getSetBooks().size();
            return ResponseEntity.ok().body(new ServerResponse(totalBooksInStoreAfterDelete, null));
        }
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
