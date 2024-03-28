package com.mohamedsamir1495.dronesrestapi.repository;

import com.mohamedsamir1495.dronesrestapi.domain.medication.entity.Medication;
import com.mohamedsamir1495.dronesrestapi.domain.medication.enums.MedicationState;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MedicationRepository extends ReactiveCrudRepository<Medication, Long> {

	Flux<Medication> findByDroneIdAndState(Long droneId, MedicationState state);
	@Modifying
	@Query(
			"update Medication " +
					"set state =  :state " +
					"where drone_id = :droneId "
	)
	Mono<Boolean> updateMedicationState(@Param("state") MedicationState state, @Param("droneId") Long droneId);
}
