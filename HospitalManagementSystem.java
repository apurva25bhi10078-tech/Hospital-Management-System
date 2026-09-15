import java.util.*;

// Simple console-based Hospital Management System
// Handles patients, doctors and appointments in memory (no database, keeps it simple)

class Patient {
    int id;
    String name;
    int age;
    String gender;
    String disease;
    boolean admitted;

    Patient(int id, String name, int age, String gender, String disease) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.disease = disease;
        this.admitted = false;
    }

    void display() {
        System.out.println("ID: " + id + " | Name: " + name + " | Age: " + age +
                " | Gender: " + gender + " | Disease: " + disease +
                " | Status: " + (admitted ? "Admitted" : "Not Admitted"));
    }
}

class Doctor {
    int id;
    String name;
    String specialization;
    boolean available;

    Doctor(int id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.available = true;
    }

    void display() {
        System.out.println("ID: " + id + " | Dr. " + name + " | Specialization: " + specialization +
                " | " + (available ? "Available" : "Busy"));
    }
}

class Appointment {
    int appointmentId;
    int patientId;
    int doctorId;
    String date;

    Appointment(int appointmentId, int patientId, int doctorId, String date) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
    }
}

public class HospitalManagementSystem {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Patient> patients = new ArrayList<>();
    static ArrayList<Doctor> doctors = new ArrayList<>();
    static ArrayList<Appointment> appointments = new ArrayList<>();

    // counters just keep going up, no reuse of old ids even after deletion
    static int patientCounter = 1;
    static int doctorCounter = 1;
    static int appointmentCounter = 1;

    public static void main(String[] args) {

        // add a couple of doctors by default so the system isn't empty when you run it
        doctors.add(new Doctor(doctorCounter++, "Sharma", "Cardiology"));
        doctors.add(new Doctor(doctorCounter++, "Mehta", "Orthopedics"));

        boolean running = true;

        while (running) {
            System.out.println("\n===== HOSPITAL MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Patient");
            System.out.println("2. Add Doctor");
            System.out.println("3. View All Patients");
            System.out.println("4. View All Doctors");
            System.out.println("5. Book Appointment");
            System.out.println("6. View Appointments");
            System.out.println("7. Admit / Discharge Patient");
            System.out.println("8. Search Patient by ID");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("That's not a valid number, try again.");
                continue;
            }

            switch (choice) {
                case 1:
                    addPatient();
                    break;
                case 2:
                    addDoctor();
                    break;
                case 3:
                    viewPatients();
                    break;
                case 4:
                    viewDoctors();
                    break;
                case 5:
                    bookAppointment();
                    break;
                case 6:
                    viewAppointments();
                    break;
                case 7:
                    toggleAdmission();
                    break;
                case 8:
                    searchPatient();
                    break;
                case 9:
                    running = false;
                    System.out.println("Closing the system. Bye!");
                    break;
                default:
                    System.out.println("Please choose a valid option (1-9).");
            }
        }

        sc.close();
    }

    static void addPatient() {
        System.out.print("Enter patient name: ");
        String name = sc.nextLine();

        int age = readInt("Enter age: ");

        System.out.print("Enter gender: ");
        String gender = sc.nextLine();

        System.out.print("Enter disease/reason for visit: ");
        String disease = sc.nextLine();

        Patient p = new Patient(patientCounter++, name, age, gender, disease);
        patients.add(p);

        System.out.println("Patient added successfully. Patient ID = " + p.id);
    }

    static void addDoctor() {
        System.out.print("Enter doctor name: ");
        String name = sc.nextLine();

        System.out.print("Enter specialization: ");
        String spec = sc.nextLine();

        Doctor d = new Doctor(doctorCounter++, name, spec);
        doctors.add(d);

        System.out.println("Doctor added successfully. Doctor ID = " + d.id);
    }

    static void viewPatients() {
        if (patients.isEmpty()) {
            System.out.println("No patients registered yet.");
            return;
        }
        System.out.println("\n--- Patient List ---");
        for (Patient p : patients) {
            p.display();
        }
    }

    static void viewDoctors() {
        if (doctors.isEmpty()) {
            System.out.println("No doctors added yet.");
            return;
        }
        System.out.println("\n--- Doctor List ---");
        for (Doctor d : doctors) {
            d.display();
        }
    }

    static void bookAppointment() {
        if (patients.isEmpty() || doctors.isEmpty()) {
            System.out.println("Need at least one patient and one doctor before booking.");
            return;
        }

        int patientId = readInt("Enter patient ID: ");
        Patient p = findPatientById(patientId);
        if (p == null) {
            System.out.println("No patient found with that ID.");
            return;
        }

        viewDoctors();
        int doctorId = readInt("Enter doctor ID to book: ");
        Doctor d = findDoctorById(doctorId);
        if (d == null) {
            System.out.println("No doctor found with that ID.");
            return;
        }

        if (!d.available) {
            System.out.println("Sorry, Dr. " + d.name + " is currently busy with another patient.");
            return;
        }

        System.out.print("Enter appointment date (dd-mm-yyyy): ");
        String date = sc.nextLine();

        Appointment a = new Appointment(appointmentCounter++, patientId, doctorId, date);
        appointments.add(a);
        d.available = false; // mark doctor busy for now, could add a "free doctor" option later

        System.out.println("Appointment booked! Appointment ID = " + a.appointmentId +
                " with Dr. " + d.name + " on " + date);
    }

    static void viewAppointments() {
        if (appointments.isEmpty()) {
            System.out.println("No appointments booked yet.");
            return;
        }
        System.out.println("\n--- Appointments ---");
        for (Appointment a : appointments) {
            Patient p = findPatientById(a.patientId);
            Doctor d = findDoctorById(a.doctorId);
            String patientName = (p != null) ? p.name : "Unknown";
            String doctorName = (d != null) ? d.name : "Unknown";

            System.out.println("Appt #" + a.appointmentId + " -> Patient: " + patientName +
                    " with Dr. " + doctorName + " on " + a.date);
        }
    }

    static void toggleAdmission() {
        int id = readInt("Enter patient ID: ");
        Patient p = findPatientById(id);
        if (p == null) {
            System.out.println("No patient found with that ID.");
            return;
        }

        p.admitted = !p.admitted;
        System.out.println(p.name + " is now " + (p.admitted ? "ADMITTED" : "DISCHARGED"));
    }

    static void searchPatient() {
        int id = readInt("Enter patient ID to search: ");
        Patient p = findPatientById(id);
        if (p == null) {
            System.out.println("No patient found with ID " + id);
        } else {
            p.display();
        }
    }

    // ---- small helper methods below ----

    static Patient findPatientById(int id) {
        for (Patient p : patients) {
            if (p.id == id) return p;
        }
        return null;
    }

    static Doctor findDoctorById(int id) {
        for (Doctor d : doctors) {
            if (d.id == id) return d;
        }
        return null;
    }

    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
