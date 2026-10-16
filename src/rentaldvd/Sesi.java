package rentaldvd;

/**
 * Menyimpan data pengguna yang sedang login (username, nama, role).
 * Diisi oleh formLogin setelah login berhasil, dibaca oleh form lain
 * untuk menentukan hak akses (admin / user).
 */
public final class Sesi {

    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_USER = "user";

    private static String username = "";
    private static String nama = "";
    private static String role = ROLE_USER;

    private Sesi() {
    }

    public static void mulai(String username, String nama, String role) {
        Sesi.username = username == null ? "" : username;
        Sesi.nama = nama == null ? "" : nama;
        Sesi.role = ROLE_ADMIN.equalsIgnoreCase(role) ? ROLE_ADMIN : ROLE_USER;
    }

    public static String getUsername() {
        return username;
    }

    public static String getNama() {
        return nama;
    }

    public static String getRole() {
        return role;
    }

    public static boolean isAdmin() {
        return ROLE_ADMIN.equals(role);
    }

    public static String labelRole() {
        return isAdmin() ? "Admin" : "User";
    }
}
