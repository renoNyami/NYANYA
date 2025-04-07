import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
@WebServlet("/context")
public class FileListServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        // 假设要获取WEB - INF目录下的文件列表，这里的路径是相对路径
        String relativePath = "/java";
        String realPath = servletContext.getRealPath(relativePath);
        File directory = new File(realPath);
        List<String> fileList = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files!= null) {
                for (File file : files) {
                    fileList.add(file.getName());
                }
            }
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>File List in " + relativePath + "</h1>");
        for (String file : fileList) {
            out.println(file + "<br>");
        }
        out.println("</body></html>");
    }
}