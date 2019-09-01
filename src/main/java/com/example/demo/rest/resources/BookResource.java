package com.example.demo.rest.resources;

import com.example.demo.model.Book;
import com.example.demo.rest.BookEndPoint;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

@Getter
public class BookResource extends ResourceSupport {
    private Book book;

    public BookResource(Book book) {
        this.book = book;
        long id = book.getId();
        add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(BookEndPoint.class).findById(id)).withSelfRel());
    }
}
