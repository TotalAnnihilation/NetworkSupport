package com.ctc.sd.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ctc.sd.entity.Article;
import com.ctc.sd.entity.User;
import com.ctc.sd.service.ArticleService;
import com.ctc.sd.service.UserService;
import com.ctc.sd.utils.PageBean;


@Controller
public class NetworkSupportController {


	@Autowired
	private UserService userService;
	
	@Autowired
	private ArticleService articleService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("/")  
	  public String MainIndex(HttpServletRequest request, Model model ) {  
		User user=(User) request.getSession().getAttribute("user");
		if(user==null)
			return "login";  
		else
			return "redirect:/main/processing";
	  }  
	@RequestMapping("/help")  
	  public String help(Model model) {  
	    return "help";  
	  }  
	
	@RequestMapping("/login")  
	  public String login(Model model ) {  
	    return "login";  
	  }  
	@RequestMapping("/register")  
	  public String register(Model model ) {  
	    return "register";  
	  }  
	@RequestMapping(value = "/dologin", method = RequestMethod.POST)
    public String doLogin(HttpServletRequest request, User user, Model model) {
//		System.out.println(user);
        if (userService.login(user.getAccount(), user.getPassword())) {
        	User loginUser=userService.getUser(user.getAccount());
            request.getSession().setAttribute("user", loginUser);
            model.addAttribute("user", loginUser);
            return "redirect:/main/processing";
        } else {
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }
    }
	
	@RequestMapping(value = "/doregister", method = RequestMethod.POST)
    public String doRegister(HttpServletRequest request, User user, Model model) {
//		System.out.println(user);
		int flag=userService.register(user.getAccount(), user.getPassword(),user.getName(),user.getPhone());
		if(flag==0){
			model.addAttribute("error", "注册成功，请登录！");
            return "login";
		}
		else if(flag==1){
			model.addAttribute("error", "注册失败，用户名已经存在！");
			return "register";  
		}
		model.addAttribute("error", "发生未知错误，注册失败！");
		return "register";  
    }
	
	

	
	@RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request, Model model) {
            request.getSession().removeAttribute("user");
            return "login";
    }
	
	@RequestMapping(value = "/main/updateuser")
    public String updateUser(HttpServletRequest request, Model model) {
		User user=(User) request.getSession().getAttribute("user");
		model.addAttribute("user", user);
        return "main/updateuser";
    }
	
	@RequestMapping(value = "/main/doupdateuser")
    public String doUpdateUser(HttpServletRequest request, User user, Model model) {
		userService.update(user.getAccount(), user.getPassword(),user.getName(),user.getPhone());
		request.getSession().removeAttribute("user");
		return "redirect:/login";
    }
	
	@ResponseBody
	@RequestMapping(value = "/test")
	public String test(){
			articleService.insert();
			return "test";
	}
	
	@RequestMapping(value = "/main/processing")
    public Object processing(HttpServletRequest request,
    		@RequestParam(value="pageno",defaultValue="1")int pageno,
    		@RequestParam(value="error",required = false) String error
    		,Model model){
		if(error!=null)
			model.addAttribute("error", error);
		User user=(User) request.getSession().getAttribute("user");
		model.addAttribute("user", user);
		int pageSize=articleService.getPageSize(0, user.getId());
		if(pageno>pageSize)
			pageno=pageSize;
		if(pageno<1)
			pageno=1;
		PageBean<Article> pageData =articleService.findArticlesByPage(0,user.getId(),pageno);
		model.addAttribute("pageData", pageData);
        return "main/processing";
    }
	
	@RequestMapping(value = "/main/article")
    public Object changeArticle(HttpServletRequest request,@RequestParam(value="id",defaultValue="0")int articleid
            ,Model model){
		User user=(User) request.getSession().getAttribute("user");
		model.addAttribute("user", user);
		System.out.println(articleid);
		Article article=articleService.findArticleByID(articleid);
		model.addAttribute("Article", article);
        return "main/article";
    }
	
	@RequestMapping(value = "/main/doupdatearticle",method = RequestMethod.POST)
    public String doUpdateArticle(HttpServletRequest request, 
    		Article article, Model model,
    		@RequestParam(value = "finish", required = false) String finish,
    		RedirectAttributes attributes) {
		System.out.println("finish:"+finish);
		Article articleBefore=articleService.findArticleByID(article.getId());
		article.setStart_time(articleBefore.getStart_time());
		if(finish!=null){
			article.setState(1);
			Date date=new Date();
			article.setEnd_time(date);
		}else{
			article.setState(0);
			article.setEnd_time(null);
		}
		articleService.update(article.getId(), article.getState(),article.getStart_time(),article.getEnd_time(),article.getTitle(),article.getCounty(),article.getPhone(),article.getContent());
		attributes.addAttribute("error", "修改事项成功！"); 
		if(finish!=null)
			return "redirect:/main/processed";
		else
			return "redirect:/main/processing";
    }
	
	@RequestMapping(value = "/main/processed")
    public Object processed(HttpServletRequest request,
    		@RequestParam(value="pageno",defaultValue="1")int pageno,
    		@RequestParam(value="error",required = false) String error
    		,Model model){
		if(error!=null)
			model.addAttribute("error", error);
		User user=(User) request.getSession().getAttribute("user");
		model.addAttribute("user", user);
		int pageSize=articleService.getPageSize(1, user.getId());
		if(pageno>pageSize)
			pageno=pageSize;
		if(pageno<1)
			pageno=1;
		PageBean<Article> pageData =articleService.findArticlesByPage(1,user.getId(),pageno);
		model.addAttribute("pageData", pageData);
        return "main/processed";
    }
	
	@RequestMapping(value = "/main/newarticle")
    public Object newArticle(HttpServletRequest request
            ,Model model){
		User user=(User) request.getSession().getAttribute("user");
		model.addAttribute("user", user);
        return "main/newarticle";
    }
	
	@RequestMapping(value = "/main/donewarticle",method = RequestMethod.POST)
    public String doNewArticle(HttpServletRequest request, 
    		Article article, Model model,
    		@RequestParam(value = "finish", required = false) String finish,
    		RedirectAttributes attributes) {
		System.out.println(article);
		System.out.println("finish:"+finish);
		User user=(User) request.getSession().getAttribute("user");
		article.setStart_time(new Date());
		if(finish!=null){
			article.setState(1);
			Date date=new Date();
			article.setEnd_time(date);
		}else{
			article.setState(0);
			article.setEnd_time(null);
		}
		articleService.newArticle(article.getState(),article.getStart_time(),article.getEnd_time(),article.getTitle(),article.getCounty(),article.getPhone(),article.getContent(),user.getId());
		attributes.addAttribute("error", "新建事项成功！"); 
		if(finish!=null)
			return "redirect:/main/processed";
		else
			return "redirect:/main/processing";
    }
	
	
	@RequestMapping(value = "/main/delarticle")
    public Object delarticle(HttpServletRequest request,
    		@RequestParam(value="id",required = true)int id,
    		RedirectAttributes attributes,
    		Model model){
		Article article=articleService.findArticleByID(id);
		int state=article.getState();
		articleService.delArticle(id);
		attributes.addAttribute("error", "删除事项："+article.getTitle()); 
		if(state==1)
			return "redirect:/main/processed";
		else
			return "redirect:/main/processing";
		
	}
	
	@RequestMapping(value = "/main/searcharticle")
    public Object searchArticle(HttpServletRequest request
            ,Model model){
		User user=(User) request.getSession().getAttribute("user");
		model.addAttribute("user", user);
		long oneMonthMS=2592000000L;
		Date endDate=new Date();
		Date startDate=new Date(endDate.getTime()-oneMonthMS);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String time_start=sdf.format(startDate);
		String time_end=sdf.format(endDate);
		System.out.println(time_start);
		System.out.println(time_end);
		model.addAttribute("time_end", time_end);
		model.addAttribute("time_start", time_start);
        return "main/searcharticle";
    }
	
	@RequestMapping(value = "/main/dosearcharticle",method = RequestMethod.POST)
    public String doSearchArticle(HttpServletRequest request, 
    		Article article, Model model,
    		@RequestParam(value = "finish", required = false) String finish,
    		@RequestParam(value = "start_time_start", required = false) String start_time_start,
    		@RequestParam(value = "start_time_end", required = false) String start_time_end,
    		@RequestParam(value = "end_time_start", required = false) String end_time_start,
    		@RequestParam(value = "end_time_end", required = false) String end_time_end) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(article);
		User user=(User) request.getSession().getAttribute("user");
		model.addAttribute("user", user);
		Long oneDayMS=86400000L;
		Date start_time_startDate=null;
		try {
			start_time_startDate=sdf.parse(start_time_start);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date start_time_endDate=null;
		try {
			start_time_endDate=sdf.parse(start_time_end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		start_time_endDate=new Date(start_time_endDate.getTime()+oneDayMS);
		Date end_time_startDate=null;
		try {
			end_time_startDate=sdf.parse(end_time_start);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date end_time_endDate=null;
		try {
			end_time_endDate=sdf.parse(end_time_end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		end_time_endDate=new Date(end_time_endDate.getTime()+oneDayMS);
		if(finish!=null){
			article.setState(1);
		}else{
			article.setState(0);
		}
		List<Article> articles=articleService.searchArticles(article.getState(),user.getId(),start_time_startDate,start_time_endDate,
        		end_time_startDate,end_time_endDate,article.getTitle(),article.getCounty(),article.getPhone(),article.getContent());
		System.out.println(articles.toString());
		model.addAttribute("articles", articles);
		
		if(finish!=null){
			return "main/searchprocessedresult";
		}else{
			return "main/searchprocessingresult";
		}
    }
	
}
