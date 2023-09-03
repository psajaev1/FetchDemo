package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.IdResponse;
import com.example.demo.model.PointsResponse;
import com.example.demo.model.Receipt;
import com.example.demo.service.FetchService;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/receipts")
@Slf4j
public class FetchController {

    @Autowired
    FetchService fetchService;
    
    @PostMapping(value="/process")
    public ResponseEntity<IdResponse> saveReceipts(@NonNull @RequestBody Receipt request) {
        log.info("Saving a receipt");
        return ResponseEntity.ok().body(fetchService.processReceipt(request));
    }

    @GetMapping("/{id}/points")
    public ResponseEntity<PointsResponse> getPoints(@PathVariable("id") String uuid) {
        log.info("Get receipt for " + uuid);
        return ResponseEntity.ok().body(fetchService.getReceiptPoints(uuid));
    }
}
