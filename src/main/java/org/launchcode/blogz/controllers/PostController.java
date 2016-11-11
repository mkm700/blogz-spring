package org.launchcode.blogz.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.Post;
import org.launchcode.blogz.models.User;
import org.launchcode.blogz.models.dao.PostDao;
import org.launchcode.blogz.models.dao.UserDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostController extends AbstractController {

	@RequestMapping(value = "/blog/newpost", method = RequestMethod.GET)
	public String newPostForm() {
		return "newpost";
	}
	
	@RequestMapping(value = "/blog/newpost", method = RequestMethod.POST)
	public String newPost(HttpServletRequest request, Model model) {
		
		// TODO - implement newPost
		//get request parameters
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		
		//validate parameters
		if (title.isEmpty()) {
			model.addAttribute("error", "Please add a title");
			model.addAttribute("body", body);
			return "newpost";
		}
		
		if (body.isEmpty()) {
			model.addAttribute("error", "Please add text in the body");
			model.addAttribute("title", title);
			return "newpost";
		}
		
		//get the user and username
		HttpSession thisSession = request.getSession();
		User author = getUserFromSession(thisSession);

		
		//create a new post and save to DB
		Post blogPost = new Post(title, body, author);
		postDao.save(blogPost);
		
		String username = author.getUsername();
		int uid = blogPost.getUid();
		
		// TODO - this redirect should go to the new post's page
		return "redirect:" + username + "/" + uid;

	}
	
	//handles requests like /blog/chris/5
	@RequestMapping(value = "/blog/{username}/{uid}", method = RequestMethod.GET)
	
	public String singlePost(@PathVariable String username, @PathVariable int uid, Model model) {
		
		// TODO - implement singlePost
		
		//get the given post
		Post blogPost = postDao.findByUid(uid);
		String title = blogPost.getTitle();
		String body = blogPost.getBody();
		
		
		//pass the post into the template
		model.addAttribute("author", username);
		model.addAttribute("post", blogPost);
		
		return "post";
	}
	
	@RequestMapping(value = "/blog/{username}", method = RequestMethod.GET)
	public String userPosts(@PathVariable String username, Model model) {
		
		// TODO - implement userPosts
		//get all of the user's posts
		User blogUser = userDao.findByUsername(username);
		int uid = blogUser.getUid();
		List<Post> blogPosts = postDao.findByAuthor_uid(uid);
		
		//pass the posts into the template
		model.addAttribute("posts", blogPosts);
		
		return "blog";
	}
	
}
