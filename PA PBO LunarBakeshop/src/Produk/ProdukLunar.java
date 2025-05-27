package Produk;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProdukLunar {
    private static final String FILE_PATH = "D:\\Latihan VSCode\\Java Semester 4\\PA PBO LunarBakeshop\\src\\Produk\\Produk.csv";

    private String idProduk;
    private String namaProduk;
    private double harga;
    private int stok;
    private String kategori;

    public ProdukLunar(String idProduk, String namaProduk, double harga, int stok, String kategori) {
        this.idProduk = idProduk;
        this.namaProduk = namaProduk;
        this.harga = harga;
        this.stok = stok;
        this.kategori = kategori;
    }

    public ProdukLunar() {
        this.idProduk = "default";
        this.namaProduk = "Default Product";
        this.harga = 0.0;
        this.stok = 0;
        this.kategori = "Uncategorized";
    }

    public String getIdProduk() {
        return idProduk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    @Override
    public String toString() {
        return idProduk + "," + namaProduk + "," + harga + "," + stok + "," + kategori;
    }

    public static List<ProdukLunar> getAllProduk() {
        List<ProdukLunar> produkList = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            System.out.println("File produk tidak ditemukan, membuat file baru...");
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Gagal membuat file produk.");
                e.printStackTrace();
            }
            return produkList;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    try {
                        ProdukLunar produk = new ProdukLunar(
                                data[0].trim(),
                                data[1].trim(),
                                Double.parseDouble(data[2].trim()),
                                Integer.parseInt(data[3].trim()),
                                data[4].trim()
                        );
                        produkList.add(produk);
                    } catch (NumberFormatException e) {
                        System.out.println("Format data salah di baris: " + line);
                    }
                } else {
                    System.out.println("Format data salah di baris: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file produk.");
            e.printStackTrace();
        }
        return produkList;
    }

    public static void tambahProduk(ProdukLunar produk) {
        if (produk == null) {
            System.out.println("Produk tidak valid.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(produk.toString());
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void simpanSemuaProduk(List<ProdukLunar> daftarProduk) {
        if (daftarProduk == null) {
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            bw.write("id,nama,harga,stok,jenis");
            bw.newLine();

            for (ProdukLunar produk : daftarProduk) {
                bw.write(produk.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat menyimpan data produk.");
            e.printStackTrace();
        }
    }

    public static boolean hapusProduk(String idProduk) {
        if (idProduk == null || idProduk.isEmpty()) {
            System.out.println("ID produk tidak valid.");
            return false;
        }

        List<ProdukLunar> produkList = getAllProduk();
        boolean found = false;

        for (int i = 0; i < produkList.size(); i++) {
            if (produkList.get(i).getIdProduk().equalsIgnoreCase(idProduk)) {
                produkList.remove(i);
                found = true;
                break;
            }
        }

        if (found) {
            simpanSemuaProduk(produkList);
        } else {

        }

        return found;
    }

    public static void lihatProduk() {
        List<ProdukLunar> produkList = getAllProduk();
        if (produkList == null || produkList.isEmpty()) {
            System.out.println("Belum ada produk.");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              Daftar Produk Tersedia                            ║");
        System.out.println("╠════════════╦══════════════════════╦════════════╦════════════╦══════════════════╣");
        System.out.printf("║ %-10s ║ %-20s ║ %-10s ║ %-10s ║ %-15s  ║\n", "ID", "Nama Produk", "Harga", "Stok", "Kategori");
        System.out.println("╠════════════╬══════════════════════╬════════════╬════════════╬══════════════════╣");

        for (ProdukLunar produk : produkList) {
            if (produk != null) {
                String namaProduk = produk.getNamaProduk() != null ? produk.getNamaProduk() : "Tidak Diketahui";
                String kategori = produk.getKategori() != null ? produk.getKategori() : "Tidak Diketahui";

                namaProduk = namaProduk.length() > 20 ? namaProduk.substring(0, 17) + "..." : namaProduk;
                kategori = kategori.length() > 15 ? kategori.substring(0, 12) + "..." : kategori;

                System.out.printf("║ %-10s ║ %-20s ║ Rp%-8.2f ║ %-10d ║ %-15s  ║\n",
                        produk.getIdProduk(),
                        namaProduk,
                        produk.getHarga(),
                        produk.getStok(),
                        kategori);
            }
        }
        System.out.println("╚════════════╩══════════════════════╩════════════╩════════════╩══════════════════╝");

    }
}