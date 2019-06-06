package com.brain.controller.admin.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.brain.model.admin.user.ManageUserService;
import com.brain.model.login.UserVO;


/**
* @brief 어드민 접속시 회원관리 페이지
* @details
* @author "InchangJung"
* @date 2018. 12. 18.
*/
@WebServlet("/admin/user.do")
public class ManageUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		Object sessionObjAd = session.getAttribute("admin");
		
		if(sessionObjAd == null) {
				response.sendRedirect(request.getContextPath() + "/index.jsp");
	
		} else {
			ManageUserService u_service = new ManageUserService();
			List<UserVO> list = u_service.selectAllUser();
			request.setAttribute("allUser", list);
			
			request.getRequestDispatcher("/admin/manageUser.jsp").forward(request, response);
		}
		
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}