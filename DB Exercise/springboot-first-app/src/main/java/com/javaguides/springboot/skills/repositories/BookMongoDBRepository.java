package com.javaguides.springboot.skills.repositories;
import com.javaguides.springboot.skills.entities.BookMongoDB;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BookMongoDBRepository extends MongoRepository<BookMongoDB, Integer> {


    // tutal
    List<BookMongoDB> findByAuthorLike(String author);

    /*
    List<BookMongoDB> findByPriceBetween(Integer priceMin, Integer priceMax);
    List<BookMongoDB> findByYearBetween(Integer yearMin, Integer yearMax);
    List<BookMongoDB> findByGenresLike(String genres);
     */

    List<BookMongoDB> findByPriceGreaterThan(Integer price);

    List<BookMongoDB> findByPriceLessThan(Integer price);

    List<BookMongoDB> findByYearGreaterThan(Integer year);

    List<BookMongoDB> findByYearLessThan(Integer year);
    //booksData
    List<BookMongoDB> findByAuthorLikeIgnoreCase(String author);

    List<BookMongoDB> findByPriceGreaterThanAndPriceLessThan(Integer priceBiggerThan, Integer priceLessThan);

    List<BookMongoDB> findByYearGreaterThanAndYearLessThan(Integer yearBiggerThan, Integer yearLessThan);

    List<BookMongoDB> findByGenresLikeIgnoreCase(String genres);

    Optional<BookMongoDB> findByRawid(Integer rawid);

    boolean existsByTitleIgnoreCase(String title);

    List<BookMongoDB> findByGenresIn(List<String> genres);
}