import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = "/ConfigServlet", initParams = {
        @WebInitParam(name = "myParam", value = "123")
})
public class ConfigServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String paramValue = config.getInitParameter("myParam");
        System.out.println("初始化: " + paramValue);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf8");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("这是一个简单的Servlet示例");
        out.println("</body></html>");
    }
}