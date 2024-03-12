package com.depot.ims.controllers;

import com.depot.ims.repositories.SitesRepository;
import com.depot.ims.models.Sites;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/sites")
@CrossOrigin(origins = "http://localhost:5173")
public class SitesController {

    private final SitesRepository sitesRepository;

    public SitesController(SitesRepository sitesRepository) {
        this.sitesRepository = sitesRepository;
    }

    @GetMapping
    public List<Sites> getSites() {
        return this.sitesRepository.findAll();
    }

    @GetMapping("/{siteName}")
    public List<Sites> getSitesByName(@PathVariable String siteName) {
        return this.sitesRepository.findBySiteName(siteName);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Sites postMethodName(@RequestBody Sites site) {
        return this.sitesRepository.save(site);
    }

}
