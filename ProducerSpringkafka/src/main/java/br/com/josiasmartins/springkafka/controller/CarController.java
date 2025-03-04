package br.com.josiasmartins.springkafka.controller;

import br.com.josiasmartins.springkafka.domain.DTO.CarDTO;
import br.com.josiasmartins.springkafka.producer.CarProducer;
import br.com.springkafka.Car;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarProducer carProducer;

    public CarController(CarProducer carProducer) {
        this.carProducer = carProducer;
    }

    @PostMapping
    public ResponseEntity<Void> sendMessage(@RequestBody CarDTO carDTO) {

        String id = UUID.randomUUID().toString();
        Car message = Car.newBuilder()
                .setId(id)
                .setName(carDTO.getName())
                .setBrand(carDTO.getBrand())
                .build();

        carProducer.sendMessage(message);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }


}
