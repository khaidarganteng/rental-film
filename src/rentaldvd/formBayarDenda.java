package rentaldvd;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Form Pembayaran Denda.
 *
 * Denda yang dibayar adalah denda yang sudah tercatat di detail_pinjam.denda
 * (diisi saat film dikembalikan atau diperpanjang). Setiap pembayaran dicatat
 * di tabel pembayaran_denda (boleh dicicil). Sisa = denda - total dibayar.
 *
 * Tabel pembayaran_denda dibuat otomatis jika belum ada.
 */
public class formBayarDenda extends javax.swing.JFrame {

    private static final String[] KOLOM = {"ID Detail", "ID Pinjam", "Anggota",
        "Film", "Jatuh Tempo", "Status", "Denda", "Dibayar", "Sisa"};

    /** Sisa dan id detail untuk tiap baris tabel (urutannya sama dengan tabel). */
    private final List<long[]> angka = new ArrayList<long[]>();
    private final List<String> idAnggotaBaris = new ArrayList<String>();
    private DefaultTableModel model;

    public formBayarDenda() {
        this(null);
    }

    /** @param idAnggota ID anggota yang langsung ditampilkan (boleh null). */
    public formBayarDenda(String idAnggota) {
        initComponents();
        if (idAnggota != null) {
            txtIDAnggota.setText(idAnggota);
        }
        TombolKembali.pasang(this);
        Tema.terapkan(this);
        this.setLocationRelativeTo(null);
        muatData();
    }

    // ------------------------------------------------------------------
    //  Tabel pembayaran
    // ------------------------------------------------------------------

    /** Membuat tabel pembayaran_denda bila belum ada. */
    static void siapkanTabel(Connection c) throws SQLException {
        try (Statement st = c.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS pembayaran_denda ("
                    + "IdBayar INT NOT NULL AUTO_INCREMENT, "
                    + "IdDetailPinjam INT NOT NULL, "
                    + "IdPinjam VARCHAR(16), "
                    + "IDAnggota VARCHAR(8), "
                    + "tgl_bayar DATETIME, "
                    + "jumlah INT NOT NULL, "
                    + "PRIMARY KEY (IdBayar), KEY IdDetailPinjam (IdDetailPinjam)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=latin1");
        }
    }

    /** Total sisa denda (denda tercatat - dibayar) seorang anggota. */
    static long sisaDendaAnggota(Connection c, String idAnggota) throws SQLException {
        siapkanTabel(c);
        String sql = "SELECT COALESCE(SUM(d.denda),0) AS denda, "
                + "COALESCE(SUM((SELECT COALESCE(SUM(b.jumlah),0) FROM pembayaran_denda b "
                + "WHERE b.IdDetailPinjam=d.IdDetailPinjam)),0) AS bayar "
                + "FROM detail_pinjam d INNER JOIN pinjam p ON p.IDPinjam = d.IdPinjam "
                + "WHERE p.IDAnggota=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idAnggota);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Math.max(0, rs.getLong("denda") - rs.getLong("bayar"));
            }
        }
        return 0;
    }

    // ------------------------------------------------------------------
    //  Memuat data
    // ------------------------------------------------------------------

    private void muatData() {
        angka.clear();
        idAnggotaBaris.clear();
        model.setRowCount(0);
        lblInfo.setText(" ");

        ConfigDB db = new ConfigDB();
        if (db.koneksi == null) {
            return;
        }
        Connection c = db.koneksi;
        try {
            siapkanTabel(c);
            String id = txtIDAnggota.getText().trim();
            String sql = "SELECT d.IdDetailPinjam, d.IdPinjam, p.IDAnggota, a.nama, f.Judul, "
                    + "d.tgl_kembali, d.status, d.denda, "
                    + "COALESCE((SELECT SUM(b.jumlah) FROM pembayaran_denda b "
                    + "WHERE b.IdDetailPinjam=d.IdDetailPinjam),0) AS dibayar "
                    + "FROM detail_pinjam d "
                    + "INNER JOIN pinjam p ON p.IDPinjam = d.IdPinjam "
                    + "LEFT JOIN anggota a ON a.IDAnggota = p.IDAnggota "
                    + "LEFT JOIN film f ON f.KodeFilm = d.KodeFilm "
                    + "WHERE d.denda > 0 "
                    + (id.isEmpty() ? "" : "AND p.IDAnggota=? ")
                    + "ORDER BY d.IdDetailPinjam DESC";
            long totalSisa = 0;
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                if (!id.isEmpty()) {
                    ps.setString(1, id);
                }
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    long denda = rs.getLong("denda");
                    long dibayar = rs.getLong("dibayar");
                    long sisa = Math.max(0, denda - dibayar);
                    if (chkBelumLunas.isSelected() && sisa == 0) {
                        continue;
                    }
                    totalSisa += sisa;
                    model.addRow(new Object[]{
                        rs.getString("IdDetailPinjam"), rs.getString("IdPinjam"),
                        rs.getString("IDAnggota") + " - " + rs.getString("nama"),
                        rs.getString("Judul"), rs.getDate("tgl_kembali"),
                        rs.getInt("status") == 1 ? "Kembali" : "Belum kembali",
                        formTransaksi.rupiah(denda), formTransaksi.rupiah(dibayar),
                        sisa == 0 ? "LUNAS" : formTransaksi.rupiah(sisa)});
                    angka.add(new long[]{sisa, rs.getLong("IdDetailPinjam")});
                    idAnggotaBaris.add(rs.getString("IDAnggota"));
                }
            }
            lblTotal.setText("Total sisa denda yang tampil: " + formTransaksi.rupiah(totalSisa));
            lblInfo.setText(infoDendaBerjalan(c, id));
            if (model.getRowCount() == 0) {
                lblInfo.setText((id.isEmpty() ? "Tidak ada denda yang perlu dibayar."
                        : "Anggota " + id + " tidak memiliki denda yang perlu dibayar.")
                        + "  " + lblInfo.getText().trim());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data denda:\n" + e.getMessage(),
                    "Kesalahan", JOptionPane.WARNING_MESSAGE);
        } finally {
            tutup(db);
        }
    }

    /** Info film yang belum kembali tapi sudah terlambat (denda belum tercatat). */
    private String infoDendaBerjalan(Connection c, String id) throws SQLException {
        String sql = "SELECT d.tgl_kembali FROM detail_pinjam d "
                + "INNER JOIN pinjam p ON p.IDPinjam = d.IdPinjam "
                + "WHERE d.status=0 AND d.tgl_kembali < CURDATE() "
                + (id.isEmpty() ? "" : "AND p.IDAnggota=?");
        long jumlah = 0;
        long berjalan = 0;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            if (!id.isEmpty()) {
                ps.setString(1, id);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jumlah++;
                berjalan += formTransaksi.hariTerlambat(rs.getDate("tgl_kembali"))
                        * formTransaksi.DENDA_PER_HARI;
            }
        }
        if (jumlah == 0) {
            return " ";
        }
        return jumlah + " film belum dikembalikan dan sudah terlambat (denda berjalan "
                + formTransaksi.rupiah(berjalan) + ", baru bisa dibayar setelah film "
                + "dikembalikan atau diperpanjang).";
    }

    // ------------------------------------------------------------------
    //  Pembayaran
    // ------------------------------------------------------------------

    /** Mencatat pembayaran untuk satu baris detail; mengembalikan jumlah tercatat. */
    private void catatBayar(Connection c, long idDetail, String idAnggota, long jumlah)
            throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO pembayaran_denda (IdDetailPinjam, IdPinjam, IDAnggota, "
                + "tgl_bayar, jumlah) SELECT d.IdDetailPinjam, d.IdPinjam, ?, NOW(), ? "
                + "FROM detail_pinjam d WHERE d.IdDetailPinjam=?")) {
            ps.setString(1, idAnggota);
            ps.setLong(2, jumlah);
            ps.setLong(3, idDetail);
            ps.executeUpdate();
        }
    }

    private void btnBayarActionPerformed(java.awt.event.ActionEvent evt) {
        int baris = tabel.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih dulu satu baris denda pada tabel");
            return;
        }
        long sisa = angka.get(baris)[0];
        long idDetail = angka.get(baris)[1];
        if (sisa <= 0) {
            JOptionPane.showMessageDialog(this, "Denda ini sudah lunas");
            return;
        }
        String isi = JOptionPane.showInputDialog(this,
                "Film: " + model.getValueAt(baris, 3) + "\nSisa denda: "
                + formTransaksi.rupiah(sisa) + "\n\nJumlah pembayaran (Rp):",
                String.valueOf(sisa));
        if (isi == null) {
            return;
        }
        long jumlah;
        try {
            jumlah = Long.parseLong(isi.trim().replace(".", "").replace(",", ""));
        } catch (NumberFormatException e) {
            jumlah = -1;
        }
        if (jumlah < 1 || jumlah > sisa) {
            JOptionPane.showMessageDialog(this, "Jumlah pembayaran harus angka antara Rp 1 "
                    + "dan " + formTransaksi.rupiah(sisa));
            return;
        }
        int jawab = JOptionPane.showConfirmDialog(this, "Terima pembayaran "
                + formTransaksi.rupiah(jumlah) + " untuk film \""
                + model.getValueAt(baris, 3) + "\"?\nSisa setelah bayar: "
                + formTransaksi.rupiah(sisa - jumlah), "Konfirmasi Pembayaran",
                JOptionPane.YES_NO_OPTION);
        if (jawab != JOptionPane.YES_OPTION) {
            return;
        }

        ConfigDB db = new ConfigDB();
        if (db.koneksi == null) {
            return;
        }
        Connection c = db.koneksi;
        try {
            c.setAutoCommit(false);
            catatBayar(c, idDetail, idAnggotaBaris.get(baris), jumlah);
            c.commit();
            JOptionPane.showMessageDialog(this, "Pembayaran " + formTransaksi.rupiah(jumlah)
                    + " berhasil dicatat.\n" + (sisa - jumlah == 0 ? "Denda film ini LUNAS."
                    : "Sisa denda: " + formTransaksi.rupiah(sisa - jumlah)),
                    "Pembayaran Berhasil", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            batal(c);
            JOptionPane.showMessageDialog(this, "Pembayaran gagal dicatat:\n" + e.getMessage(),
                    "Kesalahan", JOptionPane.WARNING_MESSAGE);
        } finally {
            tutup(db);
        }
        muatData();
    }

    private void btnLunasiActionPerformed(java.awt.event.ActionEvent evt) {
        Set<String> anggota = new HashSet<String>();
        long total = 0;
        int banyak = 0;
        for (int i = 0; i < angka.size(); i++) {
            if (angka.get(i)[0] > 0) {
                total += angka.get(i)[0];
                banyak++;
                anggota.add(idAnggotaBaris.get(i));
            }
        }
        if (banyak == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada denda yang perlu dibayar");
            return;
        }
        if (anggota.size() != 1) {
            JOptionPane.showMessageDialog(this, "Isi ID Anggota lalu klik Cari dahulu, "
                    + "supaya yang dilunasi hanya denda satu anggota");
            txtIDAnggota.requestFocus();
            return;
        }
        String idAng = anggota.iterator().next();
        int jawab = JOptionPane.showConfirmDialog(this, "Lunasi " + banyak
                + " denda milik anggota " + idAng + "?\nTotal: " + formTransaksi.rupiah(total),
                "Konfirmasi Pelunasan", JOptionPane.YES_NO_OPTION);
        if (jawab != JOptionPane.YES_OPTION) {
            return;
        }

        ConfigDB db = new ConfigDB();
        if (db.koneksi == null) {
            return;
        }
        Connection c = db.koneksi;
        try {
            c.setAutoCommit(false);
            for (int i = 0; i < angka.size(); i++) {
                if (angka.get(i)[0] > 0) {
                    catatBayar(c, angka.get(i)[1], idAnggotaBaris.get(i), angka.get(i)[0]);
                }
            }
            c.commit();
            JOptionPane.showMessageDialog(this, "Pelunasan " + formTransaksi.rupiah(total)
                    + " berhasil dicatat.\nSemua denda anggota " + idAng + " LUNAS.",
                    "Pembayaran Berhasil", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            batal(c);
            JOptionPane.showMessageDialog(this, "Pelunasan gagal dicatat:\n" + e.getMessage(),
                    "Kesalahan", JOptionPane.WARNING_MESSAGE);
        } finally {
            tutup(db);
        }
        muatData();
    }

    private void tutup(ConfigDB db) {
        try {
            if (db != null && db.koneksi != null) {
                db.koneksi.close();
            }
        } catch (SQLException e) {
        }
    }

    private void batal(Connection c) {
        try {
            if (c != null) {
                c.rollback();
            }
        } catch (SQLException e) {
        }
    }

    // ------------------------------------------------------------------
    //  Tampilan
    // ------------------------------------------------------------------

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pembayaran Denda");

        javax.swing.JLabel lblId = new javax.swing.JLabel("ID Anggota");
        txtIDAnggota = new javax.swing.JTextField(10);
        javax.swing.JButton btnCari = new javax.swing.JButton("Cari");
        chkBelumLunas = new javax.swing.JCheckBox("Hanya yang belum lunas", true);
        javax.swing.JButton btnSemua = new javax.swing.JButton("Semua Anggota");

        javax.swing.JPanel atas = new javax.swing.JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        atas.add(lblId);
        atas.add(txtIDAnggota);
        atas.add(btnCari);
        atas.add(btnSemua);
        atas.add(chkBelumLunas);

        model = new DefaultTableModel(new Object[][]{}, KOLOM) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tabel = new javax.swing.JTable(model);
        tabel.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabel.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        int[] lebar = {70, 100, 190, 170, 95, 100, 90, 90, 90};
        for (int i = 0; i < lebar.length; i++) {
            tabel.getColumnModel().getColumn(i).setPreferredWidth(lebar[i]);
        }
        javax.swing.JScrollPane sp = new javax.swing.JScrollPane(tabel);
        sp.setPreferredSize(new java.awt.Dimension(820, 260));

        lblInfo = new javax.swing.JLabel(" ");
        lblTotal = new javax.swing.JLabel(" ");
        lblTotal.setFont(lblTotal.getFont().deriveFont(java.awt.Font.BOLD));
        javax.swing.JButton btnBayar = new javax.swing.JButton("Bayar Denda");
        javax.swing.JButton btnLunasi = new javax.swing.JButton("Lunasi Semua Anggota Ini");
        javax.swing.JButton btnRefresh = new javax.swing.JButton("Refresh");

        javax.swing.JPanel tombol = new javax.swing.JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        tombol.add(btnBayar);
        tombol.add(btnLunasi);
        tombol.add(btnRefresh);

        javax.swing.JPanel bawah = new javax.swing.JPanel();
        bawah.setLayout(new javax.swing.BoxLayout(bawah, javax.swing.BoxLayout.Y_AXIS));
        bawah.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 10, 8, 10));
        lblInfo.setAlignmentX(0f);
        lblTotal.setAlignmentX(0f);
        tombol.setAlignmentX(0f);
        bawah.add(lblInfo);
        bawah.add(javax.swing.Box.createVerticalStrut(4));
        bawah.add(lblTotal);
        bawah.add(tombol);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(atas, BorderLayout.NORTH);
        getContentPane().add(sp, BorderLayout.CENTER);
        getContentPane().add(bawah, BorderLayout.SOUTH);

        java.awt.event.ActionListener cari = new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                muatData();
            }
        };
        btnCari.addActionListener(cari);
        txtIDAnggota.addActionListener(cari);
        chkBelumLunas.addActionListener(cari);
        btnRefresh.addActionListener(cari);
        btnSemua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDAnggota.setText("");
                muatData();
            }
        });
        btnBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarActionPerformed(evt);
            }
        });
        btnLunasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLunasiActionPerformed(evt);
            }
        });
        pack();
    }

    private javax.swing.JTextField txtIDAnggota;
    private javax.swing.JCheckBox chkBelumLunas;
    private javax.swing.JTable tabel;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblTotal;
}
