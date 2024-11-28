package com.upiiz.examen.services;

import com.upiiz.examen.entities.OrderEntity;
import com.upiiz.examen.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<OrderEntity> getAllOrders(){
        return orderRepository.findAll();
    }

    public OrderEntity getOrderById(Long id){
        return orderRepository.findById(id).orElse(null);
    }

    //POST
    public OrderEntity createOrder(OrderEntity order){
        return orderRepository.save(order);
    }

    //PUT
    public OrderEntity updateOrder(OrderEntity order){
        if(orderRepository.existsById(order.getOrder_id())){
            return orderRepository.save(order);
        }
        return null;
    }

    //DELETE
    public void deleteById(Long id){
        if(orderRepository.existsById(id)){
            orderRepository.deleteById(id);
        }
    }
}
