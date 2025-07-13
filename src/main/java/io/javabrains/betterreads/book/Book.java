package io.javabrains.betterreads.book;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Table(value = "book_by_id")
public class Book {

    @Id 
    @PrimaryKeyColumn(name = "book_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id;

    @Column("book_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String name;

    @Column("book_description")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String description;

    @Column("published_date")
    @CassandraType(type = CassandraType.Name.DATE)
    private LocalDate publishedDate;

    @Column("cover_ids")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> coversIds;

    @Column("author_names")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> authorNames;

    @Column("author_id")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> authorIds;

    // Default constructor for Spring Data
    public Book() {}

    // Constructor for easier object creation
    public Book(String id, String name, String description, LocalDate publishedDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.publishedDate = publishedDate;
    }

    // Getters and Setters with null-safe collection handling
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public List<String> getCoversIds() {
        return coversIds != null ? coversIds : Collections.emptyList();
    }

    public void setCoversIds(List<String> coversIds) {
        this.coversIds = coversIds;
    }

    public List<String> getAuthorNames() {
        return authorNames != null ? authorNames : Collections.emptyList();
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

    public List<String> getAuthorIds() {
        return authorIds != null ? authorIds : Collections.emptyList();
    }

    public void setAuthorIds(List<String> authorIds) {
        this.authorIds = authorIds;
    }

    // Utility method to check if book has cover images
    public boolean hasCoverImages() {
        return coversIds != null && !coversIds.isEmpty();
    }

    // Utility method to get the first cover image ID
    public String getFirstCoverId() {
        return hasCoverImages() ? coversIds.get(0) : null;
    }
}
