package Pesanan;

import Produk.ProdukLunar;
import java.io.*;
import java.util.*;

public class PesananManager {
    private static final String FILE_PATH = "D:\\Latihan VSCode\\Java Semester 4\\PA PBO LunarBakeshop\\src\\Pesanan\\HistoriPesanan.csv";

    public void simpanPesanan(String idPelanggan, PesananLunar pesanan, String timestamp, String statusPesanan) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            for (DetailPesanan item : pesanan.getDaftarItem()) {
                writer.write(
                        idPelanggan + "," +
                        pesanan.getIdPesanan() + "," +
                        item.getProduk().getIdProduk() + "," +
                        item.getProduk().getNamaProduk() + "," +
                        item.getJumlah() + "," +
                        pesanan.getMetodePembayaran() + "," +
                        timestamp + "," +
                        statusPesanan);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Gagal menyimpan pesanan: " + e.getMessage());
        }
    }

    public List<PesananLunar> getAllPesanan() {
        List<PesananLunar> pesananList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    String idProduk = data[0];
                    String namaProduk = data[1];
                    int jumlah = Integer.parseInt(data[2]);
                    String metodePembayaran = data[3];

                    ProdukLunar produk = new ProdukLunar(idProduk, namaProduk, 0, 0, "");
                    DetailPesanan detail = new DetailPesanan(produk, jumlah);

                    PesananLunar pesanan = new PesananLunar(Collections.singletonList(detail), metodePembayaran);
                    pesananList.add(pesanan);
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file pesanan: " + e.getMessage());
        }
        return pesananList;
    }
}
