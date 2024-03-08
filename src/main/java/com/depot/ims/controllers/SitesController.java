package com.depot.ims.controllers;

import com.depot.ims.repositories.SitesRepository;
import com.depot.ims.models.Sites;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/sites")

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
