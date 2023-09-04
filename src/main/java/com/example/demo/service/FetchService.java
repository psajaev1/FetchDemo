package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.BadReceiptException;
import com.example.demo.model.BadTimeException;
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
     * Also does date validation and null item field validations
     * @Params Receipt Object 
     * @Returns IdResponse Object
     */
    public IdResponse processReceipt(Receipt receipt) {

        int points = 0;

        // One point for every alphanumeric character in the retailer name.
        long countAlphaNumeric = receipt.getRetailer().chars().filter(Character::isLetterOrDigit).count();
        points += countAlphaNumeric;

        // 50 points if the total is a round dollar amount with no cents.
        double purchaseTotal = Double.parseDouble(receipt.getTotal());
        if (purchaseTotal % 1 == 0)
            points += 50;
        
        if (purchaseTotal % 0.25 == 0)
            points += 25;

        // 5 points for every two items on the receipt.
        int itemPairs = receipt.getItems().size() / 2;
        points += (itemPairs * 5);

        // trim length point calculations
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

        // input date validation and calculations
        final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final DateTimeFormatter DATE_TIME_FORMATTER_TWO = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate dateByDay = isValidLocalDate(receipt.getPurchaseDate(), DATE_TIME_FORMATTER);
        LocalTime dateByHour = isValidLocalTime(receipt.getPurchaseTime(), DATE_TIME_FORMATTER_TWO);

        if (dateByDay != null && dateByDay.getDayOfMonth() % 2 == 1)
            points += 6;
        if (dateByHour != null &&  dateByHour.getHour() < 16 && ((dateByHour.getHour() == 14 && dateByHour.getMinute() > 0) || dateByHour.getHour() == 15))
            points += 10;


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
     * Checks if the purchaseDate is valid
     */
    public static LocalDate isValidLocalDate(String dateStr, DateTimeFormatter dateFormatter) {
        LocalDate date = null;
        try {
          date = LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
          throw new BadTimeException();
        }
        return date;
      }

    /* 
     * Checks if the purchaseTime is valid
     */  
    public static LocalTime isValidLocalTime(String dateStr, DateTimeFormatter dateFormatter) {
        LocalTime date = null;
        try {
          date = LocalTime.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
          throw new BadTimeException();
        }
        return date;
      }
}
