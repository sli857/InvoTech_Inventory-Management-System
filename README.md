## Testing Documentation

# Repositories


# Controllers

### Annotations

`@InjectMocks`: Ensures that a mock object of ShipsRepository is injected into the ShipsController when the tests are
run. This allows the controller to interact with a mocked version of the repository instead of a real database or
external service, enabling isolated unit testing of the controller logic.

`@Mock` is used to create a mock object to be injected into the `ShipsController`.

### Setup

```java
@BeforeEach
public void setup() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(shipsController).build();
}
```

`MockitoAnnotations.openMocks(this);`: This line initializes annotated fields in the test class for mocking with
   Mockito. Specifically, it initializes the `@Mock` and `@InjectMocks` annotations, ensuring that the mock objects are
   properly set up before each test method is executed.

`mockMvc = MockMvcBuilders.standaloneSetup(shipsController).build();`: This line sets up a standalone MockMvc
   instance for testing the `ShipsController` without the need for a complete Spring application context. It initializes
   the MockMvc instance with the `ShipsController`, enabling you to perform HTTP requests and verify responses in the
   controller's context during the test.

### Test Methods

`testGetAllShips()`: Tests the endpoint for retrieving all ships.
It sets up mock behavior for the shipRepository to return a list of ships when `findAll()` is called.
Then, it performs an HTTP GET request to `/ships` 
and asserts that the response status is OK and the returned JSON array has a length of 2.

`testGetShipsByItemId()`: Tests the endpoint
for retrieving ships by item ID. It sets up mock behavior for `shipService`
and `shipRepository` and then performs an HTTP GET request to `/ships/item={itemId}`.

`testGetShipsByShipmentId()`: Similar to the previous test but for retrieving ships by shipment ID.

`testGetShipsByItemIdAndShipmentId()`: Tests the endpoint for retrieving ships by both item ID and shipment ID.

### Assertions

Using `MockMvc` and its `perform()` method, 
it simulates HTTP requests to the endpoints provided by the `ShipController` 
and asserts the response status and the structure/content of the returned JSON.

# Services


