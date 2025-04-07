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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        resp.setCharacterEncoding("utf8");

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

        System.out.println(username + password);
        if(username == null || "".equals(username) || password == null || "".equals(password)){
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("用户名或密码不能为空!!!");
            return;
        }
        UserDao userDao = new UserDao();
        User user1 = userDao.selectByName(username);
        if(user1 != null){
            resp.setContentType("text/html;charset=utf8");
            resp.getWriter().write("用户名已被占用,请更改用户名!!!");
            return;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userDao.insert(user);
        HttpSession session = req.getSession(true);
        session.setAttribute("user",user);
        resp.sendRedirect("blog_list.html");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}

