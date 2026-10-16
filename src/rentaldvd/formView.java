package rentaldvd;

/**
 * Form View Detail Peminjaman.
 *
 * CATATAN: Modul "Praktikum Java Database iReport - Rental Bagian 4" yang
 * diupload memanggil "new formView().setVisible(true);" pada tombol
 * "View Detail Peminjaman" di formTransaksi, tetapi pembuatan formView itu
 * sendiri belum dijelaskan di bagian ini (kemungkinan ada di modul
 * Bagian 5 / bagian laporan JasperReport). Supaya project tetap bisa
 * di-compile dan dijalankan sekarang, dibuatkan versi sederhana di sin
 */
public class formView extends javax.swing.JFrame {

    String judulView[] = {"ID Pinjam", "ID Anggota", "Nama", "Kode Film",
        "Judul", "Tgl. Pinjam", "Tgl. Kembali", "Status", "Keterangan", "Denda"};
    int lebarView[] = {80, 80, 150, 80, 200, 100, 100, 60, 100, 80};
    String sqlView = "SELECT * FROM view_pinjam ORDER BY IDPinjam DESC";

    /**
     * Creates new form formView
     */
    public formView() {
        initComponents();
        TombolKembali.pasang(this);
        Tema.terapkan(this);
        this.setLocationRelativeTo(null);
        new ConfigDB().tampilTabel(judulView, sqlView, JTableView);
        new ConfigDB().aturLebarKolom(JTableView, lebarView);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        JTableView = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Detail Peminjaman");

        JTableView.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"ID Pinjam", "ID Anggota", "Nama", "Kode Film",
                    "Judul", "Tgl. Pinjam", "Tgl. Kembali", "Status",
                    "Keterangan", "Denda"}
        ));
        jScrollPane1.setViewportView(JTableView);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new formView().setVisible(true);
            }
        });
    }

    private javax.swing.JTable JTableView;
    private javax.swing.JScrollPane jScrollPane1;
}
