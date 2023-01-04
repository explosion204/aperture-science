package com.explosion204.aperture.sandbox3.service;

import com.explosion204.aperture.sandbox3.controller.model.Student;
import com.explosion204.aperture.sandbox3.controller.model.Gender;
import com.explosion204.aperture.sandbox3.exception.StudentAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StudentsService {
  private static final List<Student> students = List.of(
          new Student(1L, "Ivan", "Ivanov", Gender.MALE, 1),
          new Student(2L, "Piotr", "Petrov", Gender.MALE, 2),
          new Student(3L, "John", "Doe", Gender.MALE, 3),
          new Student(4L, "Alicent", "Hightower", Gender.FEMALE, 4)
  );

  public List<Student> getStudents() {
    return getStudents(null);
  }

  public List<Student> getStudents(@Nullable Gender gender) {
    if (gender == null) {
      return students;
    }

    return students.stream()
            .filter(student -> student.getGender() == gender)
            .toList();
  }

  public Student createStudent(Student student) {
    if (students.contains(student)) {
      log.error("Student {} already exists", student);
      throw new StudentAlreadyExistsException();
    }

    log.info("Created student: {}", student);
    return student;
  }

  public Student updateStudent(Student student) {
    log.info("Updated student: {}", student);
    return student;
  }
}
