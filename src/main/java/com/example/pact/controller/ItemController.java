package com.example.pact.controller;

import com.example.pact.model.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ItemController {

    @GetMapping("/item/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable String id) {
        Item item = new Item(id, "Item Name", "Item Description");
        return ResponseEntity.ok(item);
    }

    @PostMapping("/item")
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        // Just echoing the same item for simplicity.
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<Item> deleteItem(@PathVariable String id) {
        return ResponseEntity.ok().build();
    }
}


