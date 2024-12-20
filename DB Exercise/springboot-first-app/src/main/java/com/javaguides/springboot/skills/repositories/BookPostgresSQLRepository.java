package com.javaguides.springboot.skills.repositories;
import com.javaguides.springboot.skills.entities.BookPostgresSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookPostgresSQLRepository extends JpaRepository<BookPostgresSQL, Integer> {

    boolean existsByTitleIgnoreCase(String title);

    @Query("SELECT COUNT(b) FROM BookPostgresSQL b WHERE " +
            "(COALESCE(:author, '') = '' OR b.author LIKE %:author%) AND " +
            "(COALESCE(:priceBiggerThan, 0) = 0 OR b.price > :priceBiggerThan) AND " +
            "(COALESCE(:priceLessThan, 0) = 0 OR b.price < :priceLessThan) AND " +
            "(COALESCE(:yearBiggerThan, 0) = 0 OR b.year > :yearBiggerThan) AND " +
            "(COALESCE(:yearLessThan, 0) = 0 OR b.year < :yearLessThan) AND " +
            "(COALESCE(:genres, '') = '' OR b.genres LIKE %:genres%)")
    int countBooksWithFilters(@Param("author") String author,
                              @Param("priceBiggerThan") Integer priceBiggerThan,
                              @Param("priceLessThan") Integer priceLessThan,
                              @Param("yearBiggerThan") Integer yearBiggerThan,
                              @Param("yearLessThan") Integer yearLessThan,
                              @Param("genres") String genres);


/*
    @Query("SELECT b FROM BookPostgresSQL b WHERE " +
            "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
            "(:priceStart IS NULL OR :priceEnd IS NULL OR b.price BETWEEN :priceStart AND :priceEnd) AND " +
            "(:yearStart IS NULL OR :yearEnd IS NULL OR b.year BETWEEN :yearStart AND :yearEnd) AND " +
            "(:genres IS NULL OR LOWER(b.genres) LIKE LOWER(CONCAT('%', :genres, '%')))")
    List<BookPostgresSQL> findBooksByFilters(@Param("author") String author,
                                             @Param("priceStart") Integer priceStart,
                                             @Param("priceEnd") Integer priceEnd,
                                             @Param("yearStart") Integer yearStart,
                                             @Param("yearEnd") Integer yearEnd,
                                             @Param("genres") String genres);

*/
@Query("SELECT b FROM BookPostgresSQL b WHERE " +
        "(:author IS NULL OR :author = '' OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
        "(:priceBiggerThan IS NULL OR b.price >= :priceBiggerThan) AND " +
        "(:priceLessThan IS NULL OR b.price <= :priceLessThan) AND " +
        "(:yearBiggerThan IS NULL OR b.year >= :yearBiggerThan) AND " +
        "(:yearLessThan IS NULL OR b.year <= :yearLessThan) AND " +
        "(:genres IS NULL OR :genres = '' OR " +
        "LOWER(b.genres) LIKE LOWER(CONCAT('%', :genres, '%'))) " +
        "ORDER BY LOWER(b.title) ASC")
List<BookPostgresSQL> findBooksByFilters(@Param("author") String author,
                                         @Param("priceBiggerThan") Integer priceBiggerThan,
                                         @Param("priceLessThan") Integer priceLessThan,
                                         @Param("yearBiggerThan") Integer yearBiggerThan,
                                         @Param("yearLessThan") Integer yearLessThan,
                                         @Param("genres") String genres);

}
