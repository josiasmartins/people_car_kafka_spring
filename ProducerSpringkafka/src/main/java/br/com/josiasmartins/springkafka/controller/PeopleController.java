package br.com.josiasmartins.springkafka.controller;

import br.com.josiasmartins.springkafka.domain.DTO.PeopleDTO;
import br.com.josiasmartins.springkafka.producer.PeopleProducer;
import br.com.springkafka.People;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/peoples")
public class PeopleController {

    private final PeopleProducer peopleProducer;

    public PeopleController(PeopleProducer peopleProducer) {
        this.peopleProducer = peopleProducer;
    }

    @PostMapping
    public ResponseEntity<Void> sendMessage(@RequestBody PeopleDTO peopleDTO) {
        String id = UUID.randomUUID().toString();
        People message = People.newBuilder()
                .setId(id)
                .setName(peopleDTO.getName())
                .setCpf(peopleDTO.getCpf())
                .setBooks(peopleDTO.getBooks().stream().map(p -> (CharSequence) p).collect(Collectors.toList()))
                .build();

        peopleProducer.sendMessage(message);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

}
