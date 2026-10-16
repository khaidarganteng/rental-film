package rentaldvd;

/**
 * 
 * 
 * 
 */
public class frmUtama extends javax.swing.JFrame {

    String judulKolom[] = {"Kode Film", "Judul", "Genre", "Tahun", "Asal", "Stok"};
    String sqlFilm = "SELECT*FROM film";
    int lebarKolom[] = {100, 300, 100, 60, 200, 60};

    /**
     * Creates new form frmUtama
     */
    public frmUtama() {
        initComponents();
        Tema.terapkan(this);
        // tombol menu: data master = ungu, laporan = hijau-biru
        java.awt.Color ungu = new java.awt.Color(98, 58, 190);
        java.awt.Color tosca = new java.awt.Color(13, 130, 120);
        Tema.gayaTombol(btnFilm, ungu);
        Tema.gayaTombol(btnAnggota, ungu);
        Tema.gayaTombol(btnTransaksi, ungu);
        Tema.gayaTombol(btnPinjam, ungu);
        Tema.gayaTombol(btnDenda, ungu);
        Tema.gayaTombol(btnLapFilm, tosca);
        Tema.gayaTombol(btnLapAnggota, tosca);
        Tema.gayaTombol(btnLapPinjam, tosca);
        // hak akses: User tidak boleh membuka Master Anggota, Pembayaran Denda, Laporan Anggota
        if (!Sesi.isAdmin()) {
            btnAnggota.setEnabled(false);
            btnDenda.setEnabled(false);
            btnLapAnggota.setEnabled(false);
            btnAnggota.setToolTipText("Hanya untuk Admin");
            btnDenda.setToolTipText("Hanya untuk Admin");
            btnLapAnggota.setToolTipText("Hanya untuk Admin");
        }
        jLabel1.setForeground(new java.awt.Color(255, 110, 135));
        jLabel1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
        this.setLocationRelativeTo(null);
        new ConfigDB().tampilTabel(judulKolom, sqlFilm, jTable1);
        new ConfigDB().aturLebarKolom(jTable1, lebarKolom);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btnFilm = new javax.swing.JButton();
        btnAnggota = new javax.swing.JButton();
        btnTransaksi = new javax.swing.JButton();
        btnPinjam = new javax.swing.JButton();
        btnLapAnggota = new javax.swing.JButton();
        btnLapPinjam = new javax.swing.JButton();
        btnLapFilm = new javax.swing.JButton();
        btnDenda = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sistem Informasi Rental VCD Sederhana");

        jLabel1.setText("SISTEM INFORMASI RENTAL VCD SEDERHANA");
        jLabel1.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 16));

        btnFilm.setText("Master Film");
        btnFilm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilmActionPerformed(evt);
            }
        });

        btnAnggota.setText("Master Anggota");
        btnAnggota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnggotaActionPerformed(evt);
            }
        });

        btnTransaksi.setText("Transaksi Peminjaman");
        btnTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransaksiActionPerformed(evt);
            }
        });

        btnPinjam.setText("Detail Peminjaman");
        btnPinjam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPinjamActionPerformed(evt);
            }
        });

        btnLapAnggota.setText("Laporan Anggota");
        btnLapAnggota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLapAnggotaActionPerformed(evt);
            }
        });

        btnLapPinjam.setText("Laporan Peminjaman");
        btnLapPinjam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLapPinjamActionPerformed(evt);
            }
        });

        btnLapFilm.setText("Laporan Film");
        btnLapFilm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLapFilmActionPerformed(evt);
            }
        });

        btnDenda.setText("Pembayaran Denda");
        btnDenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDendaActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"Kode Film", "Judul", "Genre", "Tahun", "Asal", "Stok"}
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnFilm, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(btnAnggota, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(btnTransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(btnPinjam, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(btnDenda, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(btnLapFilm, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(btnLapAnggota, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(btnLapPinjam, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnFilm)
                        .addGap(6, 6, 6)
                        .addComponent(btnAnggota)
                        .addGap(6, 6, 6)
                        .addComponent(btnTransaksi)
                        .addGap(6, 6, 6)
                        .addComponent(btnPinjam)
                        .addGap(6, 6, 6)
                        .addComponent(btnDenda)
                        .addGap(24, 24, 24)
                        .addComponent(btnLapFilm)
                        .addGap(6, 6, 6)
                        .addComponent(btnLapAnggota)
                        .addGap(6, 6, 6)
                        .addComponent(btnLapPinjam))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }

    private void btnFilmActionPerformed(java.awt.event.ActionEvent evt) {
        new formFilm().setVisible(true);
    }

    private void btnAnggotaActionPerformed(java.awt.event.ActionEvent evt) {
        new formAnggota().setVisible(true);
    }

    private void btnTransaksiActionPerformed(java.awt.event.ActionEvent evt) {
        new formTransaksi().setVisible(true);
    }

    private void btnPinjamActionPerformed(java.awt.event.ActionEvent evt) {
        new formView().setVisible(true);
    }

    private void btnLapAnggotaActionPerformed(java.awt.event.ActionEvent evt) {
        new formLapAnggota().setVisible(true);
    }

    private void btnDendaActionPerformed(java.awt.event.ActionEvent evt) {
        new formBayarDenda().setVisible(true);
    }

    private void btnLapFilmActionPerformed(java.awt.event.ActionEvent evt) {
        new formLapFilm().setVisible(true);
    }

    private void btnLapPinjamActionPerformed(java.awt.event.ActionEvent evt) {
        new formLapPinjam().setVisible(true);
    }

    private javax.swing.JButton btnDenda;
    private javax.swing.JButton btnFilm;
    private javax.swing.JButton btnAnggota;
    private javax.swing.JButton btnTransaksi;
    private javax.swing.JButton btnPinjam;
    private javax.swing.JButton btnLapAnggota;
    private javax.swing.JButton btnLapPinjam;
    private javax.swing.JButton btnLapFilm;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
}
