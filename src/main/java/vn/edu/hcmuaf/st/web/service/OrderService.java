package vn.edu.hcmuaf.st.web.service;

import vn.edu.hcmuaf.st.web.dao.OrderDao;
import vn.edu.hcmuaf.st.web.entity.Order;

public class OrderService {

    private final OrderDao orderDao;  

    public OrderService() {
        this.orderDao = new OrderDao();
    }

    public int createOrder(Order order) {
        return orderDao.insertOrder(order);
    }

    public Order getOrderById(int id) {
        return orderDao.getOrderById(id);
    }

    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        Order order = orderService.getOrderById(5);
        System.out.println(order);
    }

    public boolean updateSignature(int orderId, String signature) {
        return orderDao.updateSignature(orderId, signature);
    }
}
