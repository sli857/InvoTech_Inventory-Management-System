package com.depot.ims.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.depot.ims.repositories.SitesRepository;

import java.sql.Timestamp;
import java.util.List;

import com.depot.ims.models.Site;

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

    @GetMapping("/sites?siteId={siteID}")
    public Site getSiteById (@PathVariable Long siteID) {
        return this.sitesRepository.findBySiteId(siteID);
    }
    @GetMapping("/sites?siteName={siteName}")
    public List<Site> getSitesByName(@PathVariable String siteName) {
        return this.sitesRepository.findBySiteName(siteName);
    }
    @GetMapping("/status?siteId={siteID}")
    public String getStatusBySiteId(@PathVariable Long siteID){
        return null;
    }
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Site addSite(@RequestBody Site site) {
        return sitesRepository.save(site);
    }

    @PostMapping(value="/status?siteId={siteID}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Site updateStatus(@PathVariable Long siteID, @RequestBody String status){
//        return sitesRepository.updateStatus(siteID, status);
        try {
            Site site = getSiteById(siteID);
            site.setSiteStatus(status);
            return site;
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @PostMapping(value="/name?siteId={siteID}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Site updateName(@PathVariable Long siteID, @RequestBody String name){
        try {
            Site site = getSiteById(siteID);
            site.setSiteName(name);
            return site;
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @PostMapping(value="/ceaseDate?siteId={siteID}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Site updateStatus(@PathVariable Long siteID, @RequestBody Timestamp ceaseDate){
        try {
            Site site = getSiteById(siteID);
            site.setCeaseDate(ceaseDate);
            return site;
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    @DeleteMapping("/deleteSites?siteId={siteID}")
    public Site deleteSite(@PathVariable Long siteID){
        return null;
    }


}
