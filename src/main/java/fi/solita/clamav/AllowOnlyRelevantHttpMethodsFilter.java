package fi.solita.clamav;

import java.io.IOException;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AllowOnlyRelevantHttpMethodsFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String method = request.getMethod();
    if (HttpMethod.GET.matches(method) || HttpMethod.POST.matches(method)) {
      filterChain.doFilter(request, response);
    } else {
      response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
  }
}
