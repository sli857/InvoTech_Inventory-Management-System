package com.depot.ims.services;

import com.depot.ims.models.Availability;
import com.depot.ims.models.Ship;
import com.depot.ims.models.Shipment;
import com.depot.ims.models.Site;
import com.depot.ims.repositories.AvailabilityRepository;
import com.depot.ims.repositories.ItemRepository;
import com.depot.ims.repositories.ShipRepository;
import com.depot.ims.repositories.ShipmentRepository;
import com.depot.ims.repositories.SiteRepository;
import com.depot.ims.requests.ShipRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/** Service class for managing Ship operations. */
@Service
public class ShipService {
  private final ShipRepository shipRepository;
  private final ShipmentRepository shipmentRepository;
  private final ItemRepository itemRepository;
  private final AvailabilityRepository availabilityRepository;
  private final SiteRepository siteRepository;
  private final AuditService auditService;

  /**
   * Constructor for ShipService.
   *
   * @param shipRepository The ShipRepository instance.
   * @param shipmentRepository The ShipmentRepository instance.
   * @param itemRepository The ItemRepository instance.
   * @param availabilityRepository The AvailabilityRepository instance.
   */
  public ShipService(
      ShipRepository shipRepository,
      ShipmentRepository shipmentRepository,
      ItemRepository itemRepository,
      AvailabilityRepository availabilityRepository,
      AuditService auditService,
      SiteRepository siteRepository) {
    this.shipRepository = shipRepository;
    this.shipmentRepository = shipmentRepository;
    this.itemRepository = itemRepository;
    this.availabilityRepository = availabilityRepository;
    this.siteRepository = siteRepository;
    this.auditService = auditService;
  }

  /**
   * Adds a new Ship object to the database and updates the quantity of the item in the source and
   * destination sites.
   *
   * @param shipRequest The ShipRequest object representing the Ship object to be added.
   * @return ResponseEntity containing the result of the ship addition operation.
   */
  public ResponseEntity<?> addShip(ShipRequest shipRequest) {
    // Validate
    if (shipRequest == null
        || shipRequest.getShipmentId() == null
        || shipRequest.getItemId() == null
        || shipRequest.getQuantity() == null) {
      return ResponseEntity.badRequest().body("Invalid shipRequest");
    }
    // Check if the shipment, source, destination, and item exist in the database
    Shipment shipment = shipmentRepository.findByShipmentId(shipRequest.getShipmentId());
    com.depot.ims.models.Item item = itemRepository.findByItemId(shipRequest.getItemId());

    if (shipment == null || item == null) {
      return ResponseEntity.badRequest()
          .body(
              String.format(
                  "Shipment or item do not exist%nShipment: %s%nItem: %s", shipment, item));
    }

    // Create a new Ship object
    Ship ship =
        Ship.builder()
            .shipmentId(shipment)
            .itemId(item)
            .quantity(shipRequest.getQuantity())
            .build();

    // Check if the quantity of the item in the source site is enough
    if (availabilityRepository
            .findBySiteIdAndItemId(ship.getShipmentId().getSource(), ship.getItemId().getItemId())
            .getQuantity()
        < ship.getQuantity()) {
      return ResponseEntity.badRequest()
          .body(
              String.format(
                  "Not enough quantity for %d in the source site", shipRequest.getQuantity()));
    }

    // Update the quantity of the item in the source site
    availabilityRepository
        .findBySiteIdAndItemId(ship.getShipmentId().getSource(), ship.getItemId().getItemId())
        .setQuantity(
            availabilityRepository
                    .findBySiteIdAndItemId(
                        ship.getShipmentId().getSource(), ship.getItemId().getItemId())
                    .getQuantity()
                - ship.getQuantity());

    // Update the quantity of the item in the destination site
    // If the item is not available in the destination site, add it
    if (availabilityRepository.findBySiteIdAndItemId(
            ship.getShipmentId().getDestination(), ship.getItemId().getItemId())
        == null) {
      Site destination = siteRepository.findBySiteId(ship.getShipmentId().getDestination());
      availabilityRepository.save(
          new Availability(destination, ship.getItemId(), ship.getQuantity()));
    } else {
      availabilityRepository
          .findBySiteIdAndItemId(
              ship.getShipmentId().getDestination(), ship.getItemId().getItemId())
          .setQuantity(
              availabilityRepository
                      .findBySiteIdAndItemId(
                          ship.getShipmentId().getDestination(), ship.getItemId().getItemId())
                      .getQuantity()
                  + ship.getQuantity());
    }

    // Save the changes
    var res = shipRepository.save(ship);

    // construct rowKey as: "shipmentId: ***, itemId: ***"
    String rowKey =
        "shipmentId: "
            + res.getShipmentId().getShipmentId()
            + ", itemId: "
            + res.getItemId().getItemId();
    // record audit
    auditService.saveAudit("Ships", null, rowKey, null, res.toString(), "INSERT");

    return ResponseEntity.ok().body(res);
  }
}
