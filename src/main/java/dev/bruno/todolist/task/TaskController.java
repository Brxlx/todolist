package dev.bruno.todolist.task;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("${v1API}/tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping()
  public TaskModel create(@RequestBody TaskModel taskModel, HttpServletRequest httpRequest) {
    System.out.println(httpRequest.getAttribute("idUser"));
    taskModel.setIdUser((UUID) httpRequest.getAttribute("idUser"));
    var task = this.taskRepository.save(taskModel);

    return task;
  }

}
