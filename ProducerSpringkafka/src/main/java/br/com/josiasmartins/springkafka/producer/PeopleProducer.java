package br.com.josiasmartins.springkafka.producer;

import br.com.springkafka.People;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class PeopleProducer {

    private static final Logger log = LoggerFactory.getLogger(PeopleProducer.class);

    private final String topicName;
    private final KafkaTemplate<String, People> kafkaTemplate;

    public PeopleProducer(@Value("${topic.name}") String topicName, KafkaTemplate<String, People> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(People people) {
        // forma antiga de adicionar logs (spring 2.x.x)
//        kafkaTemplate.send(topicName, people).addCallback(
//            success -> log.info("Mensagem Enviada com sucesso"),
//            fail -> log.error("Falha ao enviar mensagem")
//        );

        // forma nova de adicionar logs (spring 3.x.x)
        kafkaTemplate.send(topicName, people)
                .thenRun(() -> log.info("Mensagem enviada com sucesso para o tópico: {}", topicName))
                .exceptionally(ex -> {
                    log.error("Falha ao enviar mensagem para o tópico: {}, message error: {}", topicName, ex);
                    return null;
                });


    }



}
