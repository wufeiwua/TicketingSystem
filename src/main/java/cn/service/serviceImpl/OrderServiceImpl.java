package cn.service.serviceImpl;

import cn.bean.OrderBean;
import cn.dao.OrderMapper;
import cn.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhoumin
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService{
    @Resource(name="orderMapper")
    private OrderMapper orderMapper;

    public OrderMapper getOrderMapper() {
        return orderMapper;
    }

    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Transactional(isolation= Isolation.REPEATABLE_READ,propagation= Propagation.REQUIRED,readOnly = false)
    public void insertTickets(OrderBean order) {
         orderMapper.insertTickets(order);
    }

    public void updateTicket(String tickets_id) {
         orderMapper.updateTicket(tickets_id);
    }

    public List<OrderBean> selectOrderByUserId(Integer user_id) {
        return orderMapper.selectOrderByUserId(user_id);
    }

    public List<OrderBean> selectGone(Integer int_id) {
        return null;
    }

    public List<OrderBean> selectNotGo(Integer int_id) {
        return null;
    }

    @Transactional(isolation= Isolation.REPEATABLE_READ,propagation= Propagation.REQUIRED,readOnly = false)
    public void deleteTicketsByTicketsID(String tickets_id) {
        orderMapper.deleteTicketsByTicketsID(tickets_id);
    }

    public int countOrder(OrderBean order) {
        return orderMapper.countOrder(order);
    }

    public void insertEndorse(OrderBean order) {
         orderMapper.insertEndorse(order);
    }

    public List<OrderBean> selectTrip(Integer user_id) {
        return null;
    }

}
