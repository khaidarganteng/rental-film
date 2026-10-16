package rentaldvd;

import javax.swing.JOptionPane;
import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;

/**
 * 
 */
public class formTransaksi extends javax.swing.JFrame {
 
    String judulPinjam[] = {"No", "ID Pinjam", "Kode Film", "Tgl. Pinjam",
        "Tgl. Kembali", "Status", "Keterangan", "Denda"};
    int LebarPinjam[] = {70, 120, 250, 110, 200, 120, 120, 120};
    String SQLPinjam = "SELECT * FROM detail_pinjam ORDER BY IDPinjam DESC";
    DefaultTableModel List;

    /**
     * 
     */
    public formTransaksi() {
        initComponents();
        txtDenda.setEditable(false);
        txtDenda.setToolTipText("Denda terisi otomatis (Rp1.000 per hari terlambat per film)");
        TombolKembali.pasang(this);
        Tema.terapkan(this);

        
        this.setLocationRelativeTo(null);
        new ConfigDB().tampilTabel(judulPinjam, SQLPinjam, JTablePinjam);
        new ConfigDB().aturLebarKolom(JTablePinjam, LebarPinjam);
        List = new DefaultTableModel();
        JTableList.setModel(List);
        List.addColumn("ID Anggota");
        List.addColumn("Nama Anggota");
        List.addColumn("Kode Film");
        List.addColumn("Judul");

        
        autonomer();

        
        txtIDAnggota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cariDataAnggota();
            }
        });

      
        txtKodeFilm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cariDataFilm();
            }
        });
    }

    /**
    
     */
    private void autonomer() {
        ConfigDB db = new ConfigDB();
        try {
            if (db.koneksi == null) {
                return;
            }
            String tglUrut = new SimpleDateFormat("yyMMdd").format(new java.util.Date());
            String prefix = "PJ-" + tglUrut + "-";

            // Nomor urut dicari dari ID yang sudah ada dengan awalan hari ini
            // (bukan dari tanggal pinjam, supaya tidak ikut berubah bila tanggal diubah).
            int urutTerakhir = 0;
            try (PreparedStatement ps = db.koneksi.prepareStatement(
                    "SELECT MAX(CAST(RIGHT(IDPinjam,3) AS UNSIGNED)) AS no_pinjam "
                    + "FROM pinjam WHERE IDPinjam LIKE ?")) {
                ps.setString(1, prefix + "%");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    urutTerakhir = rs.getInt("no_pinjam"); // NULL -> 0
                }
            }
            txtIDPinjam.setText(prefix + String.format("%03d", urutTerakhir + 1));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ERROR: \n" + e.toString(),
                    "Kesalahan", JOptionPane.WARNING_MESSAGE);
        } finally {
            tutup(db);
        }
    }

    /**
     * 
     */
    private void cariDataAnggota() {
        try {
            String id = txtIDAnggota.getText().trim();
            if (id.isEmpty()) {
                txtNamaAnggota.setText(null);
                return;
            }
            Statement st = new ConfigDB().koneksi.createStatement();
            ResultSet ra = st.executeQuery("SELECT * FROM anggota WHERE IDAnggota='" + id + "'");
            if (ra.next()) {
                txtNamaAnggota.setText(ra.getString("nama"));
            } else {
                txtNamaAnggota.setText(null);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Auto-detect Judul Film berdasarkan Kode Film yang sedang diketik.
     */
    private void cariDataFilm() {
        try {
            String kode = txtKodeFilm.getText().trim();
            if (kode.isEmpty()) {
                txtJudul.setText(null);
                return;
            }
            Statement st = new ConfigDB().koneksi.createStatement();
            ResultSet rk = st.executeQuery("SELECT * FROM film WHERE KodeFilm='" + kode + "'");
            if (rk.next()) {
                txtJudul.setText(rk.getString("judul"));
            } else {
                txtJudul.setText(null);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Auto-detect data transaksi (ID Anggota, Nama Anggota, Kode Film, Judul)
     * berdasarkan ID Pinjam yang sedang diketik — dipakai saat mencari
     * transaksi yang sudah ada (misalnya untuk proses Kembali/Perpanjang).
     */
    private void cariDataPinjam() {
        try {
            String idPinjam = txtIDPinjam.getText().trim();
            if (idPinjam.isEmpty()) {
                return;
            }
            Statement st = new ConfigDB().koneksi.createStatement();

            ResultSet rp = st.executeQuery("SELECT * FROM pinjam WHERE IDPinjam='" + idPinjam + "'");
            if (rp.next()) {
                txtIDAnggota.setText(rp.getString("IDAnggota"));
                cariDataAnggota();
            }

            ResultSet rd = st.executeQuery("SELECT * FROM detail_pinjam WHERE IDPinjam='"
                    + idPinjam + "' ORDER BY IdDetailPinjam DESC LIMIT 1");
            if (rd.next()) {
                txtIDDetailTransaksi.setText(rd.getString("IdDetailPinjam"));
                txtKodeFilm.setText(rd.getString("KodeFilm"));
                cariDataFilm();
            }
        } catch (Exception e) {
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtIDPinjam = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtIDAnggota = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtTglPinjam = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        txtNamaAnggota = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTglKembali = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        txtKodeFilm = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cmbKet = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        txtJudul = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnPinjam = new javax.swing.JButton();
        btnKembali = new javax.swing.JButton();
        btnPerpanjang = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTableList = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        JTablePinjam = new javax.swing.JTable();
        btnView = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtDenda = new javax.swing.JTextField();
        txtIDDetailTransaksi = new java.awt.Label();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("ID Pinjam");

        txtIDPinjam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDPinjamActionPerformed(evt);
            }
        });
        txtIDPinjam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIDPinjamKeyPressed(evt);
            }
        });

        jLabel2.setText("ID Anggota");

        btnCari.setText("Cari Tunggakan");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        jLabel6.setText("Tanggal Pinjam");

        txtTglPinjam.setDateFormatString("yyyy-MM-dd");

        jLabel3.setText("Nama Anggota");

        jLabel7.setText("Tanggal Kembali");

        txtTglKembali.setDateFormatString("yyyy-MM-dd");

        jLabel4.setText("Kode Film");

        jLabel8.setText("Keterangan");

        cmbKet.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pinjam", "Perpanjang", "Kembali" }));
        cmbKet.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                cmbKetAncestorRemoved(evt);
            }
        });
        cmbKet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbKetKeyPressed(evt);
            }
        });

        jLabel5.setText("Judul");

        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnPinjam.setText("Proses Pinjam");
        btnPinjam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPinjamActionPerformed(evt);
            }
        });

        btnKembali.setText("Kembalikan");
        btnKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliActionPerformed(evt);
            }
        });

        btnPerpanjang.setText("Perpanjang");
        btnPerpanjang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerpanjangActionPerformed(evt);
            }
        });

        JTableList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Anggota", "Nama Anggota", "Kode Film", "Judul"
            }
        ));
        jScrollPane1.setViewportView(JTableList);

        JTablePinjam.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "ID Pinjam", "Kode Film", "Tgl. Pinjam", "Tgl. Kembali", "Status", "Keterangan", "Denda"
            }
        ));
        JTablePinjam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTablePinjamMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(JTablePinjam);

        btnView.setText("View Detail Peminjaman");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        jLabel9.setText("Denda");

        txtDenda.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 754, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 754, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnView)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRefresh)
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDenda, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtIDPinjam, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtIDAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnCari)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtTglPinjam, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtNamaAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtTglKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtKodeFilm, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel8))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtJudul, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(130, 130, 130)
                                        .addComponent(cmbKet, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPinjam)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnKembali)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPerpanjang)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtIDDetailTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(62, 62, 62))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtIDPinjam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtIDAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari)
                    .addComponent(jLabel6)
                    .addComponent(txtTglPinjam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNamaAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtTglKembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtKodeFilm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(cmbKet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtJudul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTambah)
                            .addComponent(btnClear)
                            .addComponent(btnPinjam)
                            .addComponent(btnKembali)
                            .addComponent(btnPerpanjang))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnView)
                            .addComponent(btnRefresh)
                            .addComponent(jLabel9)
                            .addComponent(txtDenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtIDDetailTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    
    private void txtIDPinjamKeyPressed(java.awt.event.KeyEvent evt) {                                       
        new ConfigDB().cariData(judulPinjam, "SELECT * FROM detail_pinjam WHERE "
                + "IDPinjam='" + txtIDPinjam.getText() + "'", JTablePinjam);
        new ConfigDB().aturLebarKolom(JTablePinjam, LebarPinjam);
        cariDataPinjam();
    }                                      

   
    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {
        String id = txtIDAnggota.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Anggota belum diisi");
            txtIDAnggota.requestFocus();
            return;
        }
        ConfigDB db = new ConfigDB();
        if (db.koneksi == null) {
            return;
        }
        String sql = "SELECT f.Judul, d.tgl_kembali, d.denda FROM detail_pinjam d "
                + "INNER JOIN pinjam p ON p.IDPinjam = d.IdPinjam "
                + "LEFT JOIN film f ON f.KodeFilm = d.KodeFilm "
                + "WHERE p.IDAnggota=? AND d.status=0 ORDER BY d.tgl_kembali";
        try (PreparedStatement ps = db.koneksi.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            StringBuilder sb = new StringBuilder();
            int jumlah = 0;
            long totalDenda = 0;
            while (rs.next()) {
                jumlah++;
                long telat = hariTerlambat(rs.getDate("tgl_kembali"));
                long denda = rs.getInt("denda") + telat * DENDA_PER_HARI;
                totalDenda += denda;
                sb.append("- ").append(rs.getString("Judul"))
                        .append(" (jatuh tempo ").append(rs.getDate("tgl_kembali"));
                if (telat > 0) {
                    sb.append(", terlambat ").append(telat).append(" hari");
                }
                sb.append(", denda ").append(rupiah(denda)).append(")\n");
            }
            if (jumlah == 0) {
                JOptionPane.showMessageDialog(this, "Tidak ada tunggakan Film "
                        + "yang belum dikembalikan");
            } else {
                JOptionPane.showMessageDialog(this, "Ada " + jumlah
                        + " tunggakan Film yang belum dikembalikan:\n" + sb
                        + "\nTotal denda saat ini: " + rupiah(totalDenda));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mencari tunggakan: " + e.getMessage());
        } finally {
            tutup(db);
        }
    }

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {                                          
        try {
            if (txtKodeFilm.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Kode Film belum diisi");
                txtKodeFilm.requestFocus();
                return;
            }
            if (txtJudul.getText() == null || txtJudul.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Kode Film tidak ditemukan");
                txtKodeFilm.requestFocus();
                return;
            }
            String isi[] = {txtIDAnggota.getText(), txtNamaAnggota.getText(),
                txtKodeFilm.getText(), txtJudul.getText()};
            List.addRow(isi);
            txtKodeFilm.setText(null);
            txtJudul.setText(null);
            txtKodeFilm.requestFocus();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
    }                                         

   
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {                                         
        int JUMLAH = JTableList.getRowCount();
        for (int i = 0; i < JUMLAH; i++) {
            List.removeRow(0);
        }
        txtIDAnggota.setText(null);
        txtNamaAnggota.setText(null);
        txtKodeFilm.setText(null);
        txtJudul.setText(null);
        txtTglKembali.setDate(null);
        txtTglPinjam.setDate(null);
        txtDenda.setText("0");
        txtIDDetailTransaksi.setText("-");
        autonomer();
    }                                        

   
    private void btnPinjamActionPerformed(java.awt.event.ActionEvent evt) {
        if (txtIDPinjam.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Pinjam belum diisi");
            txtIDPinjam.requestFocus();
            return;
        }
        if (txtIDAnggota.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Anggota belum diisi");
            txtIDAnggota.requestFocus();
            return;
        }
        if (txtNamaAnggota.getText() == null || txtNamaAnggota.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Anggota tidak ditemukan");
            txtIDAnggota.requestFocus();
            return;
        }
        if (JTableList.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Belum ada Film yang ditambahkan. "
                    + "Isi Kode Film lalu klik tombol Tambah dahulu");
            txtKodeFilm.requestFocus();
            return;
        }
        if (txtTglPinjam.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Tanggal Pinjam belum dipilih");
            return;
        }
        if (txtTglKembali.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Tanggal Kembali belum dipilih");
            return;
        }
        java.sql.Date tglPinjam = new java.sql.Date(txtTglPinjam.getDate().getTime());
        java.sql.Date tglKembali = new java.sql.Date(txtTglKembali.getDate().getTime());
        if (tglKembali.before(tglPinjam)) {
            JOptionPane.showMessageDialog(this, "Tanggal Kembali tidak boleh "
                    + "sebelum Tanggal Pinjam");
            return;
        }

        ConfigDB db = new ConfigDB();
        if (db.koneksi == null) {
            return;
        }
        Connection c = db.koneksi;
        try {
            if (db.duplikasiData("pinjam", "IDPinjam", txtIDPinjam.getText().trim())) {
                JOptionPane.showMessageDialog(this, "ID Pinjam sudah dipakai. "
                        + "Klik Clear untuk membuat ID Pinjam baru");
                return;
            }

            // Denda berjalan: anggota yang masih punya film lewat jatuh tempo
            // diberi peringatan sebelum meminjam lagi.
            long telatFilm = 0;
            long dendaBerjalan = 0;
            String sqlTelat = "SELECT d.tgl_kembali, d.denda FROM detail_pinjam d "
                    + "INNER JOIN pinjam p ON p.IDPinjam = d.IdPinjam "
                    + "WHERE p.IDAnggota=? AND d.status=0";
            try (PreparedStatement ps = c.prepareStatement(sqlTelat)) {
                ps.setString(1, txtIDAnggota.getText().trim());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    long telat = hariTerlambat(rs.getDate("tgl_kembali"));
                    if (telat > 0) {
                        telatFilm++;
                        dendaBerjalan += rs.getInt("denda") + telat * DENDA_PER_HARI;
                    }
                }
            }
            long dendaBelumDibayar = formBayarDenda.sisaDendaAnggota(c,
                    txtIDAnggota.getText().trim());
            if (telatFilm > 0 || dendaBelumDibayar > 0) {
                int jawab = JOptionPane.showConfirmDialog(this,
                        "Anggota ini masih memiliki tanggungan denda:\n"
                        + "- Film melewati jatuh tempo: " + telatFilm
                        + " (denda berjalan " + rupiah(dendaBerjalan) + ")\n"
                        + "- Denda belum dibayar: " + rupiah(dendaBelumDibayar)
                        + "\n\nTetap proses peminjaman baru?",
                        "Ada Denda Berjalan", JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (jawab != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            c.setAutoCommit(false);
            try (PreparedStatement psP = c.prepareStatement(
                    "INSERT INTO pinjam (IDPinjam, IDAnggota, TotalDenda) VALUES (?,?,0)");
                 PreparedStatement psD = c.prepareStatement(
                    "INSERT INTO detail_pinjam (IdPinjam, KodeFilm, tgl_pinjam, tgl_kembali, "
                    + "status, keterangan, denda) VALUES (?,?,?,?,0,'Pinjam',0)");
                 PreparedStatement psS = c.prepareStatement(
                    "UPDATE film SET stok = stok - 1 WHERE KodeFilm=? AND stok > 0")) {
                psP.setString(1, txtIDPinjam.getText().trim());
                psP.setString(2, txtIDAnggota.getText().trim());
                psP.executeUpdate();
                int jum = JTableList.getRowCount();
                for (int i = 0; i < jum; i++) {
                    psD.setString(1, txtIDPinjam.getText().trim());
                    psD.setString(2, String.valueOf(JTableList.getValueAt(i, 2)));
                    psD.setDate(3, tglPinjam);
                    psD.setDate(4, tglKembali);
                    psD.executeUpdate();

                    // kurangi stok film; gagal (stok habis) -> seluruh transaksi dibatalkan
                    psS.setString(1, String.valueOf(JTableList.getValueAt(i, 2)));
                    if (psS.executeUpdate() == 0) {
                        throw new SQLException("Stok film \""
                                + JTableList.getValueAt(i, 3) + "\" sudah habis");
                    }
                }
                c.commit();
            }
            JOptionPane.showMessageDialog(this, "Peminjaman berhasil disimpan.\n"
                    + "Jatuh tempo: " + tglKembali + "\n"
                    + "Denda keterlambatan: " + rupiah(DENDA_PER_HARI) + " per hari per film");
            refreshTabelPinjam();
            btnClearActionPerformed(evt);
        } catch (SQLException e) {
            batal(c);
            JOptionPane.showMessageDialog(this, "Peminjaman gagal disimpan:\n" + e.getMessage(),
                    "Kesalahan", JOptionPane.WARNING_MESSAGE);
        } finally {
            tutup(db);
        }
    }

    // ====================================================================
    //  DENDA, PENGEMBALIAN DAN PERPANJANGAN
    // ====================================================================

    /** Tarif denda keterlambatan: Rp per hari untuk setiap film. */
    static final int DENDA_PER_HARI = 1000;
    /** Isian awal lama perpanjangan (hari). */
    static final int PERPANJANG_HARI_AWAL = 3;
    static final int PERPANJANG_HARI_MAKS = 30;

    /** Data satu baris detail_pinjam yang sedang diproses. */
    private static class Detail {
        String idDetail, idPinjam, kodeFilm, judul, idAnggota, nama;
        java.sql.Date tglPinjam, tglKembali;
        boolean kembali;
        int denda;
    }

    /** Jumlah hari terlambat dihitung dari jatuh tempo sampai hari ini (min 0). */
    static long hariTerlambat(java.sql.Date jatuhTempo) {
        if (jatuhTempo == null) {
            return 0;
        }
        long h = java.time.temporal.ChronoUnit.DAYS.between(
                jatuhTempo.toLocalDate(), java.time.LocalDate.now());
        return h > 0 ? h : 0;
    }

    static String rupiah(long n) {
        return "Rp " + String.format(java.util.Locale.US, "%,d", n).replace(',', '.');
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

    private void refreshTabelPinjam() {
        new ConfigDB().tampilTabel(judulPinjam, SQLPinjam, JTablePinjam);
        new ConfigDB().aturLebarKolom(JTablePinjam, LebarPinjam);
    }

    private Detail ambilDetail(Connection c, String idDetail) throws SQLException {
        String sql = "SELECT d.IdDetailPinjam, d.IdPinjam, d.KodeFilm, d.tgl_pinjam, "
                + "d.tgl_kembali, d.status, d.denda, f.Judul, p.IDAnggota, a.nama "
                + "FROM detail_pinjam d "
                + "LEFT JOIN pinjam p ON p.IDPinjam = d.IdPinjam "
                + "LEFT JOIN film f ON f.KodeFilm = d.KodeFilm "
                + "LEFT JOIN anggota a ON a.IDAnggota = p.IDAnggota "
                + "WHERE d.IdDetailPinjam=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idDetail);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }
            Detail d = new Detail();
            d.idDetail = rs.getString("IdDetailPinjam");
            d.idPinjam = rs.getString("IdPinjam");
            d.kodeFilm = rs.getString("KodeFilm");
            d.judul = rs.getString("Judul");
            d.idAnggota = rs.getString("IDAnggota");
            d.nama = rs.getString("nama");
            d.tglPinjam = rs.getDate("tgl_pinjam");
            d.tglKembali = rs.getDate("tgl_kembali");
            d.kembali = rs.getInt("status") == 1;
            d.denda = rs.getInt("denda");
            return d;
        }
    }

    /** Menjumlahkan denda semua film pada satu ID Pinjam ke pinjam.TotalDenda. */
    private void perbaruiTotalDenda(Connection c, String idPinjam) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "UPDATE pinjam SET TotalDenda=(SELECT COALESCE(SUM(denda),0) "
                + "FROM detail_pinjam WHERE IdPinjam=?) WHERE IDPinjam=?")) {
            ps.setString(1, idPinjam);
            ps.setString(2, idPinjam);
            ps.executeUpdate();
        }
    }

    private String idDetailTerpilih() {
        String id = txtIDDetailTransaksi.getText();
        if (id == null || id.trim().isEmpty() || id.trim().equals("-")) {
            JOptionPane.showMessageDialog(this, "Pilih dulu data peminjaman dengan "
                    + "klik salah satu baris pada tabel Detail Peminjaman");
            return null;
        }
        return id.trim();
    }

    /** Tombol Kembalikan: film dikembalikan, denda keterlambatan dihitung dan disimpan. */
    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {
        String idDetail = idDetailTerpilih();
        if (idDetail == null) {
            return;
        }
        ConfigDB db = new ConfigDB();
        if (db.koneksi == null) {
            return;
        }
        Connection c = db.koneksi;
        try {
            Detail d = ambilDetail(c, idDetail);
            if (d == null) {
                JOptionPane.showMessageDialog(this, "Data peminjaman tidak ditemukan. "
                        + "Klik Refresh lalu pilih ulang barisnya");
                return;
            }
            if (d.kembali) {
                JOptionPane.showMessageDialog(this, "Film \"" + d.judul
                        + "\" sudah dikembalikan sebelumnya");
                return;
            }
            long telat = hariTerlambat(d.tglKembali);
            long dendaTelat = telat * DENDA_PER_HARI;
            long total = d.denda + dendaTelat;

            StringBuilder ringkas = new StringBuilder();
            ringkas.append("Anggota : ").append(d.nama).append(" (").append(d.idAnggota).append(")\n")
                    .append("Film    : ").append(d.judul).append("\n")
                    .append("Jatuh tempo : ").append(d.tglKembali).append("\n")
                    .append("Terlambat   : ").append(telat).append(" hari\n");
            if (d.denda > 0) {
                ringkas.append("Denda sebelumnya : ").append(rupiah(d.denda)).append("\n");
            }
            ringkas.append("Denda terlambat  : ").append(rupiah(dendaTelat)).append("\n")
                    .append("TOTAL DENDA      : ").append(rupiah(total))
                    .append("\n\nProses pengembalian film ini?");
            int jawab = JOptionPane.showConfirmDialog(this, ringkas.toString(),
                    "Konfirmasi Pengembalian", JOptionPane.YES_NO_OPTION);
            if (jawab != JOptionPane.YES_OPTION) {
                return;
            }

            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(
                    "UPDATE detail_pinjam SET status=1, keterangan='Kembali', denda=? "
                    + "WHERE IdDetailPinjam=?")) {
                ps.setLong(1, total);
                ps.setString(2, idDetail);
                ps.executeUpdate();
            }
            // film kembali -> stok bertambah lagi
            try (PreparedStatement psS = c.prepareStatement(
                    "UPDATE film SET stok = stok + 1 WHERE KodeFilm=?")) {
                psS.setString(1, d.kodeFilm);
                psS.executeUpdate();
            }
            perbaruiTotalDenda(c, d.idPinjam);
            c.commit();

            JOptionPane.showMessageDialog(this, total > 0
                    ? "Film dikembalikan.\nDenda yang harus dibayar: " + rupiah(total)
                    : "Film dikembalikan tepat waktu. Tidak ada denda.");
            refreshTabelPinjam();
            kosongkanPilihan();
            tawarkanBayar(d.idAnggota, total);
        } catch (SQLException e) {
            batal(c);
            JOptionPane.showMessageDialog(this, "Pengembalian gagal diproses:\n" + e.getMessage(),
                    "Kesalahan", JOptionPane.WARNING_MESSAGE);
        } finally {
            tutup(db);
        }
    }

    /**
     * Tombol Perpanjang: jatuh tempo digeser. Jika sudah terlambat, denda sampai
     * hari ini ditagihkan dulu (ditambahkan ke denda) lalu masa pinjam dihitung
     * mulai hari ini; jika belum terlambat, jatuh tempo lama yang diperpanjang.
     */
    private void btnPerpanjangActionPerformed(java.awt.event.ActionEvent evt) {
        String idDetail = idDetailTerpilih();
        if (idDetail == null) {
            return;
        }
        ConfigDB db = new ConfigDB();
        if (db.koneksi == null) {
            return;
        }
        Connection c = db.koneksi;
        try {
            Detail d = ambilDetail(c, idDetail);
            if (d == null) {
                JOptionPane.showMessageDialog(this, "Data peminjaman tidak ditemukan. "
                        + "Klik Refresh lalu pilih ulang barisnya");
                return;
            }
            if (d.kembali) {
                JOptionPane.showMessageDialog(this, "Film \"" + d.judul + "\" sudah "
                        + "dikembalikan, tidak bisa diperpanjang");
                return;
            }
            String isi = JOptionPane.showInputDialog(this,
                    "Perpanjang \"" + d.judul + "\" berapa hari? (1 - "
                    + PERPANJANG_HARI_MAKS + ")", String.valueOf(PERPANJANG_HARI_AWAL));
            if (isi == null) {
                return;
            }
            int hari;
            try {
                hari = Integer.parseInt(isi.trim());
            } catch (NumberFormatException e) {
                hari = -1;
            }
            if (hari < 1 || hari > PERPANJANG_HARI_MAKS) {
                JOptionPane.showMessageDialog(this, "Lama perpanjangan harus berupa "
                        + "angka 1 sampai " + PERPANJANG_HARI_MAKS + " hari");
                return;
            }

            long telat = hariTerlambat(d.tglKembali);
            long dendaTelat = telat * DENDA_PER_HARI;
            java.time.LocalDate hariIni = java.time.LocalDate.now();
            java.time.LocalDate dasar = telat > 0 ? hariIni : d.tglKembali.toLocalDate();
            java.sql.Date jatuhTempoBaru = java.sql.Date.valueOf(dasar.plusDays(hari));
            long dendaBaru = d.denda + dendaTelat;

            StringBuilder ringkas = new StringBuilder();
            ringkas.append("Anggota : ").append(d.nama).append(" (").append(d.idAnggota).append(")\n")
                    .append("Film    : ").append(d.judul).append("\n")
                    .append("Jatuh tempo lama : ").append(d.tglKembali).append("\n")
                    .append("Jatuh tempo baru : ").append(jatuhTempoBaru)
                    .append("  (+").append(hari).append(" hari)\n");
            if (telat > 0) {
                ringkas.append("Terlambat ").append(telat).append(" hari, denda ditagih sekarang: ")
                        .append(rupiah(dendaTelat)).append("\n");
            }
            ringkas.append("Total denda tercatat: ").append(rupiah(dendaBaru))
                    .append("\n\nProses perpanjangan?");
            int jawab = JOptionPane.showConfirmDialog(this, ringkas.toString(),
                    "Konfirmasi Perpanjangan", JOptionPane.YES_NO_OPTION);
            if (jawab != JOptionPane.YES_OPTION) {
                return;
            }

            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(
                    "UPDATE detail_pinjam SET tgl_kembali=?, status=0, "
                    + "keterangan='Perpanjang', denda=? WHERE IdDetailPinjam=?")) {
                ps.setDate(1, jatuhTempoBaru);
                ps.setLong(2, dendaBaru);
                ps.setString(3, idDetail);
                ps.executeUpdate();
            }
            perbaruiTotalDenda(c, d.idPinjam);
            c.commit();

            JOptionPane.showMessageDialog(this, "Peminjaman diperpanjang sampai "
                    + jatuhTempoBaru + (dendaTelat > 0
                    ? ".\nDenda keterlambatan yang ditagih: " + rupiah(dendaTelat) : "."));
            refreshTabelPinjam();
            kosongkanPilihan();
            tawarkanBayar(d.idAnggota, dendaTelat);
        } catch (SQLException e) {
            batal(c);
            JOptionPane.showMessageDialog(this, "Perpanjangan gagal diproses:\n" + e.getMessage(),
                    "Kesalahan", JOptionPane.WARNING_MESSAGE);
        } finally {
            tutup(db);
        }
    }

    /** Setelah ada denda, tawarkan langsung membuka form Pembayaran Denda. */
    private void tawarkanBayar(String idAnggota, long denda) {
        if (denda <= 0) {
            return;
        }
        int jawab = JOptionPane.showConfirmDialog(this, "Ada denda " + rupiah(denda)
                + ".\nBuka form Pembayaran Denda sekarang?", "Pembayaran Denda",
                JOptionPane.YES_NO_OPTION);
        if (jawab == JOptionPane.YES_OPTION) {
            new formBayarDenda(idAnggota).setVisible(true);
        }
    }

    private void kosongkanPilihan() {
        txtIDDetailTransaksi.setText("-");
        txtDenda.setText("0");
        txtKodeFilm.setText(null);
        txtJudul.setText(null);
        txtIDPinjam.setText(null);
        txtIDAnggota.setText(null);
        txtNamaAnggota.setText(null);
        txtTglPinjam.setDate(null);
        txtTglKembali.setDate(null);
        autonomer();
    }

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {                                        
        new formView().setVisible(true);
    }                                       

    
    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {                                           
        new ConfigDB().tampilTabel(judulPinjam, SQLPinjam, JTablePinjam);
        new ConfigDB().aturLebarKolom(JTablePinjam, LebarPinjam);
    }                                          

   
    private void JTablePinjamMouseClicked(java.awt.event.MouseEvent evt) {                                          
        try {
            int i = JTablePinjam.getSelectedRow();
            txtIDDetailTransaksi.setText(String.valueOf(JTablePinjam.getValueAt(i, 0)));
            txtIDPinjam.setText(String.valueOf(JTablePinjam.getValueAt(i, 1)));
            txtKodeFilm.setText(String.valueOf(JTablePinjam.getValueAt(i, 2)));
            cmbKet.setSelectedItem(String.valueOf(JTablePinjam.getValueAt(i, 6)));
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            txtTglPinjam.setDate(dt.parse(String.valueOf(JTablePinjam.getValueAt(i, 3))));
            txtTglKembali.setDate(dt.parse(String.valueOf(JTablePinjam.getValueAt(i, 4))));
            txtDenda.setText(String.valueOf(JTablePinjam.getValueAt(i, 7)));
            hitungEstimasiDenda();
            Statement st = new ConfigDB().koneksi.createStatement();
            ResultSet rs = st.executeQuery("SELECT*FROM pinjam WHERE IDPinjam='" + txtIDPinjam.getText() + "'");
            if (rs.next()) {
                txtIDAnggota.setText(rs.getString("IDAnggota"));
            }
            ResultSet ra = st.executeQuery("SELECT*FROM anggota WHERE IDAnggota='" + txtIDAnggota.getText() + "'");
            if (ra.next()) {
                txtNamaAnggota.setText(ra.getString("nama"));
            }
            ResultSet rk = st.executeQuery("SELECT*FROM film WHERE KodeFilm='" + txtKodeFilm.getText() + "'");
            if (rk.next()) {
                txtJudul.setText(rk.getString("judul"));
            }
        } catch (Exception e) {
        }
    }                                         

    /** Menampilkan denda saat ini pada baris terpilih (denda tercatat + terlambat hari ini). */
    private void hitungEstimasiDenda() {
        ConfigDB db = new ConfigDB();
        if (db.koneksi == null) {
            return;
        }
        try {
            Detail d = ambilDetail(db.koneksi, txtIDDetailTransaksi.getText().trim());
            if (d != null) {
                long denda = d.denda + (d.kembali ? 0 : hariTerlambat(d.tglKembali) * DENDA_PER_HARI);
                txtDenda.setText(String.valueOf(denda));
            }
        } catch (SQLException e) {
        } finally {
            tutup(db);
        }
    }

    private void cmbKetKeyPressed(java.awt.event.KeyEvent evt) {                                  
        // TODO add your handling code here:
    }                                 

    private void cmbKetAncestorRemoved(javax.swing.event.AncestorEvent evt) {                                       
        // TODO add your handling code here:
    }                                      

    private void txtIDPinjamActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new formTransaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JTable JTableList;
    private javax.swing.JTable JTablePinjam;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnPerpanjang;
    private javax.swing.JButton btnPinjam;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnView;
    private javax.swing.JComboBox cmbKet;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtDenda;
    private javax.swing.JTextField txtIDAnggota;
    private java.awt.Label txtIDDetailTransaksi;
    private javax.swing.JTextField txtIDPinjam;
    private javax.swing.JTextField txtJudul;
    private javax.swing.JTextField txtKodeFilm;
    private javax.swing.JTextField txtNamaAnggota;
    private com.toedter.calendar.JDateChooser txtTglKembali;
    private com.toedter.calendar.JDateChooser txtTglPinjam;
    // End of variables declaration                   
}
