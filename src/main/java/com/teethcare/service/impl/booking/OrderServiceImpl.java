package com.teethcare.service.impl.booking;

import com.teethcare.mapper.OrderMapper;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Order;
import com.teethcare.model.entity.OrderDetail;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.repository.OrderDetailRepository;
import com.teethcare.repository.OrderRepository;
import com.teethcare.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderDetailRepository orderDetailRepository;
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
//        OrderDetail =
        log.info("Booking is confirmed has services: " + booking.getServices().get(0).getName());
//        orderDetailRepository.saveAll(orderDetails);
//        order.setOrderDetails(orderDetails);
        log.info("Order has services: " + order.getOrderDetails().get(0).getServiceName());
        log.info("Saving order");
        save(order);

        return order;
    }
}
