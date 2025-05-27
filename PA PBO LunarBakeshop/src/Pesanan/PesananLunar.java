package Pesanan;

import java.util.List;

public class PesananLunar {
    private List<DetailPesanan> daftarItem;
    private String metodePembayaran;
    private String idPesanan;

    public PesananLunar(List<DetailPesanan> daftarItem, String metodePembayaran) {
        this.daftarItem = daftarItem;
        this.metodePembayaran = metodePembayaran;
    }

    public String getIdPesanan() {
        return idPesanan;
    }

    public void setIdPesanan(String idPesanan) {
        this.idPesanan = idPesanan;
    }

    public List<DetailPesanan> getDaftarItem() {
        return daftarItem;
    }

    public String getMetodePembayaran() {
        return metodePembayaran;
    }

    public String setStatus (String status) {
        return status;
    }
}
