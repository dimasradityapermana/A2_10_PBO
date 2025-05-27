package User;

import Produk.ProdukLunar;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;

public final class Admin extends User {
    private String username;
    private final Scanner scanner = new Scanner(System.in);

    public Admin(int idUser, String nama, String email, String password, String username) {
        super(idUser, nama, email, password);
        this.username = username;
    }

    public void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔════════════════════════════════════════════╗");
            System.out.println("║                  Menu Admin                ║");
            System.out.println("╠════════════════════════════════════════════╣");
            System.out.println("║  1. Tambah Produk                          ║");
            System.out.println("║  2. Lihat Produk                           ║");
            System.out.println("║  3. Hapus Produk                           ║");
            System.out.println("║  4. Update Produk                          ║");
            System.out.println("║  5. Lihat Transaksi                        ║");
            System.out.println("║  6. Kelola Pegawai                         ║");
            System.out.println("║  7. Laporan Penjualan                      ║");
            System.out.println("║  8. Logout                                 ║");
            System.out.println("╚════════════════════════════════════════════╝");
            System.out.print("Pilih (1-8): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    tambahProduk();
                    break;
                case 2:
                    ProdukLunar.lihatProduk();
                    break;
                case 3:
                    hapusProduk();
                    break;
                case 4:
                    updateProduk();
                    break;
                case 5:
                    lihatTransaksi();
                    break;
                case 6:
                    kelolaPegawai();
                    break;
                case 7:
                    laporanPenjualan();
                    break;
                case 8:
                    System.out.println("Logout berhasil.");
                    running = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private void tambahProduk() {
        ProdukLunar.lihatProduk();
        System.out.println(">> Tambah produk baru");
        String id;
        while (true) {
            System.out.print("ID Produk (hanya angka): ");
            id = scanner.nextLine().trim();
            if (id.matches("\\d+")) {
                break;
            } else {
                System.out.println("ID Produk harus berupa angka. Silakan coba lagi.");
            }
        }

        String filePath = "D:\\Latihan VSCode\\Java Semester 4\\PA PBO LunarBakeshop\\src\\Produk\\Produk.csv";
        boolean idSudahAda = false;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0 && data[0].trim().equalsIgnoreCase(id)) {
                    idSudahAda = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file produk. Pastikan file ada dan dapat diakses.");
            return;
        }

        if (idSudahAda) {
            System.out.println("ID produk sudah digunakan di file Produk.csv. Silakan gunakan ID lain.");
            return;
        }

        System.out.print("Nama Produk: ");
        String nama = scanner.nextLine().trim();
        if (nama.isEmpty()) {
            System.out.println("Nama produk tidak boleh kosong.");
            return;
        }

        double harga = 0;
        while (true) {
            System.out.print("Harga: ");
            String hargaInput = scanner.nextLine().trim();
            try {
                harga = Double.parseDouble(hargaInput);
                if (harga < 0) {
                    System.out.println("Harga tidak boleh negatif.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Input harga harus berupa angka. Silakan coba lagi.");
            }
        }

        int stok = 0;
        while (true) {
            System.out.print("Stok: ");
            String stokInput = scanner.nextLine().trim();
            try {
                stok = Integer.parseInt(stokInput);
                if (stok < 0) {
                    System.out.println("Stok tidak boleh negatif.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Input stok harus berupa angka. Silakan coba lagi.");
            }
        }

        System.out.print("Kategori: ");
        String kategori = scanner.nextLine().trim();
        if (kategori.isEmpty()) {
            System.out.println("Kategori tidak boleh kosong.");
            return;
        }

        try {
            ProdukLunar produkBaru = new ProdukLunar(id, nama, harga, stok, kategori);
            ProdukLunar.tambahProduk(produkBaru);
            System.out.println("Produk berhasil ditambahkan.");
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan saat menambahkan produk: " + e.getMessage());
        }
    }

    private void updateProduk() {
        ProdukLunar.lihatProduk();
        System.out.println(">> Update data produk");
        System.out.print("Masukkan ID produk yang ingin diubah: ");
        String id = scanner.nextLine().trim();

        var produkList = ProdukLunar.getAllProduk();
        if (produkList == null || produkList.isEmpty()) {
            System.out.println("Daftar produk kosong atau tidak dapat diakses.");
            return;
        }

        ProdukLunar produkDitemukan = null;
        for (ProdukLunar produk : produkList) {
            if (produk.getIdProduk().equalsIgnoreCase(id)) {
                produkDitemukan = produk;
                break;
            }
        }

        if (produkDitemukan == null) {
            System.out.println("Produk dengan ID " + id + " tidak ditemukan.");
            return;
        }

        System.out.println("Biarkan kosong jika tidak ingin mengubah atribut.");

        System.out.print("Nama produk baru (" + produkDitemukan.getNamaProduk() + "): ");
        String namaBaru = scanner.nextLine().trim();
        if (!namaBaru.isEmpty()) {
            produkDitemukan.setNamaProduk(namaBaru);
        }

        System.out.print("Harga baru (" + produkDitemukan.getHarga() + "): ");
        String hargaInput = scanner.nextLine().trim();
        if (!hargaInput.isEmpty()) {
            try {
                double hargaBaru = Double.parseDouble(hargaInput);
                if (hargaBaru > 0) {
                    produkDitemukan.setHarga(hargaBaru);
                } else {
                    System.out.println("Harga harus lebih dari 0, nilai tidak diubah.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input harga tidak valid, nilai tidak diubah.");
            }
        }

        System.out.print("Stok baru (" + produkDitemukan.getStok() + "): ");
        String stokInput = scanner.nextLine().trim();
        if (!stokInput.isEmpty()) {
            try {
                int stokBaru = Integer.parseInt(stokInput);
                if (stokBaru >= 0) {
                    produkDitemukan.setStok(stokBaru);
                } else {
                    System.out.println("Stok tidak boleh negatif, nilai tidak diubah.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input stok tidak valid, nilai tidak diubah.");
            }
        }

        System.out.print("Kategori baru (" + produkDitemukan.getKategori() + "): ");
        String kategoriBaru = scanner.nextLine().trim();
        if (!kategoriBaru.isEmpty()) {
            produkDitemukan.setKategori(kategoriBaru);
        }

        try {
            ProdukLunar.simpanSemuaProduk(produkList);
            System.out.println("Produk berhasil diperbarui.");
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan saat memperbarui produk: " + e.getMessage());
        }
    }

    private void hapusProduk() {
        ProdukLunar.lihatProduk();
        System.out.println(">> Hapus produk dari daftar");
        System.out.print("Masukkan ID produk yang ingin dihapus: ");
        String id = scanner.nextLine().trim();

        if (id.isEmpty()) {
            System.out.println("ID produk tidak boleh kosong.");
            return;
        }

        try {
            boolean isDeleted = ProdukLunar.hapusProduk(id);
            if (isDeleted) {
                System.out.println("Produk dengan ID " + id + " berhasil dihapus.");
            } else {
                System.out.println("Produk dengan ID " + id + " tidak ditemukan.");
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan saat menghapus produk: " + e.getMessage());
        }
    }

    private void lihatTransaksi() {
        String filePath = "D:\\Latihan VSCode\\Java Semester 4\\PA PBO LunarBakeshop\\src\\Pesanan\\HistoriPesanan.csv";
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File transaksi tidak ditemukan.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String headerLine = br.readLine();
            if (headerLine != null) {
                String[] headers = headerLine.split(",", -1);

                System.out.println("\n╔═════════════════════════════════════════════════════════════════════════════════════════════╗");
                System.out.println("║                                         Data Transaksi                                      ║");
                System.out.println("╠════════════╦══════════════════════╦════════════╦══════════════════════╦═════════╦═══════════╣");
                System.out.printf("║ %-10s║ %-20s ║ %-10s ║ %-20s ║ %-5s  ║ %-9s ║\n",
                        headers.length > 0 ? headers[0] : "-",
                        headers.length > 1 ? headers[1] : "-",
                        headers.length > 2 ? headers[2] : "-",
                        headers.length > 3 ? headers[3] : "-",
                        headers.length > 4 ? headers[4] : "-",
                        headers.length > 5 ? headers[7] : "-");
                System.out.println("╠════════════╬══════════════════════╬════════════╬══════════════════════╬═════════╬═══════════╣");
            }

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                System.out.printf("║ %-10s ║ %-20s ║ %-10s ║ %-20s ║ %-5s   ║ %-9s ║\n",
                        data.length > 0 ? data[0] : "-",
                        data.length > 1 ? data[1] : "-",
                        data.length > 2 ? data[2] : "-",
                        data.length > 3 ? data[3] : "-",
                        data.length > 4 ? data[4] : "-",
                        data.length > 5 ? data[7] : "-");
            }
            System.out.println("╚════════════╩══════════════════════╩════════════╩══════════════════════╩═════════╩═══════════╝");
        } catch (IOException e) {
            System.out.println("Gagal membaca file. " + e.getMessage());
        }
    }

    private void kelolaPegawai() {
        String filePath = "D:\\Latihan VSCode\\Java Semester 4\\PA PBO LunarBakeshop\\src\\Auth\\Pegawai.csv";
        boolean managing = true;

        while (managing) {
            System.out.println("\nKelola Pegawai");
            System.out.println("1. Tambah Pegawai");
            System.out.println("2. Lihat Pegawai");
            System.out.println("3. Update Pegawai");
            System.out.println("4. Hapus Pegawai");
            System.out.println("5. Kembali ke Menu Utama");
            System.out.print("Pilih: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    tambahPegawai(filePath);
                    break;

                case 2:
                    lihatPegawai(filePath);
                    break;

                case 3:
                    updatePegawai(filePath);
                    break;

                case 4:
                    hapusPegawai(filePath);
                    break;

                case 5:
                    managing = false;
                    break;

                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        }
    }

    private void tambahPegawai(String filePath) {
        System.out.print("Masukkan ID Pegawai: ");
        String id = scanner.nextLine().trim();
        if (!id.matches("\\d+")) {
            System.out.println("ID Pegawai harus berupa angka. Silakan coba lagi.");
            return;
        }

        boolean idExists = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length > 0 && data[0].trim().equals(id)) {
                    idExists = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file pegawai: " + e.getMessage());
            return;
        }

        if (idExists) {
            System.out.println("ID Pegawai sudah digunakan. Silakan gunakan ID lain.");
            return;
        }

        System.out.print("Masukkan Nama Pegawai: ");
        String nama = scanner.nextLine().trim();
        if (!nama.matches("[a-zA-Z ]+")) {
            System.out.println("Nama Pegawai hanya boleh mengandung huruf dan spasi. Silakan coba lagi.");
            return;
        }

        System.out.print("Masukkan Email Pegawai: ");
        String email = scanner.nextLine().trim();
        if (!email.matches("[a-zA-Z0-9@.]+") || !email.contains("@") || email.chars().filter(ch -> ch == '@').count() != 1) {
            System.out.println("Email hanya boleh mengandung huruf, angka, simbol '.' dan '@', serta harus memiliki satu '@'. Silakan coba lagi.");
            return;
        }
        System.out.print("Masukkan Password Pegawai: ");
        String password = scanner.nextLine().trim();

        if (id.isEmpty() || nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("Semua field harus diisi.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(id + "," + nama + "," + email + "," + password);
            bw.newLine();
            System.out.println("Pegawai berhasil ditambahkan.");
        } catch (IOException e) {
            System.out.println("Gagal menambahkan pegawai: " + e.getMessage());
        }
    }

    private void lihatPegawai(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File pegawai tidak ditemukan.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            System.out.println("\n╔════════════╦══════════════════════╦══════════════════════════════╦══════════════╗");
            System.out.println("║ ID         ║ Nama                 ║ Email                        ║ Password     ║");
            System.out.println("╠════════════╬══════════════════════╬══════════════════════════════╬══════════════╣");
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                System.out.printf("║ %-10s ║ %-20s ║ %-29s║ %-12s ║\n",
                        data.length > 0 ? data[0] : "-",
                        data.length > 1 ? data[1] : "-",
                        data.length > 2 ? data[2] : "-",
                        data.length > 3 ? data[3] : "-");
            }
            System.out.println("╚════════════╩══════════════════════╩══════════════════════════════╩══════════════╝");
        } catch (IOException e) {
            System.out.println("Gagal membaca file pegawai: " + e.getMessage());
        }
    }

    private void updatePegawai(String filePath) {
        System.out.print("Masukkan ID Pegawai yang ingin diupdate: ");
        String updateId = scanner.nextLine().trim();

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File pegawai tidak ditemukan.");
            return;
        }

        List<String> updatedData = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length > 0 && data[0].equals(updateId)) {
                    found = true;
                    System.out.println("Biarkan kosong jika tidak ingin mengubah field.");

                    System.out.print("Nama baru (" + data[1] + "): ");
                    String newName = scanner.nextLine().trim();
                    System.out.print("Email baru (" + data[2] + "): ");
                    String newEmail = scanner.nextLine().trim();
                    System.out.print("Password baru (" + data[3] + "): ");
                    String newPassword = scanner.nextLine().trim();

                    String updatedName = newName.isEmpty() ? data[1] : newName;
                    String updatedEmail = newEmail.isEmpty() ? data[2] : newEmail;
                    String updatedPassword = newPassword.isEmpty() ? data[3] : newPassword;

                    updatedData.add(data[0] + "," + updatedName + "," + updatedEmail + "," + updatedPassword);
                } else {
                    updatedData.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file pegawai: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("Pegawai dengan ID " + updateId + " tidak ditemukan.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String line : updatedData) {
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Pegawai berhasil diupdate.");
        } catch (IOException e) {
            System.out.println("Gagal mengupdate pegawai: " + e.getMessage());
        }
    }

    private void hapusPegawai(String filePath) {
        System.out.print("Masukkan ID Pegawai yang ingin dihapus: ");
        String removeId = scanner.nextLine().trim();

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File pegawai tidak ditemukan.");
            return;
        }

        List<String> updatedData = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length > 0 && data[0].equals(removeId)) {
                    found = true;
                } else {
                    updatedData.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file pegawai: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("Pegawai dengan ID " + removeId + " tidak ditemukan.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String line : updatedData) {
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Pegawai berhasil dihapus.");
        } catch (IOException e) {
            System.out.println("Gagal menghapus pegawai: " + e.getMessage());
        }
    }

    private void laporanPenjualan() {
        System.out.println(">> Laporan Penjualan <<");
        String filePath = "D:\\Latihan VSCode\\Java Semester 4\\PA PBO LunarBakeshop\\src\\Pesanan\\HistoriPesanan.csv";
        String produkFilePath = "D:\\Latihan VSCode\\Java Semester 4\\PA PBO LunarBakeshop\\src\\Produk\\Produk.csv";
        java.util.Map<String, Double> hargaProduk = new java.util.HashMap<>();
        java.util.Map<String, Integer> produkTerjual = new java.util.HashMap<>();
        java.util.Set<String> idPemesananUnik = new java.util.HashSet<>();
        double totalTransaksi = 0.0;

        try (BufferedReader br = new BufferedReader(new FileReader(produkFilePath))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String idProduk = data[0].trim();
                    double harga = Double.parseDouble(data[2].trim());
                    hargaProduk.put(idProduk, harga);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Gagal membaca file produk: " + e.getMessage());
            return;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File laporan penjualan tidak ditemukan.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            if (line == null) {
                System.out.println("File laporan penjualan kosong.");
                return;
            }

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 8) {
                    try {
                        String idPemesanan = data[1].trim();
                        String idProduk = data[2].trim();
                        String namaProduk = data[3].trim();
                        int jumlah = Integer.parseInt(data[4].trim());
                        String status = data[7].trim();
                        double harga = hargaProduk.getOrDefault(idProduk, 0.0);

                        if (status.equalsIgnoreCase("Done")) {
                            idPemesananUnik.add(idPemesanan);
                            produkTerjual.put(namaProduk, produkTerjual.getOrDefault(namaProduk, 0) + jumlah);
                            totalTransaksi += jumlah * harga;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Data tidak valid (format angka salah): " + line);
                    }
                } else {
                    System.out.println("Data tidak valid (kolom kurang): " + line);
                }
            }

            String topSelling = "-";
            int maxJumlah = 0;
            for (var entry : produkTerjual.entrySet()) {
                if (entry.getValue() > maxJumlah) {
                    maxJumlah = entry.getValue();
                    topSelling = entry.getKey();
                }
            }

            System.out.println("Total Pesanan (Done): " + idPemesananUnik.size());
            System.out.println("Total Transaksi (Done): Rp " + totalTransaksi);
            System.out.println("Top Selling Product: " + topSelling + (maxJumlah > 0 ? " (" + maxJumlah + " terjual)" : ""));
        } catch (IOException e) {
            System.out.println("Gagal membaca file laporan penjualan: " + e.getMessage());
        }
    }
}