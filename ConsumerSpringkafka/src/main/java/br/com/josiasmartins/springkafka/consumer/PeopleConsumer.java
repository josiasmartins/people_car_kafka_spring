package br.com.josiasmartins.springkafka.consumer;

import br.com.josiasmartins.springkafka.domain.entity.Book;
import br.com.josiasmartins.springkafka.repository.PeopleRepository;
import br.com.springkafka.Car;
import br.com.springkafka.People;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@KafkaListener(topics = "${topic.name}")
public class PeopleConsumer {

    private final PeopleRepository peopleRepository;

    private static final Logger log = LoggerFactory.getLogger(PeopleConsumer.class);

    public PeopleConsumer(PeopleRepository repository) {
        this.peopleRepository = repository;
    }

    @KafkaHandler
    public void consumer(ConsumerRecord<String, People> record, Acknowledgment ack) {
        People people = record.value();

        log.info("Mensagem consumidaa: " + people.toString());

        br.com.josiasmartins.springkafka.domain.entity.People peopleEntity = br.com.josiasmartins.springkafka.domain.entity.People.builder().build();

        peopleEntity.setId(people.getId().toString());
        peopleEntity.setCpf(people.getCpf().toString());
        peopleEntity.setName(people.getName().toString());
        peopleEntity.setBooks(people.getBooks().stream()
                .map(book -> Book.builder()
                        .people(peopleEntity)
                        .name(book.toString())
                        .build()).collect(Collectors.toList()));

        peopleRepository.save(peopleEntity);

        ack.acknowledge();
    }

    @KafkaHandler
    public void consumerCar(Car car, Acknowledgment ack) {
        log.info("Message Received: " + car);
        ack.acknowledge();
    }

    @KafkaHandler(isDefault = true) // Método para ouvir os objetos desconhecidos
    public void unknown(Object object, Acknowledgment ack) {
        log.info("Message Received: " + object);
        ack.acknowledge();
    }

}
