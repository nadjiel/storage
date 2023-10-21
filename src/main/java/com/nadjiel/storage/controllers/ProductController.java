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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        List<ProductModel> products = repository.findAll();

        if(!products.isEmpty()) {
            for(ProductModel product : products) {
                UUID id = product.getId();
                product.add(linkTo(methodOn(ProductController.class).readOne(id)).withSelfRel());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> readOne(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> product = repository.findById(id);

        if(product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found!");
        }

        product.get().add(linkTo(methodOn(ProductController.class).readAll()).withRel("Products List"));

        return ResponseEntity.status(HttpStatus.OK).body(product.get());
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") UUID id, @RequestBody @Valid ProductRecordDto record) {
        Optional<ProductModel> product = repository.findById(id);

        if(product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found!");
        }

        var model = product.get();

        BeanUtils.copyProperties(record, model);

        return ResponseEntity.status(HttpStatus.OK).body(repository.save(model));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> product = repository.findById(id);

        if(product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found!");
        }

        repository.delete(product.get());

        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
    }

}
