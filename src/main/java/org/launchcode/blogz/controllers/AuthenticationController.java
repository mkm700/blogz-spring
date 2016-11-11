package org.launchcode.blogz.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.User;
import org.launchcode.blogz.models.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController extends AbstractController {
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm() {
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) {
		
		// TODO - implement signup
		
		//get parameters from request
		String un = request.getParameter("username");
		String pw = request.getParameter("password");
		String verify = request.getParameter("verify");
		boolean isValidated = true;
		
		//validate username and verify passwords are the same and valid
		if(!User.isValidUsername(un)) {
			model.addAttribute("username_error", "Username is not valid");
			isValidated = false;
		}
		
		//TODO: add a check to see if username already exists
	
		if (!User.isValidPassword(pw)) {
			model.addAttribute("password_error", "Password is not valid");
			isValidated = false;
		}
		else if (!pw.equals(verify)) {
			model.addAttribute("verify_error", "Passwords do not match");
			isValidated = false;
		}
		
		//if they validate, create a new user
		//store them in the session (setUserInSession())
		
		if (isValidated) {
			HttpSession thisSession = request.getSession();
			User newBlogUser = new User(un, pw);
			
			//saves to DB
			userDao.save(newBlogUser);
			
			//stores user in session
			setUserInSession(thisSession, newBlogUser);
			
			return "redirect:blog/newpost";
		}
		
		//invalid data - reset username in form and send back to signup form with error messages
		else {
			model.addAttribute("username", un);
			return "/signup";
		}
		
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {
		
		// TODO - implement login
		
		//get parameters from request
		String un = request.getParameter("username");
		String pw = request.getParameter("password");
		
		//get user by their username
		//get data out of database
		//List<HelloLog> logs = helloLogDao.findAll();
		User blogUser = userDao.findByUsername(un);

		//check password is correct
		if (blogUser.isMatchingPassword(pw)) {
			//log them in, if so (ie setting the user in the session)
			HttpSession thisSession = request.getSession();
			setUserInSession(thisSession, blogUser);
		}
		else {
			//display error message, reset username in form, and return to login page
			model.addAttribute("username", un);
			model.addAttribute("error","Incorrect password. Please try again.");
			return "login";
		}
		
		return "redirect:blog/newpost";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
        request.getSession().invalidate();
		return "redirect:/";
	}
}
