package com.example.storefrontdemo.services.Repository;

import com.example.storefrontdemo.domain.entities.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Spring docs available at https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.details
 */
@Repository
public interface CartDetailRepoServiceRepository extends JpaRepository<CartDetail, Integer> {

   List<CartDetail> findCartDetailsByCartId(Integer cartId);

   CartDetail findCartDetailByCartIdAndId(Integer cartId, Integer cartDetailId);

   CartDetail findCartDetailByCartIdAndProductId(Integer cartId, Integer productId);

}
