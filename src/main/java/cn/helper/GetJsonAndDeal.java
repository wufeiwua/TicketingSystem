package cn.helper;


import cn.bean.TicketingBean;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.helper.UrlAndReturnJson.returnJson;

public class GetJsonAndDeal {

	// 获得余票信息的json字符串，并进行处理
	// https://kyfw.12306.cn/otn/leftTicket/queryZ?leftTicketDTO.train_date=2018-01-22&leftTicketDTO.from_station=NJH&leftTicketDTO.to_station=HZH&purpose_codes=ADULT
	public static List<String> getTicketsInfo(String train_date, String from_station, String to_station, String purpose_codes) throws Exception {
		List<String> ticketsInfo_list = new ArrayList<String>();
		List<String> jsonLists = new ArrayList<String>();
		String urlStr = "https://kyfw.12306.cn/otn/leftTicket/query?"
				+ "leftTicketDTO.train_date="
				+ train_date
				+ "&leftTicketDTO.from_station="
				+ from_station
				+ "&leftTicketDTO.to_station="
				+ to_station
				+ "&purpose_codes="
				+ purpose_codes;
		String ticketsInfo_json = returnJson(urlStr);
		JSONObject jsonObject = JSONObject.parseObject(ticketsInfo_json);
		JSONArray jsonArray_result = jsonObject.getJSONObject("data").getJSONArray("result");
		JSONObject jsonObject_map = jsonObject.getJSONObject("data").getJSONObject("map");
		Pattern p = Pattern.compile("[\\|].*");
		for (Object object : jsonArray_result) {
			Matcher m = p.matcher(object.toString());
			if (m.find()) {
				jsonLists.add(m.group(0));
			}
		}
		for (int i = 0; i < jsonLists.size(); i++) {
			String tempStr = jsonLists.get(i).toString();
			String tempArr[] = tempStr.split("\\|");
			List<String> info = new ArrayList<String>();
			for (String str : tempArr) {
				info.add(str);
			}
			List<String> ticketsInfo_list_temp = new ArrayList<String>();
			ticketsInfo_list_temp.add(info.get(2));
			ticketsInfo_list_temp.add(info.get(3));
			ticketsInfo_list_temp.add((String) jsonObject_map.get(info.get(6)));
			ticketsInfo_list_temp.add((String) jsonObject_map.get(info.get(7)));//得到车站名字
			ticketsInfo_list_temp.add(info.get(8));
			ticketsInfo_list_temp.add(info.get(9));
			ticketsInfo_list_temp.add(info.get(10));
			ticketsInfo_list_temp.add(info.get(13));
			ticketsInfo_list_temp.add(info.get(16));
			ticketsInfo_list_temp.add(info.get(17));
			ticketsInfo_list_temp.add(info.get(21));
			ticketsInfo_list_temp.add(info.get(23));
			ticketsInfo_list_temp.add(info.get(26));
			ticketsInfo_list_temp.add(info.get(28));
			ticketsInfo_list_temp.add(info.get(29));
			ticketsInfo_list_temp.add(info.get(30));
			ticketsInfo_list_temp.add(info.get(31));
			ticketsInfo_list_temp.add(info.get(32));
			ticketsInfo_list_temp.add(info.get(35));
			ticketsInfo_list.add(ticketsInfo_list_temp.toString().replaceAll("\\s*",""));//去除空格
		}
		return ticketsInfo_list;
	}

	/* get code by name and add map
    * name_station
    * url:https://kyfw.12306.cn/otn/resources/js/framework/station_name.js?station_version=1.9030
    *获取json字符串，分析json字符串，进行分割，用正则表达式取出需要的部分存入hashmap
    * */
	public static String getCodeByName(String stationName) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		//获取json写入字符串
		String urlStr = "https://kyfw.12306.cn/otn/resources/js/framework/station_name.js?station_version=1.9030";
		String nameString = returnJson(urlStr);
		//第一次处理json字符串
		String[] nameArr = nameString.split("\\@");//获得json由@分割开
		Pattern p = Pattern.compile("[^\\@].*\\|.*");
		List<String> jsonLists = new ArrayList<String>();
		for (Object object : nameArr) {
			Matcher m = p.matcher(object.toString());
			if (m.find()) {
				jsonLists.add(m.group(0));
			}
		}
		//第二次处理json字符互串
		for (int i = 0; i < jsonLists.size(); i++) {
			String temp = jsonLists.get(i).toString();
			String jsonArr[] = temp.split("\\|");
			List<String> mapLists = new ArrayList<String>();
			for (Object object : jsonArr) {
				mapLists.add(object.toString());
			}
			map.put(mapLists.get(1), mapLists.get(2));
		}
		return map.get(stationName);
	}

	public static JSONObject getPrice(TicketingBean tickets) throws Exception {
		String urlStr="https://kyfw.12306.cn/otn/leftTicket/queryTicketPrice?train_no="+tickets.getTrain_no()
				+"&from_station_no="+tickets.getFrom_station_no()
				+"&to_station_no="+tickets.getTo_station_no()
				+"&seat_types="+tickets.getSeat_types()
				+"&train_date="+tickets.getTrain_date();
		String priceString = returnJson(urlStr);
		JSONObject jsonObject = JSONObject.parseObject(priceString);
		JSONObject jsonObjectPrice = jsonObject.getJSONObject("data");
		return jsonObjectPrice;
	}
}
