package com.example.demo.rest.resources;

import com.example.demo.model.Author;
import com.example.demo.rest.AuthorEndPoint;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

@Getter
public class AuthorResource extends ResourceSupport {
    private final Author author;

    public AuthorResource(Author author) {
        this.author = author;
        long id = author.getId();
        add(ControllerLinkBuilder.linkTo(AuthorEndPoint.class).withRel("authors"));
        add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AuthorEndPoint.class).findBooksByAuthorId(id)).withRel("books"));
        add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AuthorEndPoint.class).findById(id)).withSelfRel());
    }
}
