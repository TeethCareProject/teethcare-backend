package com.teethcare.service.impl.booking;

import com.teethcare.mapper.OrderMapper;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Order;
import com.teethcare.repository.OrderRepository;
import com.teethcare.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    @Override
    public List<Order> findAll() {
        //TODO: implements later
        return null;
    }

    @Override
    public Order findById(int id) {
        //TODO: implements later
        return null;
    }

    @Override
    public void save(Order entity) {
        orderRepository.save(entity);
    }

    @Override
    public void delete(int id) {
        //TODO: implements later
    }

    @Override
    public void update(Order entity) {
        //TODO: implements later
    }

    @Override
    public Order createOrderFromBooking(Booking booking) {
        Order order = orderMapper.mapBookingToOrder(booking);
        save(order);

        return order;
    }
}
