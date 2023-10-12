package dev.bruno.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dev.bruno.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (request.getServletPath().equals("/api/v1/tasks")) {

      // Pegar dados da autenticação
      var encondedAuthData = request.getHeader("Authorization").substring("Basic".length()).trim();

      byte[] decodedAuth = Base64.getDecoder().decode(encondedAuthData);

      var authData = new String(decodedAuth);

      String[] credentials = authData.split(":");

      String username = credentials[0];
      String password = credentials[1];

      // Validar usuário
      var hasUser = this.userRepository.findByUsername(username);
      if (hasUser == null) {
        response.sendError(401, "Unauthorized");
      } else {
        // Validar senha
        var verifiedPassword = BCrypt.verifyer().verify(password.toCharArray(), hasUser.getPassword());
        if (verifiedPassword.verified) {
          request.setAttribute("idUser", hasUser.getId());
          // Seguir execução do código
          filterChain.doFilter(request, response);
        } else {
          // Não não
          response.sendError(401, "Unauthorized");
        }
      }
    } else {
      filterChain.doFilter(request, response);
    }
  }

}
