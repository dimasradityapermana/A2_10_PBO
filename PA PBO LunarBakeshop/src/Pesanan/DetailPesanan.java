package Pesanan;

import Produk.ProdukLunar;

public class DetailPesanan {
    private ProdukLunar produk;
    private int jumlah;

    public DetailPesanan(ProdukLunar produk, int jumlah) {
        this.produk = produk;
        this.jumlah = jumlah;
    }

    public ProdukLunar getProduk() {
        return produk;
    }

    public int getJumlah() {
        return jumlah;
    }
}