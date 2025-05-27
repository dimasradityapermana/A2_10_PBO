package User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Pegawai extends User {
    private Scanner scanner = new Scanner(System.in);
    private static final String ORDER_FILE_PATH = "D:\\Latihan VSCode\\Java Semester 4\\PA PBO LunarBakeshop\\src\\Pesanan\\HistoriPesanan.csv";

    public Pegawai(int idUser, String nama, String email, String password) {
        super(idUser, nama, email, password);
    }

    @Override
    public String toString() {
        return "Pegawai{" +
                "idUser=" + idUser +
                ", nama='" + nama + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void showMenu() {
        boolean running = true;
        while (running) {
            try {
                System.out.println("\n╔════════════════════════════════════════════╗");
                System.out.println("║                 Menu Pegawai               ║");
                System.out.println("╠════════════════════════════════════════════╣");
                System.out.println("║  1. Lihat & Ubah Status Pesanan            ║");
                System.out.println("║  2. Logout                                 ║");
                System.out.println("╚════════════════════════════════════════════╝");
                System.out.print("Pilih (1-2): ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        lihatDanUbahStatus();
                        break;
                    case 2:
                        System.out.println("Logout berhasil.");
                        running = false;
                        break;
                    default:
                        System.out.println("Pilihan tidak valid.");
                }
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan input. Silakan coba lagi.");
                scanner.nextLine();
            }
        }
    }

    private void lihatDanUbahStatus() {
        try {
            List<String[]> orders = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(ORDER_FILE_PATH));

            String header = br.readLine();
            if (header == null) {
                System.out.println("File kosong.");
                br.close();
                return;
            }

            String[] headerColumns = header.split(",");
            int statusIndex = -1;
            int idPemesananIndex = -1;
            int namaProdukIndex = -1;
            int timestampIndex = -1;

            for (int i = 0; i < headerColumns.length; i++) {
                String column = headerColumns[i].trim().toLowerCase();
                if (column.equals("idpemesanan")) idPemesananIndex = i;
                else if (column.equals("namaproduk")) namaProdukIndex = i;
                else if (column.equals("timestamp")) timestampIndex = i;
                else if (column.equals("status")) statusIndex = i;
            }

            boolean statusAdded = false;
            if (statusIndex == -1) {
                header += ",status";
                statusIndex = headerColumns.length;
                statusAdded = true;
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] splitLine = line.split(",", -1);
                if (statusAdded) {
                    splitLine = expandArray(splitLine, statusIndex + 1);
                }
                orders.add(splitLine);
            }
            br.close();

            System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                                     Daftar Pesanan                                    ║");
            System.out.println("╠══════════════════╦════════════════════════╦══════════════════════════╦════════════════╣");
            System.out.printf ("║ %-16s ║ %-22s ║ %-24s ║ %-14s ║\n",
                    "ID Pemesanan", "Nama Produk", "Tanggal & Waktu", "Status");
            System.out.println("╠══════════════════╬════════════════════════╬══════════════════════════╬════════════════╣");

            String lastIdPemesanan = "";
            for (String[] order : orders) {
                String idPemesanan = order.length > idPemesananIndex ? order[idPemesananIndex] : "-";
                String namaProduk = order.length > namaProdukIndex ? order[namaProdukIndex] : "-";
                String tanggal = order.length > timestampIndex ? formatTanggal(order[timestampIndex]) : "-";
                String status = (order.length > statusIndex && !order[statusIndex].isEmpty())
                        ? order[statusIndex] : "Belum Diatur";

                String displayId = idPemesanan.equals(lastIdPemesanan) ? "" : idPemesanan;
                lastIdPemesanan = idPemesanan;

                System.out.printf("║ %-16s ║ %-22s ║ %-24s ║ %-14s ║\n",
                        displayId, namaProduk, tanggal, status);
            }
            System.out.println("╚══════════════════╩════════════════════════╩══════════════════════════╩════════════════╝");



            System.out.print("\nMasukkan ID Pemesanan yang ingin diubah statusnya (atau 0 untuk batal): ");
            String idPemesananInput = scanner.nextLine();

            List<String> idTersedia = new ArrayList<>();
            for (String[] order : orders) {
                if (order.length > idPemesananIndex) {
                    String id = order[idPemesananIndex];
                    if (!idTersedia.contains(id)) {
                        idTersedia.add(id);
                    }
                }
            }

            if (!idPemesananInput.equals("0")) {
                if (!idTersedia.contains(idPemesananInput)) {
                    System.out.println("ID Pemesanan tidak ditemukan. Proses perubahan status dibatalkan.");
                    return;
                }

                System.out.print("Status baru (Pending/Packaging/Done): ");
                String newStatus = scanner.nextLine();

                if (
                        !newStatus.equalsIgnoreCase("Pending") &&
                                !newStatus.equalsIgnoreCase("Packaging") &&
                                !newStatus.equalsIgnoreCase("Done")
                ) {
                    System.out.println("Status tidak valid. Proses perubahan status dibatalkan.");
                    return;
                }

                boolean found = false;

                for (int i = 0; i < orders.size(); i++) {
                    String[] order = orders.get(i);
                    if (order.length > idPemesananIndex && order[idPemesananIndex].equals(idPemesananInput)) {
                        if (order.length <= statusIndex) {
                            order = expandArray(order, statusIndex + 1);
                            orders.set(i, order);
                        }
                        order[statusIndex] = newStatus;
                        found = true;
                    }
                }

                if (found) {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(ORDER_FILE_PATH));
                    bw.write(header);
                    bw.newLine();
                    for (String[] order : orders) {
                        bw.write(String.join(",", order));
                        bw.newLine();
                    }
                    bw.close();
                    System.out.println("Status berhasil diubah!");
                }
            }
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
        }
    }

    private String[] expandArray(String[] original, int newSize) {
        String[] expanded = new String[newSize];
        for (int i = 0; i < newSize; i++) {
            expanded[i] = (i < original.length) ? original[i] : "";
        }
        return expanded;
    }

    private String formatTanggal(String timestamp) {
        String[] parts = timestamp.split("/");
        if (parts.length >= 4) {
            String tanggal = parts[0] + "-" + parts[1] + "-" + parts[2];
            String jam = parts[3].replace('.', ':');
            return tanggal + " " + jam;
        }
        return timestamp;
    }
}