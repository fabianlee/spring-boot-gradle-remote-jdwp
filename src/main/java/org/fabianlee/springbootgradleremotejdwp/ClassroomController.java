package org.fabianlee.springbootgradleremotejdwp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.javafaker.Faker;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/class")
public class ClassroomController {
	
	private final Random r = new Random();
	
	private List<Student> studentList = new ArrayList<Student>(
			Arrays.asList(new Student("Alice",21), new Student("Bob",29), new Student("Charlie",38))
			);

    // return back all students
    @GetMapping(value="/student")
    public ResponseEntity<List<Student>> getStudents() {
        log.debug("getStudents");

        return new ResponseEntity<List<Student>>(studentList,HttpStatus.OK);
    }

    // return back single user added
    @PostMapping(value="/student")
    public ResponseEntity<Student> addStudent() {
        log.debug("addStudent");
        
        // create new user with random name and age
        Faker faker = new Faker();
        String theNewName = faker.name().firstName();
        Student newStudent = new Student(theNewName,r.nextInt(78));
        studentList.add(newStudent);

        return new ResponseEntity<Student>(newStudent,HttpStatus.CREATED);
    }
    

    // delete last user, return HTTP code=200 and OK if deleted
    // if no users to delete, returns back HTTP code=417
    @DeleteMapping(value="/student")
    public ResponseEntity<List<String>> deleteStudent() {
        log.debug("deleteStudent");
    	
    	int nstudents = studentList.size();
        if (studentList.size() > 0) {
            studentList.remove(studentList.size() - 1);
        	log.debug("deleteUser: #students was " + nstudents + " and is now  " + studentList.size());
        	List<String> res = new ArrayList<String>(Arrays.asList("ok"));
        	return new ResponseEntity<List<String>>(res,HttpStatus.OK);
        }
        
        List<String> res = new ArrayList<String>(Arrays.asList("empty"));
        return new ResponseEntity<List<String>>(res,HttpStatus.EXPECTATION_FAILED); // code=417
    }
    
}
