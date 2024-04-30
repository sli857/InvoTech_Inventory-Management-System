package com.depot.ims.repositories;

import com.depot.ims.models.Ship;
import com.depot.ims.models.keys.ShipKey;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/** Ship Repository interface extends JpaRepository for performing CRUD on table Ships. */
@Repository
public interface ShipRepository extends JpaRepository<Ship, ShipKey> {

  /**
   * Find a list of ships that contain a specific item.
   *
   * @param itemId itemId
   * @return a list of ships
   */
  @Query("select s from Ship s where s.itemId.itemId = ?1")
  List<Ship> findByItemId(Long itemId);

  /**
   * Find a list of ships that associate with a specific shipment identified by shipmentId.
   *
   * @param shipmentId shipmentId
   * @return a list of ships
   */
  @Query("select s from Ship s where s.shipmentId.shipmentId = ?1")
  List<Ship> findByShipmentId(Long shipmentId);

  /**
   * Find a list of ships that contains a specific item and associate with a specific shipment
   * identified by shipmentId.
   *
   * @param itemId itemId
   * @param shipmentId shipmentId
   * @return a list of ships. Ideally, only one ship entity will be returned
   */
  @Query("select s from Ship s where s.itemId.itemId = ?1 and s.shipmentId.shipmentId = ?2")
  List<Ship> findByItemIdAndShipmentId(Long itemId, Long shipmentId);
}
