"""Core domain models for the OOP demo project."""

from __future__ import annotations


class Person:
    """Represents a person with a name and age."""

    def __init__(self, name: str, age: int) -> None:
        if not name.strip():
            raise ValueError("Name cannot be empty.")
        if age < 0:
            raise ValueError("Age cannot be negative.")

        self.name = name.strip()
        self.age = age

    def greet(self) -> str:
        return f"Hello, my name is {self.name} and I am {self.age} years old."


class Course:
    """Represents a course a student can enroll in."""

    def __init__(self, title: str, credits: int, teacher: str) -> None:
        if not title.strip():
            raise ValueError("Course title cannot be empty.")
        if credits <= 0:
            raise ValueError("Credits must be positive.")
        if not teacher.strip():
            raise ValueError("Teacher cannot be empty.")

        self.title = title.strip()
        self.credits = credits
        self.teacher = teacher.strip()

    def __str__(self) -> str:
        return f"{self.title} ({self.credits} credits) taught by {self.teacher}"


class Student(Person):
    """Represents a student who can enroll in courses."""

    def __init__(self, name: str, age: int, student_id: str) -> None:
        super().__init__(name, age)
        if not student_id.strip():
            raise ValueError("Student ID cannot be empty.")

        self.student_id = student_id.strip()
        self.courses: list[Course] = []

    def enroll(self, course: Course) -> None:
        if course in self.courses:
            raise ValueError(f"Student {self.name} is already enrolled in {course.title}.")
        self.courses.append(course)

    def list_courses(self) -> list[str]:
        return [course.title for course in self.courses]

    def study_summary(self) -> str:
        return f"{self.name} ({self.student_id}) is enrolled in {len(self.courses)} course(s)."


class BankAccount:
    """Represents a simple bank account with deposits and withdrawals."""

    def __init__(self, account_holder: str, balance: float = 0.0) -> None:
        if not account_holder.strip():
            raise ValueError("Account holder name cannot be empty.")
        if balance < 0:
            raise ValueError("Balance cannot be negative.")

        self.account_holder = account_holder.strip()
        self.balance = balance

    def deposit(self, amount: float) -> float:
        if amount <= 0:
            raise ValueError("Deposit amount must be positive.")
        self.balance += amount
        return self.balance

    def withdraw(self, amount: float) -> float:
        if amount <= 0:
            raise ValueError("Withdrawal amount must be positive.")
        if amount > self.balance:
            raise ValueError("Insufficient funds.")
        self.balance -= amount
        return self.balance

    def statement(self) -> str:
        return f"{self.account_holder}'s balance: ${self.balance:.2f}"
