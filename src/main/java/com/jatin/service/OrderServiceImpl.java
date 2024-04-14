package com.jatin.service;

import com.jatin.model.*;
import com.jatin.repository.*;
import com.jatin.request.CreateOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CartService cartService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Order createOrder(CreateOrderRequest order, User user) throws Exception {
        Address address = order.getDeliveryAddress();
        Restaurant restaurant = restaurantService.findRestaurantById(order.getRestaurantId());

        Order createdOrder = new Order();
        createdOrder.setCustomer(user);
        createdOrder.setRestaurant(restaurant);
        createdOrder.setOrderStatus("PENDING");
        createdOrder.setCreatedAt(new Date());
        createdOrder.setDeliveryAddress(address);

        Cart cart = cartService.findCartByUserId(user.getId());
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setFood(cartItem.getFood());
            orderItem.setIngredients(cartItem.getIngredients());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getTotalPrice());

            OrderItem savedOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(savedOrderItem);
        }

        createdOrder.setItems(orderItems);
        createdOrder.setTotalPrice(cartService.calculateCartTotal(cart));

        Order savedOrder = orderRepository.save(createdOrder);
        restaurant.getOrders().add(savedOrder);
        // restaurantRepository.save(restaurant);
        return savedOrder;
    }

    @Override
    public Order updateOrder(Long orderId, String orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        if (orderStatus.equals("OUT_FOR_DELIVERY")
                || orderStatus.equals("DELIVERED")
                || orderStatus.equals("COMPLETED")
                || orderStatus.equals("PENDING")
        ) {
            order.setOrderStatus(orderStatus);
            return orderRepository.save(order);
        }
        return null;
    }

    @Override
    public void cancelOrder(Long orderId) throws Exception {
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<Order> getUsersOrder(Long userId) throws Exception {
        return orderRepository.findByCustomerId(userId);
    }

    @Override
    public List<Order> getRestaurantsOrder(Long restaurantId, String orderStatus) throws Exception {
        List<Order> orders = orderRepository.findByRestaurantId(restaurantId);
        orders = orders.stream().filter(Order::isPaymentSuccess).collect(Collectors.toList());
        if (orderStatus != null)
            orders = orders.stream().filter(order -> order.getOrderStatus().equals(orderStatus)).collect(Collectors.toList());
        return orders;
    }

    @Override
    public Order findOrderById(Long orderId) throws Exception {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new Exception("Order not found");
        }
        return optionalOrder.get();
    }
}
