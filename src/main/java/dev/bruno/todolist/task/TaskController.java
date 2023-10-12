package dev.bruno.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
  public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest httpRequest) {
    System.out.println(httpRequest.getAttribute("idUser"));
    taskModel.setIdUser((UUID) httpRequest.getAttribute("idUser"));

    // Validar data
    var currentDate = LocalDateTime.now();
    if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dates must have been greater than now");
    }

    if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start date must be less than now");
    }

    var task = this.taskRepository.save(taskModel);

    return ResponseEntity.status(HttpStatus.OK).body(task);
  }

  @GetMapping()
  public List<TaskModel> list(HttpServletRequest httpRequest) {
    var tasks = this.taskRepository.findByIdUser((UUID) httpRequest.getAttribute("idUser"));

    return tasks;
  }

}
