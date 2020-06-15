package com.alexstrasza.webapi.controller;

import com.alexstrasza.webapi.models.DataContainer;
import com.alexstrasza.webapi.models.PlayerInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("currency/")
public class CurrencyController
{
    private String url = "http://localhost:8084/currency/";

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("retrieveBalance")
    public Integer RetrieveBalance(@RequestBody DataContainer user)
    {
        String fullUrl = url + "retrieveBalance";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DataContainer> entity = new HttpEntity<>(user, headers);

        return restTemplate.postForObject(fullUrl, entity, Integer.class);
    }

    @PostMapping("retrieveFloating")
    public Integer RetrieveFloating(@RequestBody DataContainer user)
    {
        String fullUrl = url + "retrieveFloating";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DataContainer> entity = new HttpEntity<>(user, headers);

        return restTemplate.postForObject(fullUrl, entity, Integer.class);
    }
}
