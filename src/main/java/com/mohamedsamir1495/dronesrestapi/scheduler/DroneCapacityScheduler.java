package com.mohamedsamir1495.dronesrestapi.scheduler;

import com.mohamedsamir1495.dronesrestapi.repository.DroneRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Component
@EnableScheduling
@AllArgsConstructor
public class DroneCapacityScheduler {

	private final DroneRepository droneRepository;

	@Scheduled(fixedRateString = "${scheduler.audit-interval}")
	public void logCapacity() {
		log.info("Drone capacity check at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));
		Optional.ofNullable(droneRepository.findAll()
										   .collectList()
										   .block())
				.orElse(new ArrayList<>())
				.forEach(drone -> log.info(String.valueOf(drone)));
	}
}
