package com.mohamedsamir1495.dronesrestapi.repository;

import com.mohamedsamir1495.dronesrestapi.domain.drone.entity.Drone;
import com.mohamedsamir1495.dronesrestapi.domain.drone.enums.DroneState;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DroneRepository extends ReactiveCrudRepository<Drone, Long> {
	Flux<Drone> findByState(DroneState state);
	Mono<Drone> findBySerialNumber(String serialNumber);

}
