package com.example.demo.rest.resources;

import com.example.demo.model.Author;
import com.example.demo.rest.AuthorEndPoint;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

@Getter
@Relation(value = "author", collectionRelation = "authors")
public class AuthorResource extends ResourceSupport {
    private Author author;

    public AuthorResource(Author author) {
        this.author = author;
        long id = author.getId();
        add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AuthorEndPoint.class).save(author)).withSelfRel().withType("POST"));
        add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AuthorEndPoint.class).findAll()).withSelfRel().withType("GET"));
        add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AuthorEndPoint.class).findById(id)).withSelfRel().withType("GET"));
        add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AuthorEndPoint.class).findBooksByAuthorId(id)).withRel("books").withType("GET"));
        add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AuthorEndPoint.class).update(author)).withSelfRel().withType("PUT"));
        add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AuthorEndPoint.class).delete(author)).withSelfRel().withType("DELETE"));
    }
}
