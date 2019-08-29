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
    public ResponseEntity<Void> save(@RequestBody Author author) {
        authorRepository.save(author);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Author>> findAll() {
        return ResponseEntity.ok(authorRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResource> findById(@PathVariable long id) {
        Optional<Author> findById = authorRepository.findById(id);
        if (!findById.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(findById.map(AuthorResource::new).get());
    }

    @GetMapping(value = "/{authorId}/books")
    public ResponseEntity<Resources<BookResource>> findBooksByAuthorId(@PathVariable long authorId) {
        List<BookResource> booksByAuthorId = findAllBookByAuthor(authorId);
        Resources<BookResource> bookResources = new Resources<>(booksByAuthorId);
        String toUriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        bookResources.add(new Link(toUriString, "self"));
        return ResponseEntity.ok(bookResources);
    }

    private List<BookResource> findAllBookByAuthor(long authorId) {
        return bookRepository.findAllByAuthorId(authorId)
                .stream()
                .map(BookResource::new)
                .collect(Collectors.toList());
    }

    @PutMapping
    public ResponseEntity update(@RequestBody Author author) {
        Optional<Author> repoAuthor = authorRepository.findById(author.getId());
        if (!repoAuthor.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<Author> mapAuthor = repoAuthor.map(a -> {
            a.setName(author.getName());
            a.setSurname(author.getSurname());
            a.setAge(author.getAge());
            return a;
        });
        authorRepository.save(mapAuthor.get());
        return ResponseEntity.ok().body("author is update");
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
