package bz.sunlight.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import bz.sulight.common.BaseConstant;
import bz.sunlight.entity.CustomerServer;
import bz.sunlight.service.CustomerServerService;
import bz.sunlight.util.CookieUtil;

@Controller
@RequestMapping("/user/")
public class UserController {
	
	@Autowired
	private HttpServletRequest  request;
	
	@Autowired
    private HttpServletResponse response;

	@Autowired
	private CustomerServerService customerServerService;
	
	@RequestMapping(value="login/{userName}/{password}",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<CustomerServer> login(@PathVariable String userName,@PathVariable String password){
		CustomerServer customerServer = customerServerService.getCustomerServer(userName, password, BaseConstant.BASE_TRUE);
		if(customerServer==null){
			return new ResponseEntity<CustomerServer>(HttpStatus.NO_CONTENT);
		}
//		将客服的id 和 昵称  写入到cookie中
		CookieUtil cookieUtil=new CookieUtil(request, response);
		cookieUtil.addCookie("customerServerId", customerServer.getId());
		cookieUtil.addCookie("customerServerName", customerServer.getNickName());
		return new ResponseEntity<CustomerServer>(customerServer,HttpStatus.OK);
	}
	
	@RequestMapping(value="logout",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Void> logout(){
		CookieUtil cookieUtil=new CookieUtil(request, response);
		cookieUtil.deleteCookie("customerServerId");
		cookieUtil.deleteCookie("customerServerName");
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	
	
}
