package io.javabrains.betterreads.book;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class BookController {

    private final String COVER_IMAGE_ROOT="https://covers.openlibrary.org/b/id/";

    @Autowired
    BookRepository bookRepository;

    @GetMapping(value="/books/{bookId}")
    public String getBook(@PathVariable String bookId, Model model){ //Imp
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(optionalBook.isPresent()){
            Book book = optionalBook.get();
            String coverImageUrl="/images/No-image.png";
            try {
                if(book.getCoversIds()!=null & book.getCoversIds().size()>0 ){
                      coverImageUrl=COVER_IMAGE_ROOT + book.getCoversIds().get(0) + "-L.jpg";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            model.addAttribute("coverImage",coverImageUrl);
            model.addAttribute("book",book);
            //How to return it
            return "book"; //book.html ==> template
        }
        return "book-not-found";
    }
}
