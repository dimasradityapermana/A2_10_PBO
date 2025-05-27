package Auth;

import User.Pelanggan;
import java.io.*;
import java.util.Scanner;

public class Register {
    private static final String ADMIN_FILE_PATH = "D:\\Latihan VSCode\\Java Semester 4\\PA PBO LunarBakeshop\\src\\Auth\\Admin.csv";
    private static final String PEGAWAI_FILE_PATH = "D:\\Latihan VSCode\\Java Semester 4\\PA PBO LunarBakeshop\\src\\Auth\\Pegawai.csv";
    private static final String PELANGGAN_FILE_PATH = "D:\\Latihan VSCode\\Java Semester 4\\PA PBO LunarBakeshop\\src\\Auth\\Pelanggan.csv";

    public static void registerUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nama: ");
        String nama = scanner.nextLine();

        if (nama.isEmpty() || !nama.matches("[a-zA-Z\\s]+")) {
            System.out.println("Nama tidak boleh kosong, mengandung angka, atau simbol. Silakan coba lagi.");
            return;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine();

        if (!email.matches("[a-zA-Z0-9@.]+") || !email.contains("@") || email.chars().filter(ch -> ch == '@').count() != 1) {
            System.out.println("Email harus mengandung simbol '@', boleh mengandung simbol '.', dan hanya boleh memiliki satu '@'. Silakan coba lagi.");
            return;
        }

        if (isEmailExists(email)) {
            System.out.println("Email yang Anda masukkan sudah terdaftar di sistem. Harap coba dengan email lain.");
            return;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Alamat: ");
        String alamat = scanner.nextLine();

        int id = generateId();

        try (PrintWriter pw = new PrintWriter(new FileWriter(PELANGGAN_FILE_PATH, true))) {
            pw.println(id + "," + nama + "," + email + "," + password + "," + alamat);
            System.out.println("Registrasi berhasil sebagai pelanggan!");
        } catch (IOException e) {
            System.out.println("Gagal menulis ke file.");
            e.printStackTrace();
        }

        Pelanggan pelanggan = new Pelanggan(id, nama, email, password, alamat);
    }

    private static boolean isEmailExists(String email) {
        return checkEmailInFile(ADMIN_FILE_PATH, 2, email) ||
                checkEmailInFile(PEGAWAI_FILE_PATH, 2, email) ||
                checkEmailInFile(PELANGGAN_FILE_PATH, 2, email);
    }

    private static boolean checkEmailInFile(String filePath, int emailIndex, String email) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > emailIndex && data[emailIndex].equals(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file: " + filePath);
            e.printStackTrace();
        }
        return false;
    }

    private static int generateId() {
        return (int) (System.currentTimeMillis() % 100000);
    }
}