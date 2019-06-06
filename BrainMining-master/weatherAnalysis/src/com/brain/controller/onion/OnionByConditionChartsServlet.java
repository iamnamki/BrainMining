package com.brain.controller.onion;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.brain.model.onion.OnionService;
import com.brain.model.onion.OnionVO;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


 /**
* @brief 총생산량 변화추이 chart
* @details
* @author "JungeunPark"
* @date 2018. 12. 18.
*/
@WebServlet("/onion/onionByConditioncharts.do")
public class OnionByConditionChartsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String selectedCondition = request.getParameter("condition");
		OnionService service = new OnionService();
		List<OnionVO> list = null;
		
		if(selectedCondition != null) {
			System.err.println(selectedCondition);
			if (selectedCondition.equals("output"))  {
			list = service.output();	
			System.out.println("outputChart");
			System.out.println(list);
		}	else if (selectedCondition.equals("area")) {
			list = service.area();	
			System.out.println("areaChart");
			System.out.println(list);
		}	else if (selectedCondition.equals("unitOutput")){
			list = service.unitOutput();	
			System.out.println("unitOutputChart");
			System.out.println(list);
		}
		}
	
		JsonObject data = new JsonObject();
		JsonArray arryCols = new JsonArray();
		JsonArray arrayRows = new JsonArray();
		
		List<String> cities = service.top5Region();
		System.out.println(cities);
		List<String> years = service.allYear();
		System.out.println(years);
		String[][] colvals = new String[6][];
		int idx=0;
		colvals[idx] = new String[]{"string", "연도"};
		String colType = "number";		
		for(String city : cities) {
			colvals[++idx] = new String[]{colType,city};
		}
		for (String[] s : colvals) {
			System.out.println(Arrays.toString(s));
			JsonObject col = new JsonObject();
			col.addProperty("type", s[0]);
			col.addProperty("label", s[1]);
//			System.out.println(col);
			arryCols.add(col);
		}
		
		for(String year : years) {
			JsonObject cell = new JsonObject();
			JsonObject ajaxObjYear = new JsonObject();
			JsonArray ajaxArryRowsC = new JsonArray();
			ajaxObjYear.addProperty("v", year);
			ajaxArryRowsC.add(ajaxObjYear);
				for(String city : cities) {
	            	for (OnionVO vo : list) {
	            		if(vo.getRegion().equals(city)&& vo.getYear().equals(year)) {
	        	            JsonObject ajaxObjRow1 = new JsonObject();	
	        	    		if(selectedCondition != null) {
	        	    			if (selectedCondition.equals("output"))  {
	        						ajaxObjRow1.addProperty("v", vo.getOutput());
	        	    		}	else if (selectedCondition.equals("area")) {
        							ajaxObjRow1.addProperty("v", vo.getArea());
	        	    		}	else if (selectedCondition.equals("unitOutput")){
        						ajaxObjRow1.addProperty("v", vo.getUnitOutput());
	        	    		}
	        	            
	        	    		}
						ajaxArryRowsC.add(ajaxObjRow1);
						break;
					}
				}
			}
			cell.add("c", ajaxArryRowsC);
			arrayRows.add(cell);
		}
		
		data.add("cols", arryCols);
		data.add("rows", arrayRows);
		response.setContentType("application/json;charset=UTF-8");
 		response.getWriter().print(data);
		response.getWriter().flush();
		}
}


