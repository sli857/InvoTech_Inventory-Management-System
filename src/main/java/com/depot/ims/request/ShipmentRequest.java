package com.depot.ims.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentRequest {

    // would be much easier if the frontend saved item objects and only sent ids

    private Integer source;

    private Integer destination;

    private Map<Integer, Integer> itemsWithQuantities;

//    POST JSON format:
//    {
//        "source":123,
//        "destination":456,
//        "itemsWithQuantities":{
//            "item1_id":quantity1,
//            "item2_id":quantity2,
//            ...
//        }
//    }


}
