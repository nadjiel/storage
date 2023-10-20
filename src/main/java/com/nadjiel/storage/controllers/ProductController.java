package com.nadjiel.storage.controllers;

import com.nadjiel.storage.dtos.ProductRecordDto;
import com.nadjiel.storage.models.ProductModel;
import com.nadjiel.storage.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
