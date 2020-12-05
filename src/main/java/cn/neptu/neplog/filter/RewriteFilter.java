package cn.neptu.neplog.filter;

import javax.servlet.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Rewrite filter forwarding request to vue
 */
public class RewriteFilter implements Filter {

    private final List<String> allowUrls = Arrays.asList("/index","/api","/js","/css");

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
//        req.getRequestDispatcher("/index.html").forward(req, resp);
    }
}
