package com.example.demo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.BadReceiptException;
import com.example.demo.model.IdResponse;
import com.example.demo.model.PointsResponse;
import com.example.demo.model.Receipt;
import com.example.demo.model.ReceiptNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FetchService {

    // in memory map for tracking points. This map is a bean created at runtime. 
    @Autowired
    public Map<String, Integer> pointsMap;

    /* 
     * Method for processing receipts and assigning a point value to a randomly generated UUID
     */
    public IdResponse processReceipt(Receipt receipt) {

        validateReceipt(receipt);
        int points = 0;

        long countAlphaNumeric = receipt.getRetailer().chars().filter(Character::isLetterOrDigit).count();
        points += countAlphaNumeric;

        double purchaseTotal = Double.parseDouble(receipt.getTotal());
        if (purchaseTotal % 1 == 0)
            points += 50;
        
        if (purchaseTotal % 0.25 == 0)
            points += 25;

        int itemPairs = receipt.getItems().size() / 2;
        points += (itemPairs * 5);

        int addPoints = receipt.getItems().stream()
            .map(item -> {

                if (Objects.isNull(item.shortDescription()) || Objects.isNull(item.price()))
                    throw new BadReceiptException();


                String description = item.shortDescription();
                int itemPoints = 0;
                if (description.trim().length() % 3 == 0) {
                    itemPoints = (int) Math.ceil(Double.parseDouble(item.price()) * 0.2);
                }
                return itemPoints;
            }).mapToInt(Integer::intValue).sum();

        points += addPoints;

        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");  
        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
        Date calendarDate; 
        Date timeDate;

        // try catch block is needed when using the SimpleDateFormat
        try {
            calendarDate = formatter1.parse(receipt.getPurchaseDate());
            timeDate = formatter2.parse(receipt.getPurchaseTime());

            if (calendarDate != null && calendarDate.getDate() % 2 == 1)
                points += 6;
            if (timeDate != null &&  timeDate.getHours() < 16 && ((timeDate.getHours() == 14 && timeDate.getMinutes() > 0) || timeDate.getHours() == 15))
                points += 10;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String uuid = UUID.randomUUID().toString();
        log.info(String.format("Final Value of points for id receipt %s is %s", uuid, String.valueOf(points)));
        pointsMap.put(uuid, points);
        return new IdResponse(uuid);
    }
    /*
     * Get points of corresponding reciept ID
     */
    public PointsResponse getReceiptPoints(String id) {
        if (pointsMap.get(id) == null) {
            throw new ReceiptNotFoundException();
        }
        return new PointsResponse(pointsMap.get(id));
    }

    /* 
     * Validation of POST reciept Request body
     */
    public void validateReceipt(Receipt reciept) {
        if (Objects.isNull(reciept.getItems())  || 
            Objects.isNull(reciept.getRetailer()) || 
            Objects.isNull(reciept.getPurchaseDate()) || 
            Objects.isNull(reciept.getPurchaseTime()) || 
            Objects.isNull(reciept.getTotal())) {
                throw new BadReceiptException();
        }
    }
}
