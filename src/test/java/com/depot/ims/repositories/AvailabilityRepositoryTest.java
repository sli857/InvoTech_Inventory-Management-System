package com.depot.ims.repositories;


import com.depot.ims.controllers.ShipController;
import com.depot.ims.controllers.ItemController;
import com.depot.ims.controllers.AvailabilityController;
import com.depot.ims.repositories.ShipRepository;
import com.depot.ims.services.*;
import com.depot.ims.repositories.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import com.depot.ims.models.Availability;
import com.depot.ims.models.Site;
import com.depot.ims.models.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class AvailabilityRepositoryTest {

    @Mock
    private SiteRepository siteRepository; // Assuming UserRepository is a dependency of UserService

    //@InjectMocks
    //private Si siteService;





}
