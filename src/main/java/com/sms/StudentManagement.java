package com.sms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class StudentManagement {

    // ======================= MODEL =======================
    static class Student {
        int id;
        String name;
        String department;
        double marks;

        public Student(int id, String name, String department, double marks) {
            this.id = id;
            this.name = name;
            this.department = department;
            this.marks = marks;
        }

        @Override
        public String toString() {
            return "ID: " + id +
                    " | Name: " + name +
                    " | Dept: " + department +
                    " | Marks: " + marks;
        }
    }

    // ======================= REPOSITORY =======================
    static class StudentRepository {
        List<Student> students = new ArrayList<>();

        boolean existsById(int id) {
            return students.stream().anyMatch(s -> s.id == id);
        }

        void save(Student s) {
            students.add(s);
        }

        Optional<Student> findById(int id) {
            return students.stream().filter(s -> s.id == id).findFirst();
        }

        boolean deleteById(int id) {
            return students.removeIf(s -> s.id == id);
        }

        List<Student> findAll() {
            return students;
        }

        List<Student> findSortedByName() {
            List<Student> list = new ArrayList<>(students);
            list.sort(Comparator.comparing(s -> s.name.toLowerCase()));
            return list;
        }

        int count() {
            return students.size();
        }
    }

    // ======================= SERVICE =======================
    static class StudentService {
        StudentRepository repo = new StudentRepository();

        void addStudent(int id, String name, String dept, double marks) {
            if (repo.existsById(id)) {
                System.out.println("ID already exists!");
                return;
            }
            if (name.trim().isEmpty()) {
                System.out.println("Name cannot be empty");
                return;
            }
            if (marks < 0 || marks > 100) {
                System.out.println("Marks must be 0–100");
                return;
            }
            repo.save(new Student(id, name, dept, marks));
            System.out.println("Student added.");
        }

        void listStudents() {
            if (repo.findAll().isEmpty()) {
                System.out.println("No students found.");
                return;
            }
            repo.findAll().forEach(System.out::println);
        }

        void searchStudent(int id) {
            Optional<Student> s = repo.findById(id);
            System.out.println(s.isPresent() ? s.get() : "Student not found.");
        }

        void deleteStudent(int id) {
            System.out.println(repo.deleteById(id) ? "Deleted." : "Student not found.");
        }

        void updateStudent(int id, String name, String dept, Double marks) {
            Optional<Student> opt = repo.findById(id);
            if (!opt.isPresent()) {
                System.out.println("Student not found.");
                return;
            }
            Student s = opt.get();

            if (!name.trim().isEmpty()) s.name = name;
            if (!dept.trim().isEmpty()) s.department = dept;
            if (marks != null && marks >= 0 && marks <= 100) s.marks = marks;

            System.out.println("Updated: " + s);
        }

        void sortedStudents() {
            List<Student> sorted = repo.findSortedByName();
            if (sorted.isEmpty()) {
                System.out.println("No students.");
                return;
            }
            sorted.forEach(System.out::println);
        }

        void totalCount() {
            System.out.println("Total Students: " + repo.count());
        }
    }

    // ======================= MAIN MENU =======================
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        StudentService service = new StudentService();
        int ch;

        while (true) {
            System.out.println("\n===== Student Management System =====");
            System.out.println("1. Add Student");
            System.out.println("2. List Students");
            System.out.println("3. Search Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Update Student");
            System.out.println("6. Sort Students by Name");
            System.out.println("7. Total Student Count");
            System.out.println("8. Exit");
	    System.out.println("Feature: Search Student option updated.");
            System.out.print("Enter choice: ");
            ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1:
                    System.out.print("ID: ");
                    int id = Integer.parseInt(sc.nextLine());
                    System.out.print("Name: ");
                    String name = sc.nextLine();
                    System.out.print("Department: ");
                    String dept = sc.nextLine();
                    System.out.print("Marks: ");
                    double marks = Double.parseDouble(sc.nextLine());
                    service.addStudent(id, name, dept, marks);
                    break;

                case 2:
                    service.listStudents();
                    break;

                case 3:
                    System.out.print("Enter ID: ");
                    service.searchStudent(Integer.parseInt(sc.nextLine()));
                    break;

                case 4:
                    System.out.print("Enter ID: ");
                    service.deleteStudent(Integer.parseInt(sc.nextLine()));
                    break;

                case 5:
                    System.out.print("Enter ID: ");
                    int uid = Integer.parseInt(sc.nextLine());
                    System.out.print("New Name (blank to skip): ");
                    String n = sc.nextLine();
                    System.out.print("New Dept (blank to skip): ");
                    String d = sc.nextLine();
                    System.out.print("New Marks (-1 to skip): ");
                    String mk = sc.nextLine();
                    Double updatedMarks = mk.equals("-1") || mk.trim().isEmpty() ? null : Double.parseDouble(mk);
                    service.updateStudent(uid, n, d, updatedMarks);
                    break;

                case 6:
                    service.sortedStudents();
                    break;

                case 7:
                    service.totalCount();
                    break;

                case 8:
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
