package my.controller;

import my.domain.student.Student;
import my.domain.student.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/student/save")
    public void save() {
        Student student = new Student("Eng2015001", "John Doe", Student.Gender.MALE, 1);
        studentRepository.save(student);
    }

    @GetMapping("/student/get")
    public Student get() {
        Student student = studentRepository.findOne("Eng2015001");
        return student;
    }

    @GetMapping("/student/update")
    public Student update() {
        Student student = studentRepository.findOne("Eng2015001");
        student.setName("Richard Watson");
        studentRepository.save(student);
        return student;
    }

    @GetMapping("/student/delete")
    public void delete() {
        Student student = studentRepository.findOne("Eng2015001");
        studentRepository.delete(student.getId());
    }

    @GetMapping("/student/getall")
    public List<Student> getAll() {
        Student engStudent = new Student(
                "Eng2015001", "John Doe", Student.Gender.MALE, 1);
        Student medStudent = new Student(
                "Med2015001", "Gareth Houston", Student.Gender.MALE, 2);
        studentRepository.save(engStudent);
        studentRepository.save(medStudent);

        List<Student> students = new ArrayList<>();
        studentRepository.findAll().forEach(students::add);

        return students;
    }
}
