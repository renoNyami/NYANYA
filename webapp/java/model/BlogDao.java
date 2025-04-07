package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//这个类用于去封装博客表的基本操作
public class BlogDao {
    // 一. 往博客列表里插入一个博客.
    public void insert(Blog blog){
        //JDBC 的基本代码
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            // 1. 和数据库建立连接
            connection = DBUtil.getConnection();
            // 2. 构造 sql 语句
            String sql = "insert into blog values(null,?,?,?,now())";
            statement = connection.prepareStatement(sql);
            statement.setString(1,blog.getTitle());
            statement.setString(2,blog.getContent());
            statement.setInt(3,blog.getUserId());
            // 3. 执行 sql
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            // 4. 关闭资源
            DBUtil.close(connection,statement,null);
        }
    }

    //二. 能够获取到博客表中的所有博客的信息(用于博客列表页,注意此处的获取不一定就是里面的所有内容)
    public List<Blog> selectAll(){
        List<Blog> blogs = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            //我们想要让最新的排在博客列表最上面
            String sql = "select * from blog order by postTime desc";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                Blog blog = new Blog();
                blog.setBlogId(resultSet.getInt("blogId"));
                blog.setTitle(resultSet.getString("title"));
                //这里我们要针对长的内容进行截取
                String content = resultSet.getString("content");
                //自己设定长度
                if(content.length() > 50){
                    content = content.substring(0,50)+"...";
                }
                blog.setContent(content);
                blog.setUserId(resultSet.getShort("userId"));
                blog.setPostTime(resultSet.getTimestamp("postTime"));
                blogs.add(blog);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(connection,statement,resultSet);
        }
        return blogs;
    }

    //三. 能够根据博客 id 获取到指定的博客内容(用于在博客详情页)
    public Blog selectOne(int blogId){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "select * from blog where blogId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1,blogId);
            resultSet = statement.executeQuery();
            // 这里根据主键来查询的,查到的要么是 0,要么是 1..
            if(resultSet.next()){
                Blog blog = new Blog();
                blog.setBlogId(resultSet.getInt("blogId"));
                blog.setTitle(resultSet.getString("title"));
                blog.setContent(resultSet.getString("content"));
                blog.setUserId(resultSet.getShort("userId"));
                blog.setPostTime(resultSet.getTimestamp("postTime"));
                return blog;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(connection,statement,resultSet);
        }
        return null;
    }

    //四. 从博客列表中,根据博客 id 删除博客
    public boolean delete(int blogId){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "delete from blog where blogId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1,blogId);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }finally {
            DBUtil.close(connection,statement,null);
        }

        return true;
    }


    //五. 修改
    public void update(Blog blog) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            // 1. 建立连接
            connection = DBUtil.getConnection();
            // 2. 拼装 SQL 语句
            String sql = "update blog set content = ? ,title = ? where blogId = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, blog.getContent());
            statement.setString(2, blog.getTitle());

            statement.setInt(3, blog.getBlogId());
            // 3. 执行 SQL 语句
            int ret = statement.executeUpdate();
            if (ret == 1) {
                System.out.println("编辑成功");
            } else {
                System.out.println("编辑失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
    }

    //6. 计算个人文章的总数
    public static Integer selectTotal(int userId){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "select count(userId) from blog where userId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1,userId);
            resultSet = statement.executeQuery();
            if(resultSet.next()){

            }
            return resultSet.getInt(1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.close(connection,statement,resultSet);
        }
        return null;
    }

    public static void main(String[] args) {
        Integer ret1 = selectTotal(2);
        System.out.println(ret1);
        BlogDao blogDao = new BlogDao();
        Blog blog = new Blog();
        blog.setUserId(1);
        blog.setBlogId(1);
        blog.setTitle("hahah");
        blog.setContent("fff");
        blogDao.update(blog);
    }
}