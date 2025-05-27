package Auth;

import User.Admin;
import User.Pegawai;
import User.Pelanggan;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Login {
    private static final String ADMIN_FILE_PATH = "D:\\Latihan VSCode\\Java Semester 4\\PA PBO LunarBakeshop\\src\\Auth\\Admin.csv";
    private static final String PEGAWAI_FILE_PATH = "D:\\Latihan VSCode\\Java Semester 4\\PA PBO LunarBakeshop\\src\\Auth\\Pegawai.csv";
    private static final String PELANGGAN_FILE_PATH = "D:\\Latihan VSCode\\Java Semester 4\\PA PBO LunarBakeshop\\src\\Auth\\Pelanggan.csv";

    public static String currentUserId = null;
    public static String currentUserName = null;
    public static String currentUserEmail = null;

    public static void loginUser() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            if (email.isEmpty() || password.isEmpty()) {
                System.out.println("Email dan password tidak boleh kosong.");
                return;
            }

            if (checkAdminCredentials(email, password)) {
                System.out.println("Login berhasil sebagai Admin. Selamat datang!");
                Admin admin = new Admin(0, "Admin", email, password, "admin");
                admin.showMenu();
            } else if (checkEmployeeCredentials(email, password)) {
                System.out.println("Login berhasil sebagai Pegawai. Selamat datang!");
                Pegawai pegawai = new Pegawai(0, "Pegawai", email, password);
                pegawai.showMenu();
            } else if (checkUserCredentials(email, password)) {
                System.out.println("Login berhasil sebagai Pelanggan. Selamat datang!");

                try {
                    int userId = Integer.parseInt(currentUserId);
                    Pelanggan pelanggan = new Pelanggan(
                            userId,
                            currentUserName != null ? currentUserName : "Pelanggan",
                            email,
                            password,
                            "Alamat Pelanggan"
                    );

                    pelanggan.showMenu();
                } catch (NumberFormatException e) {
                    System.out.println("Error: ID pengguna tidak valid.");
                    currentUserId = null;
                }
            } else {
                System.out.println("Email atau password salah.");
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan saat login: " + e.getMessage());
        }
    }

    private static boolean checkAdminCredentials(String email, String password) {
        File file = new File(ADMIN_FILE_PATH);
        if (!file.exists()) {
            System.out.println("File data admin tidak ditemukan: " + ADMIN_FILE_PATH);
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 4 &&
                        data[2].trim().equals(email) &&
                        data[3].trim().equals(password)) {
                    currentUserEmail = email;
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file admin: " + e.getMessage());
        }
        return false;
    }

    private static boolean checkEmployeeCredentials(String email, String password) {
        File file = new File(PEGAWAI_FILE_PATH);
        if (!file.exists()) {
            System.out.println("File data pegawai tidak ditemukan: " + PEGAWAI_FILE_PATH);
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 4 &&
                        data[2].trim().equals(email) &&
                        data[3].trim().equals(password)) {
                    currentUserId = data[0].trim();
                    currentUserName = data[1].trim();
                    currentUserEmail = email;
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file pegawai: " + e.getMessage());
        }
        return false;
    }

    private static boolean checkUserCredentials(String email, String password) {
        File file = new File(PELANGGAN_FILE_PATH);
        if (!file.exists()) {
            System.out.println("File data pelanggan tidak ditemukan: " + PELANGGAN_FILE_PATH);
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 4 &&
                        data[2].trim().equals(email) &&
                        data[3].trim().equals(password)) {
                    currentUserId = data[0].trim();
                    currentUserName = data.length > 1 ? data[1].trim() : "Pelanggan";
                    currentUserEmail = email;
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file pengguna: " + e.getMessage());
        }
        return false;
    }

    public static void logout() {
        currentUserId = null;
        currentUserName = null;
        currentUserEmail = null;
        System.out.println("Logout berhasil.");
    }

    public static boolean isLoggedIn() {
        return currentUserId != null;
    }

    public static String getCurrentUserInfo() {
        if (isLoggedIn()) {
            return "ID: " + currentUserId + ", Nama: " + currentUserName + ", Email: " + currentUserEmail;
        }
        return "Tidak ada user yang login";
    }

    private static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    public static void ensureDirectoriesExist() {
        File authDir = new File("src/Auth");
        if (!authDir.exists()) {
            authDir.mkdirs();
        }

        File pesananDir = new File("src/Pesanan");
        if (!pesananDir.exists()) {
            pesananDir.mkdirs();
        }
    }
}
