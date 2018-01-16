package com.ctc.sd.service;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ctc.sd.dao.ArticleDao;
import com.ctc.sd.entity.Article;
import com.ctc.sd.utils.PageBean;

@Service
public class ArticleService {
	@Autowired
	private ArticleDao articleDao;
	
	public void insert() {
        for(int i=0;i<100;i++)
        	articleDao.insertTestAritcle();
    }
	
	public PageBean<Article> findArticlesByPage(int state, int userid, int currentPage) {
		int startno=(currentPage-1)*10;
        List<Article> allArticles = articleDao.getArticles(state,userid,startno);       //全部商品
        int countNums = articleDao.countArticles(state,userid);            //总记录数
        PageBean<Article> pageData = new PageBean<Article>(currentPage, countNums);
        pageData.setItems(allArticles);
        return pageData;
    }
	
	public int getPageSize(int state,int userid){
		int countNums = articleDao.countArticles(state,userid);
		return (countNums+9)/10;
	}

	public Article findArticleByID(int articleid) {
		Article article = articleDao.getArticleById(articleid);  
		return article;
	}

	public void update(int id, int state, Date start_time, Date end_time, String title, String county, String phone, String content) {
		articleDao.update(id,state,start_time,end_time,title,county,phone,content);
		
	}

	public void newArticle(int state, Date start_time, Date end_time, String title, String county, String phone,
			String content,int userid) {
		articleDao.newArticle(state,start_time,end_time,title,county,phone,content,userid);
	}

	public void delArticle(int id) {
		articleDao.delArticle(id);
	}
	
	public List<Article> searchArticles(int state, int userid, Date start_time_startDate,
			Date start_time_endDate,Date end_time_startDate,Date end_time_endDate,
			String title, String county, String phone,String content) {
        List<Article> articles = null;
        if(state==1)
        	articles =articleDao.searchProcessedArticles(state,userid,start_time_startDate,start_time_endDate,
        		end_time_startDate,end_time_endDate,title,county,phone,content);   
        else
        	articles =articleDao.searchProcessingArticles(state,userid,start_time_startDate,start_time_endDate,
            		title,county,phone,content);   
        return articles;
	}
    
}
