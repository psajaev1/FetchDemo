package com.example.demo;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.example.demo.model.BadReceiptException;
import com.example.demo.model.BadTimeException;
import com.example.demo.model.IdResponse;
import com.example.demo.model.Item;
import com.example.demo.model.Receipt;
import com.example.demo.model.ReceiptNotFoundException;
import com.example.demo.service.FetchService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FetchServiceTest {

    @Autowired
    private FetchService fetchService;

    @Test
    public void testReceiptProcess() {
        Item item1 = new Item("Mountain Dew 12PK", "6.49");
        Item item2 = new Item("Emils Cheese Pizza", "12.25");
        Item item3 = new Item("Knorr Creamy Chicken", "1.26");
        Item item4 = new Item("Doritos Nacho Cheese", "3.35");
        Item item5 = new Item("  Klarbrunn 12-PK 12 FL OZ  ", "12.00");
        List<Item> items = Arrays.asList(item1, item2, item3, item4, item5);
        
        Receipt reciept = new Receipt("Target", "2022-01-01", "11:01", items, "35.35");
        IdResponse resp = fetchService.processReceipt(reciept);
        
        assertThat(fetchService.getReceiptPoints(resp.getId()).getPoints()).isEqualTo(28);
    }

    @Test
    public void testReceiptProcessTwo() {
        Item item1 = new Item("Gatorade", "2.25");
        Item item2 = new Item("Gatorade", "2.25");
        Item item3 = new Item("Gatorade", "2.25");
        Item item4 = new Item("Gatorade", "2.25");
        List<Item> items = Arrays.asList(item1, item2, item3, item4);
        
        Receipt reciept = new Receipt("M&M Corner Market", "2022-03-20", "14:33", items, "9.00");
        IdResponse resp = fetchService.processReceipt(reciept);
        
        assertThat(fetchService.getReceiptPoints(resp.getId()).getPoints()).isEqualTo(109);
    }

    @Test 
    public void badDate() {
        Item item1 = new Item("Gatorade", "2.25");
        List<Item> items = Arrays.asList(item1);
        Receipt reciept = new Receipt("Target", "01-01-2022", "11:01", items, "35.35");
        assertThrows(BadTimeException.class, () -> {fetchService.processReceipt(reciept);});
    }

    @Test 
    public void badTime() {
        Item item1 = new Item("Gatorade", "2.25");
        List<Item> items = Arrays.asList(item1);
        Receipt reciept = new Receipt("Target", "01-01-2022", "33:01", items, "35.35");
        assertThrows(BadTimeException.class, () -> {fetchService.processReceipt(reciept);});
    }

    @Test 
    public void missingItemFields() {
        Item item1 = new Item("Gatorade", null);
        List<Item> items = Arrays.asList(item1);
        Receipt reciept = new Receipt("Target", "01-01-2022", "33:01", items, "35.35");
        assertThrows(BadReceiptException.class, () -> {fetchService.processReceipt(reciept);});
    }

    @Test
    public void testMissingIdReceipt() {
        Item item1 = new Item("Gatorade", "2.25");
        Item item2 = new Item("Gatorade", "2.25");
        Item item3 = new Item("Gatorade", "2.25");
        Item item4 = new Item("Gatorade", "2.25");
        List<Item> items = Arrays.asList(item1, item2, item3, item4);
        
        Receipt reciept = new Receipt("M&M Corner Market", "2022-03-20", "14:33", items, "9.00");        
        assertThrows(ReceiptNotFoundException.class, () -> {fetchService.getReceiptPoints("some-fake-id").getPoints();});
    }   
}
