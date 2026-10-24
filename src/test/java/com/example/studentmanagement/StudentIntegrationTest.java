package com.example.studentmanagement;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudentIntegrationTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void testSaveAndFindAll() {
        Calendar cal = Calendar.getInstance();
        cal.set(1998, Calendar.MARCH, 15);
        Student student = new Student("ALAMI", "Ahmed", cal.getTime());

        Student saved = studentService.save(student);
        assertNotNull(saved.getId());

        List<Student> list = studentService.findAll();
        assertFalse(list.isEmpty());
        assertTrue(list.stream().anyMatch(s -> "ALAMI".equals(s.getNom())));
    }

    @Test
    void testFindNbrStudentByYearQuery() {
        Calendar cal1 = Calendar.getInstance();
        cal1.set(1995, Calendar.JANUARY, 10);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(1995, Calendar.MAY, 20);
        Calendar cal3 = Calendar.getInstance();
        cal3.set(2000, Calendar.JULY, 1);

        studentService.save(new Student("BENANI", "Sara", cal1.getTime()));
        studentService.save(new Student("IDRISSI", "Karim", cal2.getTime()));
        studentService.save(new Student("TAZI", "Fatima", cal3.getTime()));

        Collection<?> stats = studentService.findNbrStudentByYear();
        assertNotNull(stats);
        assertFalse(stats.isEmpty());
    }

    @Test
    void testDeleteStudent() {
        Student student = new Student("TEST", "Delete", new Date());
        Student saved = studentService.save(student);

        boolean deleted = studentService.delete(saved.getId());
        assertTrue(deleted);

        Student notFound = studentRepository.findById(saved.getId());
        assertNull(notFound);
    }
}
