package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import model.UserDao;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        resp.setCharacterEncoding("utf8");

        // 读取请求体中的JSON数据
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String line;
        while ((line = reader.readLine())!= null) {
            stringBuilder.append(line);
        }

        // 将读取到的JSON字符串解析为Map（假设发送的数据就是简单的包含username和password的JSON对象）
        Map<String, String> jsonData = objectMapper.readValue(stringBuilder.toString(), Map.class);

        // 从解析后的Map中获取参数
        String username = jsonData.get("username");
        String password = jsonData.get("password");

        System.out.println("username=" + username + ",password=" + password);
        if (username == null || "".equals(username) || password == null || "".equals(password)) {
            //请求的内容缺失,肯定是登录失败!!
            resp.setContentType("text/html; charset=utf8");
            resp.getWriter().write("当前的用户名或密码为空!!");
            return;
        }

        //2. 和数据库中的内容进行比较
        UserDao userDao = new UserDao();
        User user = userDao.selectByName(username);
        if (user == null ||!user.getPassword().equals(password)) {
            //请求的内容缺失,肯定是登录失败!!
            resp.setContentType("text/html; charset=utf8");
            resp.getWriter().write("当前的用户名或密码错误!!");
            return;
        }

        //3. 如果比较通过,就创建会话
        HttpSession session = req.getSession(true);
        //把刚才的用户信息,存储到会话中
        session.setAttribute("user", user);
        resp.getWriter().write("登陆成功!!");
        //4. 返回一个重定向报文,跳转到博客列表
        
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}

