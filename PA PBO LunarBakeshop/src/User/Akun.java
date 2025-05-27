package User;

public interface Akun {
    int getIdUser();
    String getNama();
    String getEmail();
    String getPassword();
    void ubahPassword(String newPassword);
    void setNama(String nama);
}