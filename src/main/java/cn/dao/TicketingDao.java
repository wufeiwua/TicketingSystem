package cn.dao;

import cn.bean.PriceBean;
import cn.bean.TicketingBean;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static cn.helper.GetJsonAndDeal.getCodeByName;
import static cn.helper.GetJsonAndDeal.getPrice;
import static cn.helper.QueryTickets.queryTickets;

/**
 * @author zhoumin
 * @date 2018/3/14 21:28
 */
public class TicketingDao {

    public static List<TicketingBean> selectTickets(String train_date,String from_station_name,String to_station_name,String purpose_codes) throws Exception {
        String from_station = getCodeByName(from_station_name.replaceAll("\\s*",""));
        String to_station = getCodeByName(to_station_name.replaceAll("\\s*",""));//将名字转换为标准的地名代码
        List<TicketingBean> ticketsList = queryTickets(train_date,from_station,to_station,purpose_codes);
        return ticketsList;
    }

    public static List<PriceBean> selectPrice(TicketingBean tickets) throws Exception {
        JSONObject priceMap ;
        PriceBean priceBean = new PriceBean();
        priceMap=getPrice(tickets);
        if(priceMap.get("WZ") != null)
            priceBean.setWZ(priceMap.getString("WZ"));
        if(priceMap.get("A1") != null)
            priceBean.setA1(priceMap.getString("A1"));
        if(priceMap.get("A2") != null)
            priceBean.setA2(priceMap.getString("A2"));
        if(priceMap.get("A3") != null)
            priceBean.setA3(priceMap.getString("A3"));
        if(priceMap.get("A4") != null)
            priceBean.setA4(priceMap.getString("A4"));
        if(priceMap.get("A6") != null)
            priceBean.setA6(priceMap.getString("A6"));
        if(priceMap.get("F") != null)
            priceBean.setF(priceMap.getString("F"));
        if(priceMap.get("O") != null)
            priceBean.setO(priceMap.getString("O"));
        if(priceMap.get("M") != null)
            priceBean.setM(priceMap.getString("M"));
        if(priceMap.get("P") != null)
            priceBean.setP(priceMap.getString("P"));
        if(priceMap.get("A9") != null)
            priceBean.setA9(priceMap.getString("A9"));
        List<PriceBean> listPrice = new ArrayList<PriceBean>();
        listPrice.add(priceBean);
        return listPrice;
    }

}



