
package acme.features.manager.leg;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.claim.Claim;
import acme.entities.flight.Flight;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.entities.trackingLogs.TrackingLog;

@Repository
public interface ManagerLegRepository extends AbstractRepository {

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select l from Leg l where l.flight.id = :masterId order by l.scheduledDeparture")
	List<Leg> findAllLegsByMasterId(int masterId);

	@Query("select f from Flight f where f.id = :masterId")
	Flight findFlightById(int masterId);

	@Query("select ac from Aircraft ac where ac.airline.id=:airlineId")
	Collection<Aircraft> findAllAircraftsByAirlineId(int airlineId);

	@Query("select ot.location from OperatesAt ot where ot.operator.id=:operatorId")
	Collection<Airport> findAllAirportsByOperatorId(int operatorId);

	@Query("select ap from Airport ap")
	Collection<Airport> findAllAirports();

	@Query("select ap from Airport ap where ap.id=:airportId")
	Airport findAirportById(int airportId);

	@Query("select ac from Aircraft ac")
	Collection<Aircraft> findAllAircrafts();

	@Query("select ac from Aircraft ac where ac.id=:aircraftId")
	Aircraft findAircraftById(int aircraftId);

	@Query("select l from Leg l where l.aircraft.id = :aircraftId")
	Collection<Leg> findAllLegsByAircraftId(int aircraftId);

	@Query("select fa from FlightAssignment fa where fa.leg.id = :legId")
	Collection<FlightAssignment> findAllFlightAssignmentsByLegId(int legId);

	@Query("select c from Claim c where c.leg.id = :legId")
	Collection<Claim> findAllClaimsByLegId(int legId);

	@Query("select tl from TrackingLog tl where tl.claim.id = :claimId")
	Collection<TrackingLog> findAllTrackingLogsByClaimId(int claimId);

}
