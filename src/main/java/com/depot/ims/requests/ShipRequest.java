package com.depot.ims.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ShipRequest is a request object for the Ship entity used by the frontend. It abstracts the Ship
 * object to allow for business logic like querying to be done in the service layer rather than in
 * the frontend.
 *
 * <p>Rather than frontend passing in the Item and Shipment objects, it passes in their IDs and
 * querying is done in the service layer to get the actual objects.
 *
 * @see com.depot.ims.models.Ship
 * @see com.depot.ims.services.ShipService
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipRequest {
  private Long itemId;
  private Long shipmentId;
  private Integer quantity;
}
