package com.one.health.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.one.health.dto.MembersDto;
import com.one.health.dto.TrainnersDto;
import com.one.health.dto.UsersDto;
import com.one.health.service.MembersService;
import com.one.health.service.TrainnersService;
import com.one.health.service.UsersService;

@Controller
public class UsersController {
	
	private static Logger logger = LoggerFactory.getLogger(UsersController.class);
	
	@Autowired
	UsersService uService;
	
	@Autowired
	MembersService mService;
	
	@Autowired
	TrainnersService tService;
	
	@RequestMapping(value = "moveLogin.do", method = RequestMethod.GET)
	public String moveLogin() {
		logger.info("UsersController moveLogin " + new Date());
		
		return "users/login";	
	}
	
	@RequestMapping(value = "moveSignup.do", method = RequestMethod.GET)
	public String moveSignup() {
		logger.info("UsersController moveSignup " + new Date());
		
		return "users/signup";	
	}
	
	@RequestMapping(value = "login.do", method = RequestMethod.POST)
	public String login(UsersDto user, HttpSession session, HttpServletRequest req) {
		logger.info("UsersController login " + new Date());
		
		UsersDto dto = uService.getUsers(user);
		if(dto!=null)
		{
			if(user.getId().equals(dto.getId())&&user.getPw().equals(dto.getPw()))
			{
				req.getSession().setAttribute("login", dto);
				return "main";
			}
		}
		return "users/login";
	}
	
	@RequestMapping(value = "signup.do", method = RequestMethod.POST)
	public String signup(Model model, UsersDto user) {
		logger.info("UsersController signup " + new Date());
		
		uService.insertUsers(user);
		
		if(user.getAuth()==1)
		{
			mService.insertMembers(new MembersDto(user.getId(),user.getName(),0,0,0,0));
		}
		else
		{
			tService.insertTrainners(new TrainnersDto(user.getId(),user.getName(),1,"입력이 필요합니다.","입력이 필요합니다."));
		}
		return "users/login";
	}
	
}
