## Testing Documentation

# General Standards

Tests follow Behavior-Driven Development (BDD) principles where each test method follows a similar structure:

- Given: Set up the test scenario by defining inputs and expected behavior.
- When: Execute the method being tested with the defined inputs.
- Then: Assert the expected behavior based on the method's output.

# Repositories

### Annotations

- `@DataJpaTest`: This annotation is used to test JPA applications. It focuses only on JPA components.
- `@TestPropertySource`: This annotation is used to specify property sources for the test.
  In this case, it sets specific properties related to the database and Hibernate.
- `@Autowired`: This annotation is used to inject dependencies automatically.
  In this case, it is used to inject the repositories into the test class.

### Test Methods

- `findAllTest()`: Tests the `findAll()` method of `ShipRepository`.
  It creates some ships, invokes the method, and checks if the returned list is not null and contains three items.
- `findByItemIdTest()`: Tests the `findByItemId()` method of `ShipRepository`.
  It creates ships, invokes the method with a specific item ID,
  and checks if the returned list is not null and contains two items.
- `findByShipmentIdTest()`: Tests the `findByShipmentId()` method of `ShipRepository`.
  It creates ships, invokes the method with a specific shipment ID,
  and checks if the returned list is not null and contains one item.
- `findByItemIdAndShipmentIdTest()`: Tests the `findByItemIdAndShipmentId()` method of `ShipRepository`.
  It creates ships, invokes the method with specific item and shipment IDs,
  and checks if the returned lists are not null and contain one item each.

### Helper Methods

- `createShip()`: Creates a Ship entity and saves it to the database using `shipRepository`.
- `createItem()`: Creates an Item entity and saves it to the database using `itemRepository`.
- `createShipment()`: Creates a Shipment entity and saves it to the database using `shipmentRepository`.

These tests ensure that the CRUD operations on the Ship entity work correctly and that the custom query methods
in `ShipRepository` return the expected results.

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

- `MockitoAnnotations.openMocks(this);`: This line initializes annotated fields in the test class for mocking with
  Mockito.
  Specifically, it initializes the `@Mock` and `@InjectMocks` annotations, ensuring that the mock objects are
  properly set up before each test method is executed.
- `mockMvc = MockMvcBuilders.standaloneSetup(shipsController).build();`: This line sets up a standalone MockMvc
  instance for testing the `ShipsController` without the need for a complete Spring application context.
  It initializes the MockMvc instance with the `ShipsController`,
  enabling you to perform HTTP requests and verify responses in the controller's context during the test.

### Test Methods

- `testGetAllShips()`: Tests the endpoint for retrieving all ships.
  It sets up mock behavior for the shipRepository to return a list of ships when `findAll()` is called.
  Then, it performs an HTTP GET request to `/ships`
  and asserts that the response status is OK and the returned JSON array has a length of 2.
- `testGetShipsByItemId()`: Tests the endpoint for retrieving ships by item ID. It sets up mock behavior
  for `shipService` and `shipRepository` and then performs an HTTP GET request to `/ships/item={itemId}`.
- `testGetShipsByShipmentId()`: Similar to the previous test but for retrieving ships by shipment ID.
- `testGetShipsByItemIdAndShipmentId()`: Tests the endpoint for retrieving ships by both item ID and shipment ID.

### Assertions

Using `MockMvc` and its `perform()` method,
it simulates HTTP requests to the endpoints provided by the `ShipController`
and asserts the response status and the structure/content of the returned JSON.

# Services

### Annotations

```java

@Mock
ShipRepository shipRepository = mock(ShipRepository.class);
```

Here mocks are explicitly created using the `mock()` and explicitly assigns them to class fields.

- Using `@Mock` annotation directly is more concise
  and relies on the testing framework to handle mock initialization and cleanup.
- Using `mock()` method explicitly gives more control over mock initialization and allows for additional setup,
  but requires manual management of mock lifecycles.

The behavior of the mocked repositories is set up using the `given(...).willReturn(...)` syntax from Mockito.
This allows controlling the behavior of the repositories when their methods are called during the tests.

### Test Methods

- `getShipmentById_ValidId_ReturnsShipment`: Tests `getShipmentById` with a valid ID, verifying it returns the
  corresponding shipment.
- `getShipmentById_InvalidId_ReturnsNull`: Tests `getShipmentById` with an invalid ID, ensuring it returns null.
- `getItemById_ValidId_ReturnsItem`: Tests `getItemById` with a valid ID, confirming it returns the associated item.
- `getItemById_InvalidId_ReturnsNull`: Tests `getItemById` with an invalid ID, affirming it returns null.
