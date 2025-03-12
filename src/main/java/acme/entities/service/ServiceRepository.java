
package acme.entities.service;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface ServiceRepository extends AbstractRepository {

	@Query("select s from Service s where s.promotionCode = :PromotionCode")
	Service findServiceByPromotionCode(String PromotionCode);
}
