package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Blog;
import model.BlogDao;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

// 通过这个注解指定该Servlet处理的请求路径，这里假设处理的路径是 /publish（可根据实际情况调整）
@WebServlet("/publish")
public class Publish extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            // 从请求中读取数据并转换为Blog对象
            Blog blog = objectMapper.readValue(req.getReader(), Blog.class);
            BlogDao blogDao = new BlogDao();
            blogDao.insert(blog);
            HashMap<String, String> result = new HashMap<>();
            result.put("message", "博客新增成功");
            String respJson = objectMapper.writeValueAsString(result);
            resp.setContentType("application/json;charset=utf8");
            resp.getWriter().write(respJson);
        } catch (IOException e) {
            // 处理读取或转换数据出现的异常，返回合适的错误信息
            handleError(resp, "数据解析出错", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String singleBlogIdStr = req.getParameter("blogId");
        if (singleBlogIdStr!= null) {
            try {
                int singleBlogId = Integer.parseInt(singleBlogIdStr);
                BlogDao blogDao = new BlogDao();
                Blog blog = blogDao.selectOne(singleBlogId);
                if (blog!= null) {
                    String respJson = objectMapper.writeValueAsString(blog);
                    resp.setContentType("application/json;charset=utf8");
                    resp.getWriter().write(respJson);
                } else {
                    HashMap<String, String> errorResult = new HashMap<>();
                    errorResult.put("message", "未查询到指定ID的博客");
                    String respJson = objectMapper.writeValueAsString(errorResult);
                    resp.setContentType("application/json;charset=utf8");
                    resp.getWriter().write(respJson);
                }
            } catch (NumberFormatException e) {
                // 处理参数转换为整数出错的情况，返回相应错误信息
                handleError(resp, "参数格式错误，blogId应为整数", e);
            }
        } else {
            try {
                BlogDao blogDao = new BlogDao();
                List<Blog> blogs = blogDao.selectAll();
                String respJson = objectMapper.writeValueAsString(blogs);
                resp.setContentType("application/json;charset=utf8");
                resp.getWriter().write(respJson);
            } catch (IOException e) {
                // 处理查询所有博客出现异常的情况，返回合适错误提示
                handleError(resp, "查询博客列表出错", e);
            }
        }
    }

    private void handleError(HttpServletResponse resp, String errorMessage, Exception e) throws IOException {
        HashMap<String, String> errorResult = new HashMap<>();
        errorResult.put("message", errorMessage);
        String respJson = objectMapper.writeValueAsString(errorResult);
        resp.setContentType("application/json;charset=utf8");
        resp.getWriter().write(respJson);
        e.printStackTrace();
    }
}