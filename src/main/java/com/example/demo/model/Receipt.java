package com.example.demo.model;

import java.util.List;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Receipt { 

    @NonNull String retailer;
    @NonNull String purchaseDate; 
    @NonNull String purchaseTime; 
    @NonNull List<Item> items; 
    @NonNull String total;
}
