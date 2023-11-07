package com.example.springtrash.common.filter;


import com.example.springtrash.common.exception.ErrorResponse;
import com.example.springtrash.common.exception.GlobalErrorCode;
import com.example.springtrash.common.exception.ServerErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
@Slf4j
public class LoginSessionFilter  implements Filter {

    private final ObjectMapper mapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        String method = req.getMethod();

        // /members로 온 post는 상관 없음.
        //

        String uri = req.getRequestURI();
        log.info("uri : {}, method : {}", uri, method);
        if (uri.equals("/members") && method.equalsIgnoreCase("post")) {
            chain.doFilter(request, response);
        }
        else if (uri.equals("/members/login")) {
            chain.doFilter(request, response);
        }
        // 세션 정보 체크
        else if (req.getSession().getAttribute("userInfo") == null) {
            // exception 내기
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setCharacterEncoding("UTF-8");
            String body = mapper.writeValueAsString(
                    ErrorResponse.of(GlobalErrorCode.UN_AUTHORIZED_ACCESS, uri));
            res.setStatus(GlobalErrorCode.UN_AUTHORIZED_ACCESS.getStatus().value());
            res.getWriter().println(body);

            return;
        }else {

            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
