package com.example.demo.model;

import org.springframework.lang.NonNull;

public record Item (@NonNull String shortDescription, @NonNull String price) {}
