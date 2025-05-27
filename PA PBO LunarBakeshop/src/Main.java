import Auth.Login;
import Auth.Register;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║             LunarBakeshop            ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Register                         ║");
            System.out.println("║  2. Login                            ║");
            System.out.println("║  3. Keluar                           ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print ("Pilih menu (1-3): ");

            String inputPilihan = scanner.nextLine();
            if (inputPilihan.contains(" ")) {
                System.out.println("Pilihan tidak boleh mengandung spasi!\n");
                continue;
            }

            int pilihan = -1;
            try {
                pilihan = Integer.parseInt(inputPilihan);
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka!\n");
                continue;
            }

            switch (pilihan) {
                case 1:
                    Register.registerUser();
                    break;
                case 2:
                    Login.loginUser();
                    break;
                case 3:
                    running = false;
                    System.out.println("Terima kasih telah menggunakan Aplikasi LunarBakeshop!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }
}