package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Blog;
import model.BlogDao;
import model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

// 通过这个类, 来处理 /blog 路径对应的请求
@WebServlet("/blog")
public class BlogServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();

    // 这个方法用来获取到数据库中的博客列表.
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf8");
        BlogDao blogDao = new BlogDao();
        // 先尝试获取到 req 中的 blogId 参数. 如果该参数存在, 说明是要请求博客详情
        // 如果该参数不存在, 说明是要请求博客的列表.
        String param = req.getParameter("blogId");
        if (param == null) {
            // 不存在参数, 获取博客列表
            List<Blog> blogs = blogDao.selectAll();
            // 把 blogs 对象转成 JSON 格式.
            String respJson = objectMapper.writeValueAsString(blogs);
            resp.getWriter().write(respJson);
        } else {
            // 存在参数, 获取博客详情
            int blogId = Integer.parseInt(param);
            Blog blog = blogDao.selectOne(blogId);
            String respJson = objectMapper.writeValueAsString(blog);
            resp.getWriter().write(respJson);
        }
    }
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Blog blog = objectMapper.readValue(req.getReader(), Blog.class);
        BlogDao blogDao = new BlogDao();
        blogDao.update(blog);
        Map<String, String> result = new HashMap<>();
        result.put("message", "博客更新成功");
        String respJson = objectMapper.writeValueAsString(result);
        resp.setContentType("application/json;charset=utf8");
        resp.getWriter().write(respJson);
    }
}

