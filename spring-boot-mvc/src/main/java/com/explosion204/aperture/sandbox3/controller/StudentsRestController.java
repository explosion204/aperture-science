package com.explosion204.aperture.sandbox3.controller;

import com.explosion204.aperture.sandbox3.controller.advice.EnableTimestampResponseBody;
import com.explosion204.aperture.sandbox3.controller.model.Gender;
import com.explosion204.aperture.sandbox3.controller.model.JsonViews;
import com.explosion204.aperture.sandbox3.controller.model.SecretIdentifier;
import com.explosion204.aperture.sandbox3.controller.model.Student;
import com.explosion204.aperture.sandbox3.controller.validation.CreateGroup;
import com.explosion204.aperture.sandbox3.controller.validation.UpdateGroup;
import com.explosion204.aperture.sandbox3.service.StudentsService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/students")
@Slf4j
@EnableTimestampResponseBody
@RequiredArgsConstructor
public class StudentsRestController {
  private final StudentsService studentsService;
  private final AsyncTaskExecutor asyncTaskExecutor;

  @GetMapping
  @JsonView(JsonViews.Student.External.class)
  public Callable<ResponseEntity<?>> getAllStudents(@RequestParam(name = "gender", required = false) Gender gender) {
    log.info("Log from request thread");

    return () -> {
      log.info("Log from async thread");
      final List<Student> students = studentsService.getStudents(gender);
      return ResponseEntity.ok()
              .cacheControl(CacheControl.maxAge(30, TimeUnit.SECONDS))
              .eTag(String.valueOf(students.size()))
              .body(students);
    };
  }

  @PostMapping
  @JsonView(JsonViews.Student.External.class)
  public DeferredResult<ResponseEntity<?>> createStudent(@Validated(CreateGroup.class) @RequestBody Student student,
                                                         @SecretIdentifier String secretIdentifier) {
    log.info("Log from request thread");
    final DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    asyncTaskExecutor.submit(() -> {
      log.info("Log from async thread");
      log.info("Create request with secret identifier: {}", secretIdentifier);

      final Student createdStudent = studentsService.createStudent(student);
      result.setResult(new ResponseEntity<>(createdStudent, HttpStatus.CREATED));
    });

    return result;
  }

  @PutMapping
  public ResponseEntity<Student> updateStudent(@Validated(UpdateGroup.class) @RequestBody Student student) {
    final Student savedStudent = studentsService.updateStudent(student);
    return ResponseEntity.ok(savedStudent);
  }

  @PostMapping("{with-matrix}")
  @JsonView(JsonViews.Student.External.class)
  public ResponseEntity<Student> createStudent(@MatrixVariable String firstName, @MatrixVariable String lastName,
                                               @MatrixVariable Gender gender) {

    final Student student = new Student(0L, firstName, lastName, gender, 0);
    final Student savedStudent = studentsService.createStudent(student);
    return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
  }
}
