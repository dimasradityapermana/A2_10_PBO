package User;

public abstract class User {
    protected int idUser;
    protected String nama;
    protected String email;
    protected String password;

    public User(int idUser, String nama, String email, String password) {
        this.idUser = idUser;
        this.nama = nama;
        this.email = email;
        this.password = password;
    }

    public int getIdUser() { return idUser; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public void setNama(String nama) { this.nama = nama;}
}