package com.nadjiel.storage.controllers;

import com.nadjiel.storage.dtos.ProductRecordDto;
import com.nadjiel.storage.models.ProductModel;
import com.nadjiel.storage.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository repository;

    @PostMapping("/products")
    public ResponseEntity<ProductModel> create(@RequestBody @Valid ProductRecordDto record) {
        var model = new ProductModel();

        BeanUtils.copyProperties(record, model);

        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(model));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> readAll() {
        return ResponseEntity.status(HttpStatus.OK).body(repository.findAll());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> readOne(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> product = repository.findById(id);

        if(product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(product.get());
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") UUID id, @RequestBody @Valid ProductRecordDto record) {
        Optional<ProductModel> product = repository.findById(id);

        if(product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }

        var model = product.get();

        BeanUtils.copyProperties(record, model);

        return ResponseEntity.status(HttpStatus.OK).body(repository.save(model));
    }

}
