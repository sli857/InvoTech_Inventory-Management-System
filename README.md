## Testing Documentation

# Controllers

`@InjectMocks` is ensuring that a mock object of ShipsRepository is injected into the ShipsController when the tests are
run. This allows the controller to interact with a mocked version of the repository instead of a real database or
external service, enabling isolated unit testing of the controller logic.

The `setup()` method in the test class is annotated with `@BeforeEach`, which means it will run before each test method
in the class. In this method, the following actions are performed:

```java

@BeforeEach
public void setup() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(shipsController).build();
}
```

1. `MockitoAnnotations.openMocks(this);`: This line initializes annotated fields in the test class for mocking with
   Mockito. Specifically, it initializes the `@Mock` and `@InjectMocks` annotations, ensuring that the mock objects are
   properly set up before each test method is executed.

2. `mockMvc = MockMvcBuilders.standaloneSetup(shipsController).build();`: This line sets up a standalone MockMvc
   instance for testing the `ShipsController` without the need for a complete Spring application context. It initializes
   the MockMvc instance with the `ShipsController`, enabling you to perform HTTP requests and verify responses in the
   controller's context during the test.


This test method, named `testGetAllShips()`, is designed to test the behavior of the `getAllShips()` endpoint in
the `ShipsController` class.

1. **Data Setup**: It creates two `Ship` objects (`ship1` and `ship2`) and adds them to a list called `ships`.

2. **Mocking Repository Behavior**: It sets up behavior for the `shipsRepository` mock object. Specifically, it tells
   Mockito that when `shipsRepository.findAll()` is called, it should return the list of ships created in the previous
   step.

3. **Performing Mock HTTP Request**: It uses the `mockMvc` instance to perform a mock HTTP GET request to the `/ships`
   endpoint.

4. **Expectations**: It sets up expectations on the response of this mock request:
    - Expects the HTTP response status to be `200 OK` (`status().isOk()`).
    - Expects the content of the response to be in JSON format (`contentType(MediaType.APPLICATION_JSON)`).
    - Expects the content of the response to match the provided JSON representation of the list of
      ships (`content().json("[{},{}]")`).
