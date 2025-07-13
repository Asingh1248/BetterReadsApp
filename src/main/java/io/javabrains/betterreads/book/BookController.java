package io.javabrains.betterreads.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class BookController {

    private static final String COVER_IMAGE_ROOT = "https://covers.openlibrary.org/b/id/";
    private static final String NO_IMAGE_PATH = "/images/No-image.png";
    private static final String COVER_IMAGE_SUFFIX = "-L.jpg";

    @Autowired
    private BookRepository bookRepository;

    @GetMapping(value = "/books/{bookId}")
    public String getBook(@PathVariable String bookId, Model model) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        
        if (optionalBook.isEmpty()) {
            return "book-not-found";
        }

        Book book = optionalBook.get();
        String coverImageUrl = getCoverImageUrl(book);
        
        model.addAttribute("coverImage", coverImageUrl);
        model.addAttribute("book", book);
        
        return "book"; // book.html template
    }

    private String getCoverImageUrl(Book book) {
        // Use the optimized utility method from Book entity
        if (!book.hasCoverImages()) {
            return NO_IMAGE_PATH;
        }
        
        try {
            String firstCoverId = book.getFirstCoverId();
            if (firstCoverId != null && !firstCoverId.trim().isEmpty()) {
                return COVER_IMAGE_ROOT + firstCoverId + COVER_IMAGE_SUFFIX;
            }
        } catch (Exception e) {
            // Log the error instead of printing stack trace
            System.err.println("Error processing cover image for book " + book.getId() + ": " + e.getMessage());
        }
        
        return NO_IMAGE_PATH;
    }
}
