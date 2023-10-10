package dev.bruno.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${v1API}/users")
public class UserController {

  @Autowired
  private IUserRepository userRepository;

  @PostMapping()
  public ResponseEntity<String> create(@RequestBody UserModel userModel) {
    System.out.println(userModel.getName());
    var user = this.userRepository.findByUsername(userModel.getUsername());

    if (user != null) {
      // Mensagem de erro
      // Status Code
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe.");
    }
    this.userRepository.save(userModel);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
