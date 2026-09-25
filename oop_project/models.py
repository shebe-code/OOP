import unittest

from oop_project.models import BankAccount, Course, Person, Student


class PersonTests(unittest.TestCase):
    def test_person_greet(self):
        person = Person("Bob", 30)
        self.assertEqual(person.greet(), "Hello, my name is Bob and I am 30 years old.")

    def test_invalid_name_raises_error(self):
        with self.assertRaises(ValueError):
            Person("   ", 25)


class CourseTests(unittest.TestCase):
    def test_course_str(self):
        course = Course("History", 2, "Prof. Lee")
        self.assertIn("History", str(course))
        self.assertIn("Prof. Lee", str(course))


class StudentTests(unittest.TestCase):
    def test_student_enroll_and_list_courses(self):
        student = Student("Carol", 19, "S-101")
        course = Course("Physics", 4, "Dr. Kline")

        student.enroll(course)

        self.assertEqual(student.list_courses(), ["Physics"])
        self.assertIn("Carol", student.study_summary())

    def test_duplicate_enrollment_raises_error(self):
        student = Student("Dana", 21, "S-202")
        course = Course("Chemistry", 3, "Dr. Ross")

        student.enroll(course)
        with self.assertRaises(ValueError):
            student.enroll(course)


class BankAccountTests(unittest.TestCase):
    def test_deposit_and_withdraw(self):
        account = BankAccount("Eve", 100.0)
        self.assertEqual(account.deposit(50), 150.0)
        self.assertEqual(account.withdraw(25), 125.0)

    def test_insufficient_funds_raises_error(self):
        account = BankAccount("Frank", 10.0)
        with self.assertRaises(ValueError):
            account.withdraw(20.0)


if __name__ == "__main__":
    unittest.main()
