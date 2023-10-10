package dev.bruno.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
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
  public UserModel create(@RequestBody UserModel userModel) {
    System.out.println(userModel.getName());
    var user = this.userRepository.findByUsername(userModel.getUsername());

    if(user != null){
      System.out.println("Usuário já existe.");
      return null;
    }

    return this.userRepository.save(userModel);
  }
}