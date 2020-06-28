package cn.action;

import cn.bean.OrderBean;
import cn.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.helper.SeatNumber.getSeatNo;

/**
 * @author zhoumin
 */
@Controller("orderAction")
@RequestMapping("/order")
public class OrderAction {
    @Resource(name="orderService")
    private OrderService orderService;

    public OrderService getOrderService() {
        return orderService;
    }
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping("/buyTickets")
    public String buyTickets(OrderBean order){
        Pattern p = Pattern.compile("\\d+\\.?\\d");
        Matcher m = p.matcher(order.getPrice());
        if (m.find()) {
            order.setPrice(m.group(0));
        }
        //行程冲突问题
        //do
        String seat_no = "";
        seat_no = getSeatNo(order.getSeat_types());//随机生成座位号
        order.setSeat_no(seat_no);
        if("无座".equals(order.getSeat_types()))
            return null;
        else{
            int count = orderService.countOrder(order);//同一天同一车不能同座位
            for( ;count > 0;){
                seat_no = getSeatNo(order.getSeat_types());
                order.setSeat_no(seat_no);
                count = orderService.countOrder(order);
            }
        }
        orderService.insertTickets(order);
        return "/pages/common/queryTickets.jsp";
    }

    /*查看订单*/
    @RequestMapping("/showOrder")
    public ModelAndView showOrder(String state, Integer user_id){
        ModelAndView model =  new ModelAndView();
        List<OrderBean> list = new ArrayList<OrderBean>();
        if("Y".equals(state)){
            list = orderService.selectGone(user_id);
        }else if("N".equals(state)){
            list = orderService.selectNotGo(user_id);
        }else{
            list = orderService.selectOrderByUserId(user_id);
        }
        model.addObject("_ORDER_",list);
        model.setViewName("/pages/common/order.jsp");
        return model;
    }

    //退票操作
    @RequestMapping("/cancelTickets")
    public String cancelTickets(HttpSession session,String tickets_id,Integer user_id){
        orderService.deleteTicketsByTicketsID(tickets_id);
        List<OrderBean> list = orderService.selectNotGo(user_id);
        if(list.size()> 0)
            session.setAttribute("_ORDER_",list);
        else
            session.setAttribute("_ORDER_",null);
        return "/pages/common/order.jsp";
    }

    //改签
    @RequestMapping("/toEndorseTickets")
    public String toEndorseTickets(HttpSession session,String ticketsId){
        session.setAttribute("tickets_ID",ticketsId);
        return "/pages/common/endorseQuery.jsp";
    }
    @RequestMapping("/endorseTickets")
    public String endorseTickets(HttpSession session,OrderBean order){
        System.out.println(order.toString());
        Pattern p = Pattern.compile("\\d+\\.?\\d");
        Matcher m = p.matcher(order.getPrice());
        if (m.find()) {
            order.setPrice(m.group(0));
        }
        //行程冲突问题
        //do
        String seat_no = "";
        seat_no = getSeatNo(order.getSeat_types());//随机生成座位号
        order.setSeat_no(seat_no);
        if("无座".equals(order.getSeat_types()))
            return null;
        else{
            int count = orderService.countOrder(order);//同一天同一车不能同座位
            for( ;count > 0;){
                seat_no = getSeatNo(order.getSeat_types());
                order.setSeat_no(seat_no);
                count = orderService.countOrder(order);
            }
        }
        orderService.updateTicket(order.getHistory_id());
        orderService.insertEndorse(order);
        List<OrderBean> list =  orderService.selectNotGo(order.getUser_id());
        session.setAttribute("_ORDER_",list);
        return "/pages/common/order.jsp";
    }

    @RequestMapping("/myTrip")
    public String myTrip(HttpSession session, Integer user_id){
        List<OrderBean> list = orderService.selectTrip(user_id);
        session.setAttribute("_TRIP_",list);
        return "/pages/common/myTrip.jsp";
    }

}
