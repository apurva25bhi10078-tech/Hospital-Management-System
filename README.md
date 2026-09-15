# Hospital Management System

A simple console-based Hospital Management System written in core Java. It lets you register patients and doctors, book appointments, track admission status, and search records — all through a menu-driven interface, no database or GUI required.

## Features

- Add and view patients (name, age, gender, reason for visit)
- Add and view doctors (name, specialization, availability)
- Book appointments between a patient and an available doctor
- Automatic doctor availability tracking (prevents double-booking)
- Admit / discharge toggle for patients
- Search for a patient by ID
- Basic input validation so bad input doesn't crash the program

## Tech Used

- Java (core Java only — no external libraries)
- `ArrayList` for in-memory storage of patients, doctors, and appointments
- `Scanner` for console input

## Getting Started

### Prerequisites

- JDK 8 or above installed
- A terminal / command prompt

### Run it

```bash
javac HospitalManagementSystem.java
java HospitalManagementSystem
```

You'll see a numbered menu — enter the number for whatever you want to do (add a patient, book an appointment, view records, etc.).

## Project Structure

Everything lives in one file for simplicity:

```
HospitalManagementSystem.java
├── Patient class        - patient details + admission status
├── Doctor class          - doctor details + availability
├── Appointment class     - links a patient to a doctor on a date
└── HospitalManagementSystem class (main) - menu + all the logic
```

## Notes / Limitations

- Data is stored in memory only — nothing is saved once you close the program.
- Once a doctor is marked busy, there's currently no way to free them up again except restarting the app.
- No login system — anyone running the program has full access.

## Possible Improvements

- Save data to a file or database so records persist between runs
- Build a proper GUI with JavaFX or Swing
- Add a way to manually mark a doctor available again after an appointment
- Role-based login (admin / doctor / receptionist)
- Billing module

