package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.BlogDao;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

// 通过这个注解指定该Servlet处理的请求路径，这里假设处理的路径是 /deleteBlog（可根据实际情况调整）
@WebServlet("/deleteBlog")
public class deleteBlog extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 实际中，若删除操作需要一些额外的验证等逻辑，可以在这里添加，比如验证用户是否有权限删除等
        // 这里暂时假设可以直接根据传入的博客ID进行删除操作

        String blogIdStr = req.getParameter("blogId");
        if (blogIdStr == null) {
            // 如果没有传入博客ID参数，返回错误信息
            handleError(resp, "缺少必要参数blogId，无法执行删除操作", null);
            return;
        }

        try {
            int blogId = Integer.parseInt(blogIdStr);
            BlogDao blogDao = new BlogDao();
            boolean isDeleted = blogDao.delete(blogId); // 假设BlogDao中有delete方法用于删除博客，根据实际调整
            if (isDeleted) {
                HashMap<String, String> result = new HashMap<>();
                result.put("message", "博客删除成功");
                String respJson = objectMapper.writeValueAsString(result);
                resp.setContentType("application/json;charset=utf8");
                resp.getWriter().write(respJson);
            } else {
                handleError(resp, "删除博客失败，可能该博客不存在或其他原因", null);
            }
        } catch (NumberFormatException e) {
            // 如果传入的blogId参数格式不正确，转换整数出错
            handleError(resp, "参数blogId格式错误，应为整数", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 通常删除操作建议用POST方式更符合语义，但这里也简单实现下GET方式的处理，可根据实际需求调整或删除
        String blogIdStr = req.getParameter("blogId");
        if (blogIdStr == null) {
            handleError(resp, "缺少必要参数blogId，无法执行删除操作", null);
            return;
        }

        try {
            int blogId = Integer.parseInt(blogIdStr);
            BlogDao blogDao = new BlogDao();
            boolean isDeleted = blogDao.delete(blogId);
            if (isDeleted) {
                HashMap<String, String> result = new HashMap<>();
                result.put("message", "博客删除成功");
                String respJson = objectMapper.writeValueAsString(result);
                resp.setContentType("application/json;charset=utf8");
                resp.getWriter().write(respJson);
            } else {
                handleError(resp, "删除博客失败，可能该博客不存在或其他原因", null);
            }
        } catch (NumberFormatException e) {
            handleError(resp, "参数blogId格式错误，应为整数", e);
        }
    }

    private void handleError(HttpServletResponse resp, String errorMessage, Exception e) throws IOException {
        HashMap<String, String> errorResult = new HashMap<>();
        errorResult.put("message", errorMessage);
        String respJson = objectMapper.writeValueAsString(errorResult);
        resp.setContentType("application/json;charset=utf8");
        resp.getWriter().write(respJson);
        if (e!= null) {
            e.printStackTrace();
        }
    }
}