/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.msun.luckyBlog.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.stereotype.Repository;

import com.msun.luckyBlog.persistence.domain.BlogView;

/**
 * mybatis的mapper 映射所有博客操作的sql语句
 * 
 * @author zxc Dec 1, 2016 6:37:14 PM
 */
@Repository
@Mapper
public interface BlogMapper {
      @Select({
              "select vid,title,tags",
              "from blog_view",
              "limit #{st},10"
      })
      List<BlogView> selectTenBlogs(@Param("st") int start) throws RuntimeException;

      @Select("select count(*) from blog_view")
      int selectBlogNum() throws RuntimeException;

      @Select("select distinct name from view_tag")
      @ResultType(String.class)
      List<String> selectTags() throws RuntimeException;

      @Select({"select vid,date,title",
              "from blog_view",
              "order by date desc",
              "limit #{st},12"})
      List<BlogView> selectArc(@Param("st") int start) throws RuntimeException;

      @Select({
              "select title,tags,md",
              "from blog_view",
              "where vid = #{id}",
              "limit 1"
      })
      BlogView selectAdmin(@Param("id") int id) throws RuntimeException;

      @Select({
              "select title,article",
              "from blog_view",
              "where vid = #{id}",
              "limit 1"
      })
      BlogView selectView(@Param("id") int id) throws RuntimeException;

      @Select({
              "select vid,title ",
              "from blog_view",
              "where vid < #{id}",
              "order by vid desc",
              "limit 1"
      })
      BlogView selectPreView(@Param("id") int vid) throws RuntimeException;

      @Select({
              "select vid,title ",
              "from blog_view",
              "where vid > #{id}",
              "limit 1"
      })
      BlogView selectNextView(@Param("id") int vid) throws RuntimeException;

      @Select({
              "select distinct vid",
              "from view_tag",
              "where name = #{tag}"
      })
      List<Integer> selectVidBytag(@Param("tag") String tagName) throws RuntimeException;

      @Select({
              "select date,title",
              "from blog_view",
              "where vid = #{vid}",
              "limit 1"
      })
      BlogView selectTagView(@Param("vid") int vid) throws RuntimeException;

      @Insert({"insert into blog_view " ,
              "(date,title,article,tags,md) " ,
              "values(#{bv.date},#{bv.title}," ,
              "#{bv.article},#{bv.tags},#{bv.md})"})
      @SelectKey(before=false,keyProperty="bv.vid",resultType=Integer.class,
              statementType= StatementType.STATEMENT,statement="SELECT LAST_INSERT_ID() AS id")
      int insertBlog(@Param("bv") BlogView blogView) throws RuntimeException;

      @Insert("insert ignore into view_tag (name,vid) values(#{tn},#{id})")
      int insertViewTag(@Param("tn") String tagName, @Param("id") int vid) throws RuntimeException;

      @Delete("delete from view_tag where vid = #{vid}")
      int deleteViewTag(@Param("vid") int vid) throws RuntimeException;

      @Delete("delete from blog_view where vid =#{vid} limit 1")
      int deleteBlogView(@Param("vid") int vid) throws RuntimeException;

      @Update({
            "update blog_view",
              "set title = #{bv.title},",
              "tags = #{bv.tags},",
              "md = #{bv.md},",
              "article = #{bv.article}",
              "where vid = #{bv.vid}"
      })
      void updateBlogView(@Param("bv") BlogView blogView) throws RuntimeException;
}
