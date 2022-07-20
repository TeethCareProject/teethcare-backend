package com.teethcare.service.impl.booking;

import com.teethcare.mapper.OrderMapper;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Order;
import com.teethcare.model.entity.OrderDetail;
import com.teethcare.repository.OrderRepository;
import com.teethcare.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    @Override
    public List<Order> findAll() {
        //TODO: implements later
        return null;
    }

    @Override
    public Order findById(int id) {
        return orderRepository.findById(id).orElse(null);
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
    @Transactional
    public Order createOrderFromBooking(Booking booking) {
        Order order = orderMapper.mapBookingToOrder(booking);
        List<OrderDetail> orderDetails = order.getOrderDetails();
        for(OrderDetail orderDetail : orderDetails) {
            orderDetail.setOrder(order);
        }
        log.info("Order has services: " + order.getOrderDetails().get(0).getServiceName());
        if (order.getDiscountValue() == null) {
            order.setDiscountValue(BigDecimal.ZERO);
        }
        save(order);
        log.info("Saving order");
        return order;
    }
}
