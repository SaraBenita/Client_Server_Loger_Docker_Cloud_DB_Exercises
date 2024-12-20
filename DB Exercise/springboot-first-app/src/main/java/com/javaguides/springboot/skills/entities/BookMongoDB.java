package com.javaguides.springboot.skills.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "books")  // שם הקולקציה ב-MongoDB
public class BookMongoDB {

    @Id
    private String _id;  // תן ל-MongoDB ליצור את ה-ID שלו

    private Integer rawid; // שדה rawid, לא כ-ID
    private String title;
    private String author;
    private int year;
    private int price;
    private List<String> genres;  // genres יהיה מחרוזת

    // Constructors, Getters, Setters, ToString (לפי הצורך)
    public BookMongoDB() {}

    public BookMongoDB(Integer rawid, String title, String author, int year, int price, List<String> genres) {
        this.rawid = rawid;
        this.title = title;
        this.author = author;
        this.year = year;
        this.price = price;
        this.genres = genres;
    }

    // Getters and Setters
    public Integer getRawid() {
        return rawid;
    }

    public void setRawid(Integer rawid) {
        this.rawid = rawid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}
