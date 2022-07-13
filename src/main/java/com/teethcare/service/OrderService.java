package com.teethcare.service;

import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Order;

public interface OrderService extends CRUDService<Order> {
    Order createOrderFromBooking(Booking booking);
}
