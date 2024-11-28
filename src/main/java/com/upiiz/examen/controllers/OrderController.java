package com.upiiz.examen.controllers;

import com.upiiz.examen.entities.OrderEntity;
import com.upiiz.examen.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    //GET de todos
    @GetMapping
    public ResponseEntity<List<OrderEntity>> getAll(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    //GET
    @GetMapping("/{id}")
    public ResponseEntity<OrderEntity> getById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    //POST
    @PostMapping
    public ResponseEntity<OrderEntity> createOrder(@RequestBody OrderEntity order){
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<OrderEntity> updateOrder(@RequestBody OrderEntity order, @PathVariable Long id){
        order.setOrder_id(id);
        return ResponseEntity.ok(orderService.updateOrder(order));
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<OrderEntity> deleteOrder(@PathVariable Long id){
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
