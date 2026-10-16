package rentaldvd;

/**
 * Form cetak Laporan Detail Peminjaman (lihat modul Bagian 5, bab 9).
 * Sama seperti formLapAnggota, ditulis pure-Java (tanpa .form) agar aman
 * dikompilasi. Bisa dipasangkan file .form lewat GUI Builder NetBeans kalau
 * mau, dengan komponen: jRadioButton1, jRadioButton2, txtIDAnggota,
 * btnCetak, btnKeluar.
 */
public class formLapPinjam extends javax.swing.JFrame {

    /**
     * Creates new form formLapPinjam
     */
    public formLapPinjam() {
        initComponents();
        Tema.terapkan(this);
        this.setLocationRelativeTo(this);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        txtIDAnggota = new javax.swing.JTextField();
        btnCetak = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cetak Laporan Detail Peminjaman");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Semua Data");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("ID Anggota");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        btnCetak.setText("Preview");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });

        btnKeluar.setText("Keluar");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(txtIDAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCetak)
                        .addGap(18, 18, 18)
                        .addComponent(btnKeluar)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jRadioButton1)
                .addGap(12, 12, 12)
                .addComponent(jRadioButton2)
                .addGap(6, 6, 6)
                .addComponent(txtIDAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCetak)
                    .addComponent(btnKeluar))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        jRadioButton2.setSelected(false);
    }

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        jRadioButton1.setSelected(false);
    }

    /** Query laporan peminjaman: sudah menyertakan nama peminjam dan judul film. */
    private static final String SQL_LAPORAN_PINJAM
            = "SELECT d.IdPinjam AS IDPinjam, p.IDAnggota, a.nama AS nama_peminjam, "
            + "d.KodeFilm, f.Judul, d.tgl_pinjam, d.tgl_kembali "
            + "FROM detail_pinjam d "
            + "INNER JOIN pinjam p ON p.IDPinjam = d.IdPinjam "
            + "INNER JOIN anggota a ON a.IDAnggota = p.IDAnggota "
            + "LEFT JOIN film f ON f.KodeFilm = d.KodeFilm ";

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (jRadioButton1.isSelected()) {
                new ConfigDB().tampilLaporan("src/laporan/Pinjam.jrxml",
                        SQL_LAPORAN_PINJAM + "ORDER BY p.IDAnggota ASC, d.IdPinjam ASC");
            } else {
                String id = txtIDAnggota.getText().trim().replace("'", "''");
                new ConfigDB().tampilLaporan("src/laporan/Pinjam.jrxml",
                        SQL_LAPORAN_PINJAM + "WHERE p.IDAnggota='" + id
                        + "' ORDER BY d.IdPinjam ASC");
            }
        } catch (Exception e) {
        }
    }

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JTextField txtIDAnggota;
}
