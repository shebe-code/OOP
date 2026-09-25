"""Example usage for the OOP project."""

from oop_project.models import BankAccount, Course, Student


def main() -> None:
    alice = Student("Alice", 20, "S-1001")
    math = Course("Algebra", 3, "Dr. Patel")
    biology = Course("Biology", 4, "Dr. Gomez")

    alice.enroll(math)
    alice.enroll(biology)

    account = BankAccount("Alice", 500.0)
    account.deposit(75.5)
    account.withdraw(30.0)

    print(alice.greet())
    print(alice.study_summary())
    print(f"Courses: {', '.join(alice.list_courses())}")
    print(account.statement())


if __name__ == "__main__":
    main()
