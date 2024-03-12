package com.depot.ims.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import com.depot.ims.repositories.SitesRepository;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.depot.ims.models.Site;
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
    public List<Site> getSites() {
        return this.sitesRepository.findAll();
    }

    @GetMapping("/siteId={siteId}")
    public Site getSitesById (@PathVariable Integer siteId) {
        return this.sitesRepository.findBySiteId(siteId);
    }
    @GetMapping("/siteName={siteName}")
    public List<Site> getSitesByName(@PathVariable String siteName) {
        return this.sitesRepository.findBySiteName(siteName);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Site postMethodName(@RequestBody Site site) {
        return sitesRepository.save(site);
    }

}
