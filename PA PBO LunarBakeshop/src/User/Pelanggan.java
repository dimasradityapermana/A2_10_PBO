package User;

import Pesanan.DetailPesanan;
import Pesanan.PesananLunar;
import Pesanan.PesananManager;
import Produk.ProdukLunar;
import java.io.*;
import java.util.*;

public class Pelanggan extends User {
    private Scanner scanner = new Scanner(System.in);
    private String alamat;
    private PesananManager pesananManager;
    private ProdukLunar produkManager;

    public Pelanggan(int idUser, String nama, String email, String password, String alamat) {
        super(idUser, nama, email, password);
        this.alamat = alamat;
        this.pesananManager = new PesananManager();
        this.produkManager = new ProdukLunar();
    }

    public void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔════════════════════════════════════════════╗");
            System.out.println("║                Menu Pelanggan              ║");
            System.out.println("╠════════════════════════════════════════════╣");
            System.out.println("║  1. Pesan Produk                           ║");
            System.out.println("║  2. Lihat Pesanan                          ║");
            System.out.println("║  3. Ubah Password Akun                     ║");
            System.out.println("║  4. Logout                                 ║");
            System.out.println("╚════════════════════════════════════════════╝");
            System.out.print("Pilih (1-4): ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        pesanProduk();
                        break;
                    case 2:
                        lihatPesanan();
                        break;
                    case 3:
                        ubahPassword();
                        break;
                    case 4:
                        System.out.println("Logout berhasil.");
                        running = false;
                        break;
                    default:
                        System.out.println("Pilihan tidak valid.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Input tidak valid. Masukkan angka.");
                scanner.nextLine();
            }
        }
    }

    private void pesanProduk() {
        if (Auth.Login.currentUserId == null) {
            System.out.println("Anda belum login. Silakan login terlebih dahulu.");
            return;
        }

        List<ProdukLunar> daftarProduk = ProdukLunar.getAllProduk();

        if (daftarProduk.isEmpty()) {
            System.out.println("Tidak ada produk tersedia saat ini.");
            return;
        }

        System.out.println("\n╔═════════════════════════════════════════════════════════════╗");
        System.out.println("║                     Daftar Produk Tersedia                  ║");
        System.out.println("╠═══════╦══════════════════════════╦════════════╦═════════════╣");
        System.out.printf ("║ %-5s ║ %-24s ║ %-10s ║ %-11s ║\n", "ID", "Nama Produk", "Harga", "Stok");
        System.out.println("╠═══════╬══════════════════════════╬════════════╬═════════════╣");

        for (ProdukLunar p : daftarProduk) {
            System.out.printf("║ %-5s ║ %-24s ║ Rp%-8.0f ║ %-11d ║\n",
                    p.getIdProduk(),
                    p.getNamaProduk(),
                    p.getHarga(),
                    p.getStok());
        }

        System.out.println("╚═══════╩══════════════════════════╩════════════╩═════════════╝");


        List<DetailPesanan> keranjang = new ArrayList<>();

        while (true) {
            System.out.print("\nMasukkan ID produk yang ingin dipesan (atau 'selesai' untuk checkout): ");
            String id = scanner.nextLine();

            if (id.equalsIgnoreCase("selesai")) break;

            ProdukLunar produkDipilih = null;
            for (ProdukLunar p : daftarProduk) {
                if (p.getIdProduk().equalsIgnoreCase(id)) {
                    produkDipilih = p;
                    break;
                }
            }

            if (produkDipilih == null) {
                System.out.println("Produk dengan ID tersebut tidak ditemukan.");
                continue;
            }

            System.out.print("Jumlah yang ingin dibeli: ");

            try {
                int jumlah = scanner.nextInt();
                scanner.nextLine();

                if (jumlah <= 0) {
                    System.out.println("Jumlah pembelian harus lebih dari 0.");
                    continue;
                }

                if (jumlah > produkDipilih.getStok()) {
                    System.out.println("Stok tidak mencukupi.");
                    continue;
                }

                produkDipilih.setStok(produkDipilih.getStok() - jumlah);
                keranjang.add(new DetailPesanan(produkDipilih, jumlah));
            } catch (InputMismatchException e) {
                System.out.println("Input tidak valid. Masukkan angka.");
                scanner.nextLine();
                continue;
            }
        }

        if (keranjang.isEmpty()) {
            System.out.println("Tidak ada produk yang dipesan.");
            return;
        }

        double totalHarga = 0;
        for (DetailPesanan item : keranjang) {
            totalHarga += item.getProduk().getHarga() * item.getJumlah();
        }

        System.out.println("\nTotal harga pembelian Anda: " + (totalHarga));

        System.out.print("Pilih metode pembayaran (QRIS / Cash): ");
        String metodePembayaran = scanner.nextLine();

        if (!metodePembayaran.equalsIgnoreCase("QRIS") && !metodePembayaran.equalsIgnoreCase("Cash")) {
            System.out.println("Metode pembayaran tidak valid. Proses pembelian dibatalkan.");
            return;
        }

        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy/HH.mm.ss"));

        String idPesanan = Auth.Login.currentUserId + "-" + (int) (Math.random() * 1000);

        PesananLunar pesanan = new PesananLunar(keranjang, metodePembayaran);
        pesanan.setIdPesanan(idPesanan);
        String statusPesanan = "Pending";

        pesananManager.simpanPesanan(Auth.Login.currentUserId, pesanan, timestamp, statusPesanan);
        ProdukLunar.simpanSemuaProduk(daftarProduk);

        String filePath = "src/Pesanan/HistoriPesanan.csv";
        simpanKeHistori(keranjang, idPesanan, metodePembayaran, timestamp, filePath);

        System.out.println("Pesanan berhasil dibuat dengan ID: " + idPesanan + " pada " + timestamp + "!");
    }

    private void simpanKeHistori(List<DetailPesanan> keranjang, String idPesanan, String metodePembayaran, String timestamp, String filePath) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();

            Set<String> existingKeys = new HashSet<>();

            if (file.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] data = line.split(",");
                        if (data.length >= 5) {
                            String key = data[0] + "|" + data[1] + "|" + data[2] + "|" + data[4];
                            existingKeys.add(key.toLowerCase());
                        }
                    }
                }
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                for (DetailPesanan item : keranjang) {
                    String statusPesanan = "Pending";

                    String checkKey = (Auth.Login.currentUserId + "|" +
                            idPesanan + "|" +
                            item.getProduk().getIdProduk() + "|" +
                            item.getJumlah()).toLowerCase();

                    if (!existingKeys.contains(checkKey)) {
                        String newData = Auth.Login.currentUserId + "," +
                                idPesanan + "," +
                                item.getProduk().getIdProduk() + "," +
                                item.getProduk().getNamaProduk() + "," +
                                item.getJumlah() + "," +
                                metodePembayaran + "," +
                                timestamp + "," +
                                statusPesanan;

                        bw.write(newData);
                        bw.newLine();
                        existingKeys.add(checkKey);
                    }
                }
            }
            System.out.println("Pesanan berhasil disimpan ke dalam histori.");
        } catch (IOException e) {
            System.out.println("Gagal menyimpan pesanan ke dalam histori: " + e.getMessage());
        }
    }

    private void lihatPesanan() {
        String userId = Auth.Login.currentUserId;
        String filePath = "src/Pesanan/HistoriPesanan.csv";

        if (userId == null) {
            System.out.println("Anda belum login. Silakan login terlebih dahulu.");
            return;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Tidak ada riwayat pesanan.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean hasOrders = false;

            System.out.println("\n╔════════════════╦══════════════════════════╦══════════════════════╦════════════╦══════════════════════╦═══════════════╗");
            System.out.printf ("║ %-14s ║ %-24s ║ %-20s ║ %-10s ║ %-20s ║ %-13s ║\n",
                    "ID Pesanan", "Tanggal", "Nama Produk", "Jumlah", "Metode Pembayaran", "Status");
            System.out.println("╠════════════════╬══════════════════════════╬══════════════════════╬════════════╬══════════════════════╬═══════════════╣");

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 7 && data[0].equals(userId)) {
                    hasOrders = true;

                    String idPesanan = data[1];
                    String tanggal = data[6];
                    String produk = data[3];
                    String jumlah = data[4];
                    String metodePembayaran = data[5];
                    String status = data[7];

                    System.out.printf("║ %-14s ║ %-24s ║ %-20s ║ %-10s ║ %-20s ║ %-13s ║\n",
                            idPesanan, tanggal, produk, jumlah, metodePembayaran, status);
                }
            }

            System.out.println("╚════════════════╩══════════════════════════╩══════════════════════╩════════════╩══════════════════════╩═══════════════╝");


            if (!hasOrders) {
                System.out.println("Tidak ada riwayat pesanan.");
            }
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat membaca file riwayat pesanan: " + e.getMessage());
        }
    }

    private void ubahPassword() {
        if (Auth.Login.currentUserId == null) {
            System.out.println("Anda belum login. Silakan login terlebih dahulu.");
            return;
        }

        String filePath = "src/Auth/Pelanggan.csv";
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File data pengguna tidak ditemukan.");
            return;
        }

        List<String[]> userData = new ArrayList<>();
        boolean passwordUpdated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                userData.add(data);
            }
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat membaca file data pengguna: " + e.getMessage());
            return;
        }

        for (String[] user : userData) {
            if (user.length > 3 && user[0].equals(Auth.Login.currentUserId)) {
                System.out.print("Masukkan password lama: ");
                String oldPassword = scanner.nextLine();

                if (!user[3].equals(oldPassword)) {
                    System.out.println("Password lama tidak sesuai. Password tidak dapat diubah.");
                    return;
                }

                System.out.print("Masukkan password baru: ");
                String newPassword = scanner.nextLine();

                if (newPassword.trim().isEmpty()) {
                    System.out.println("Password baru tidak boleh kosong.");
                    return;
                }

                user[3] = newPassword;
                passwordUpdated = true;
                break;
            }
        }

        if (passwordUpdated) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
                for (String[] user : userData) {
                    bw.write(String.join(",", user));
                    bw.newLine();
                }
                System.out.println("Password berhasil diubah.");
            } catch (IOException e) {
                System.out.println("Terjadi kesalahan saat menyimpan data pengguna: " + e.getMessage());
            }
        } else {
            System.out.println("Pengguna tidak ditemukan.");
        }
    }
}