package com.ctc.sd.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.ctc.sd.entity.Article;
public interface ArticleDao {
	
	@Select("SELECT * FROM articles WHERE state = #{state}  AND userid =#{userid} ORDER BY id DESC LIMIT #{startno},10  ")
	public List<Article> getArticles(@Param("state") int state,@Param("userid") int userid,@Param("startno") int startno);
	
	@Insert("INSERT INTO articles (state,start_time,county,phone,title,content,userid) VALUES (0,now(),'测试区','18953384206','测试标题','测试内容',0) ;")
	public int insertTestAritcle();
	
	@Select("SELECT COUNT(0) FROM articles WHERE state = #{state}  AND userid =#{userid}")
	public int countArticles(@Param("state") int state,@Param("userid") int userid);

	@Select("SELECT * FROM articles WHERE id = #{id} ")
	public Article getArticleById(@Param("id") int id);

	@Update("UPDATE articles SET state=#{state},start_time=#{start_time},end_time=#{end_time},title=#{title},county=#{county},phone=#{phone},content=#{content} WHERE id =#{id}")
	public void update(@Param("id") int id, @Param("state") int state,@Param("start_time") Date start_time,
			@Param("end_time") Date end_time, @Param("title") String title, 
			@Param("county") String county, @Param("phone") String phone, 
			@Param("content") String content);

	@Insert("insert into articles(state,start_time,end_time,title,county,phone,content,userid) "+
            "values(#{state},#{start_time},#{end_time},#{title},#{county},#{phone},#{content},#{userid})")
	public void newArticle(@Param("state") int state, @Param("start_time") Date start_time, @Param("end_time") Date end_time, @Param("title") String title, @Param("county") String county, @Param("phone") String phone,
			@Param("content") String content,@Param("userid") int userid);
	
	
	@Delete("delete from articles where id =#{id}")
	public void delArticle(@Param("id") int id);

	@Select("SELECT * FROM articles WHERE state = #{state} AND userid=#{userid} AND start_time>#{start_time_startDate} AND start_time<#{start_time_endDate} AND end_time>#{end_time_startDate} AND end_time<#{end_time_endDate} AND title LIKE CONCAT('%',#{title},'%')   AND county LIKE CONCAT('%',#{county},'%') AND phone LIKE CONCAT('%',#{phone},'%') AND content LIKE CONCAT('%',#{content},'%') ORDER BY id DESC")
	public List<Article> searchProcessedArticles(@Param("state")  int state, @Param("userid") int userid,
			@Param("start_time_startDate")  Date start_time_startDate,@Param("start_time_endDate")  Date start_time_endDate,
			@Param("end_time_startDate")  Date end_time_startDate,@Param("end_time_endDate")   Date end_time_endDate, 
			@Param("title")  String title,@Param("county")  String county, 
			@Param("phone")String phone, @Param("content") String content);
	
	@Select("SELECT * FROM articles WHERE state = #{state} AND userid=#{userid} AND start_time>#{start_time_startDate} AND start_time<#{start_time_endDate}  AND title LIKE CONCAT('%',#{title},'%')   AND county LIKE CONCAT('%',#{county},'%') AND phone LIKE CONCAT('%',#{phone},'%') AND content LIKE CONCAT('%',#{content},'%') ORDER BY id DESC")
	public List<Article> searchProcessingArticles(@Param("state")  int state, @Param("userid") int userid,
			@Param("start_time_startDate")  Date start_time_startDate,@Param("start_time_endDate")  Date start_time_endDate,
			@Param("title")  String title,@Param("county")  String county, 
			@Param("phone")String phone, @Param("content") String content);
}
