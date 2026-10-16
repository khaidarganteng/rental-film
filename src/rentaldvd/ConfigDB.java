package rentaldvd;

import java.sql.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.io.File;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ConfigDB {

    private String Password = "";
    private String Username = "root";
    private String Database = "dbfilm";

    public Connection koneksi;

    public ConfigDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            koneksi = DriverManager.getConnection("jdbc:mysql://localhost:3306/"
                    + this.Database + "?useSSL=false&allowPublicKeyRetrieval=true",
                    this.Username, this.Password);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, ""
                    + "Maaf Terjadi Kesalahan pada Bagian : [" + e.toString() + "]");
        }
    }

    public Object[][] isiTabel(String SQL, int jumlah) {
        Object[][] data = null;
        try {
            Statement st = ConfigDB.this.koneksi.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(SQL);
            rs.last();
            int baris = rs.getRow();
            rs.beforeFirst();
            int j = 0;
            data = new Object[baris][jumlah];
            while (rs.next()) {
                for (int i = 0; i < jumlah; i++) {
                    data[j][i] = rs.getString(i + 1);
                }
                j++;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, ""
                    + "Maaf Terjadi Kesalahan pada  bagian method isiTable : ["
                    + e.toString() + "]");
        }
        return data;
    }

    public void tampilTabel(String Judul[], String SQL, JTable Tabel) {
        try {
            String title[] = Judul;
            int jum = title.length;
            Tabel.setModel(new DefaultTableModel(isiTabel(SQL, jum), title));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, ""
                    + "Maaf Terjadi Kesalahan pada  bagian method tampilTabel : ["
                    + e.toString() + "]");
        }
    }

    public void aturLebarKolom(JTable tabel, int baris[]) {
        try {
            int getBaris[] = baris;
            int JumlahBaris = getBaris.length;
            tabel.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            for (int i = 0; i < JumlahBaris - 1; i++) {
                tabel.getColumnModel().getColumn(i).setPreferredWidth(getBaris[i]);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, ""
                    + "Maaf Terjadi Kesalahan pada bagian method aturLebarKolom : ["
                    + e.toString() + "]");
        }
    }

    public void simpanData(String SQL) {
        try {
            Statement st = ConfigDB.this.koneksi.createStatement();
            st.execute(SQL);
            st.close();
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, ""
                    + "Maaf  Terjadi  Kesalahan pada bagian method simpanData : ["
                    + e.toString() + "]");
        }
    }

    public void ubahData(String SQL) {
        try {
            Statement st = ConfigDB.this.koneksi.createStatement();
            st.execute(SQL);
            st.close();
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, ""
                    + "Maaf Terjadi Kesalahan pada bagian method ubahData : ["
                    + e.toString() + "]");
        }
    }

    public void hapusData(String SQL) {
        try {
            Statement st = ConfigDB.this.koneksi.createStatement();
            st.execute(SQL);
            st.close();
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, ""
                    + "Maaf  Terjadi  Kesalahan  pada  bagian method hapusData : ["
                    + e.toString() + "]");
        }
    }

    public boolean duplikasiData(String tabel, String Key, String nilai) {
        boolean ada = false;
        try {
            Statement st = ConfigDB.this.koneksi.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM " + tabel + " "
                    + "WHERE " + Key + "='" + nilai + "'");
            if (rs.next()) {
                ada = true;
            } else {
                ada = false;
            }
        } catch (SQLException sqe) {
        }
        return ada;
    }

    public String generateKode(String tabel, String kolom, String prefix, int digit) {
        String kodeBaru = prefix + String.format("%0" + digit + "d", 1);
        try {
            Statement st = ConfigDB.this.koneksi.createStatement();
            String SQL = "SELECT MAX(CAST(SUBSTRING(" + kolom + ", "
                    + (prefix.length() + 1) + ") AS UNSIGNED)) AS urut FROM "
                    + tabel + " WHERE " + kolom + " LIKE '" + prefix + "%'";
            ResultSet rs = st.executeQuery(SQL);
            int urut = 1;
            if (rs.next() && rs.getString("urut") != null) {
                urut = Integer.parseInt(rs.getString("urut")) + 1;
            }
            kodeBaru = prefix + String.format("%0" + digit + "d", urut);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, ""
                    + "Maaf Terjadi Kesalahan pada bagian method generateKode : ["
                    + e.toString() + "]");
        }
        return kodeBaru;
    }

    public void cariData(String Judul[], String Cari, JTable Tabel) {
        try {
            Statement st = ConfigDB.this.koneksi.createStatement();
            String title[] = Judul;
            int jum = title.length;
            Tabel.setModel(new DefaultTableModel(isiTabel(Cari, jum), title));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, ""
                    + "Maaf  Terjadi  Kesalahan  pada  bagian method cariData : ["
                    + e.toString() + "]");
        }
    }

    public void tampilLaporan(String laporanFile, String SQL) {
        try {
            File file = new File(laporanFile);
            JasperDesign jasDes = JRXmlLoader.load(file);
            JRDesignQuery sqlQuery = new JRDesignQuery();
            sqlQuery.setText(SQL);
            jasDes.setQuery(sqlQuery);
            JasperReport JR = JasperCompileManager.compileReport(jasDes);
            JasperPrint JP = JasperFillManager.fillReport
                    (JR, null, ConfigDB.this.koneksi);
            JasperViewer.viewReport(JP);
                } catch (Exception e) {
            e.printStackTrace();
            Throwable t = e;
            while (t.getCause() != null) {
                t = t.getCause();
            }
            JOptionPane.showMessageDialog(null, ""
                    + "Maaf  Terjadi Kesalahan pada bagian method TampilLaporan : ["
                    + e.toString() + "]\n\nPenyebab asli: " + t.toString());
        }
    }

    public int jumlahRecord(String SQL) {
        int hasil = 0;
        int i = 0;
        try {
            Statement st = ConfigDB.this.koneksi.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                i++;
            }
            hasil = i;
               } catch (Exception e) {
            JOptionPane.showMessageDialog(null, ""
                    + "Maaf Terjadi Kesalahan  pada bagian method jumlahRecord : ["
                    + e.toString() + "]");
        }
        return hasil;
    }
}