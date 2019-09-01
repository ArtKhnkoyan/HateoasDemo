package com.example.demo.rest;

import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.rest.resources.AuthorResource;
import com.example.demo.rest.resources.BookResource;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/authors", produces = "application/hal+json")
@AllArgsConstructor
public class AuthorEndPoint {

    private AuthorRepository authorRepository;
    private BookRepository bookRepository;

    @PostMapping
    public ResponseEntity<AuthorResource> save(@RequestBody Author author) {
        authorRepository.save(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthorResource(author));
    }

    @GetMapping
    public ResponseEntity<Resources<AuthorResource>> findAll() {
        List<AuthorResource> collect = authorRepository.findAll().stream().map(AuthorResource::new).collect(Collectors.toList());
        Resources<AuthorResource> resources = new Resources<>(collect);
        String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        resources.add(new Link(uriString, "self"));
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResource> findById(@PathVariable long id) {
        Optional<Author> findById = authorRepository.findById(id);
        return findById.map(author -> ResponseEntity.ok(new AuthorResource(author))).
                orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{authorId}/books")
    public ResponseEntity<Resources<BookResource>> findBooksByAuthorId(@PathVariable long authorId) {
        List<BookResource> booksByAuthorId = findAllBookByAuthor(authorId);
        Resources<BookResource> bookResources = new Resources<>(booksByAuthorId);
        String toUriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        bookResources.add(new Link(toUriString, "books"));
        return ResponseEntity.ok(bookResources);
    }

    private List<BookResource> findAllBookByAuthor(long authorId) {
        return bookRepository.findAllByAuthorId(authorId)
                .stream()
                .map(BookResource::new)
                .collect(Collectors.toList());
    }

    @PutMapping
    public ResponseEntity<AuthorResource> update(@RequestBody Author author) {
        Optional<Author> repoAuthor = authorRepository.findById(author.getId());
        return repoAuthor.map(a -> {
            a.setName(author.getName());
            a.setSurname(author.getSurname());
            a.setAge(author.getAge());
            authorRepository.save(a);
            return ResponseEntity.ok().body(new AuthorResource(a));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestBody Author author) {
        Optional<Author> repoAuthor = authorRepository.findById(author.getId());
        if (!repoAuthor.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        authorRepository.delete(author);
        return ResponseEntity.ok().body("author is delete");
    }
}
