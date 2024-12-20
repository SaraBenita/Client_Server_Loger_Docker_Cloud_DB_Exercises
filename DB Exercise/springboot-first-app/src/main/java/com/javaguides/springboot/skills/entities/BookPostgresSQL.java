package com.javaguides.springboot.skills.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "books")  // שם הטבלה
public class BookPostgresSQL {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rawid")  // שם עמודה ב-DB
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "year")
    private int year;

    @Column(name = "price")
    private int price;

    @Column(name = "genres")
    private String genres; // אחסון רשימה של ג'אנרים כ-String אחד

    // Constructors, Getters, Setters, ToString (לפי הצורך)
    public BookPostgresSQL() {}

    public BookPostgresSQL(String title, String author, int year, int price, String genres) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.price = price;
        this.genres = genres;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }
}
