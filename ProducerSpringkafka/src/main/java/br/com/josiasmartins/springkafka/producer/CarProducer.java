package br.com.josiasmartins.springkafka.producer;

import br.com.springkafka.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CarProducer {

    private static final Logger log = LoggerFactory.getLogger(CarProducer.class);

    private final String topicName;
    private final KafkaTemplate<String, Car> kafkaTemplate;

    public CarProducer(@Value("${topic.name}") String topicName, KafkaTemplate<String, Car> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Car car) {
        kafkaTemplate.send(topicName, car.getId().toString(), car).whenComplete((success, error) -> {
            if (!error.getMessage().isBlank()) {
                log.info("Falha ao enviar message car: " + error.getMessage());
                return;
            }

            log.info("Mensagem Enviada com sucesso: " + success);

        });
    }


}
