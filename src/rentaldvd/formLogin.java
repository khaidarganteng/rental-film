package rentaldvd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Form Login - tampil pertama kali sebelum menu utama (frmUtama).
 * Data pengguna dibaca dari tabel "pengguna" di database dbfilm.
 * Password disimpan dalam bentuk MD5 (dicek dengan fungsi MD5() MySQL).
 *
 * Tampilan: tema gelap bergaya bioskop (ungu - merah) dengan panel
 * kiri bergradasi + ikon pita film, dan kartu form di sebelah kanan.
 * Logika login (prosesLogin) tidak diubah.
 */
public class formLogin extends javax.swing.JFrame {

    private static final int MAKS_PERCOBAAN = 3;
    private int percobaan = 0;

    // ---------- palet warna ----------
    private static final Color WARNA_BG     = new Color(20, 18, 31);
    private static final Color WARNA_FIELD  = new Color(32, 29, 48);
    private static final Color WARNA_GARIS  = new Color(60, 55, 85);
    private static final Color WARNA_AKSEN  = new Color(229, 56, 79);
    private static final Color WARNA_TEKS   = new Color(240, 238, 250);
    private static final Color WARNA_REDUP  = new Color(150, 146, 175);

    public formLogin() {
        initComponents();
        this.setLocationRelativeTo(null);
        getRootPane().setDefaultButton(btnLogin); // tekan Enter = Login
    }

    // ---------- panel kiri: gradasi + ikon pita film ----------
    private static class PanelKiri extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            g2.setPaint(new GradientPaint(0, 0, new Color(76, 29, 149), w, h, new Color(225, 50, 90)));
            g2.fillRect(0, 0, w, h);

            // lingkaran dekoratif transparan
            g2.setColor(new Color(255, 255, 255, 18));
            g2.fillOval(-70, h - 170, 260, 260);
            g2.fillOval(w - 120, -80, 220, 220);
            g2.fillOval(w - 70, h / 2, 120, 120);

            // ikon pita film (reel)
            int d = 130;
            int cx = w / 2;
            int cy = 135;
            g2.setColor(new Color(255, 255, 255, 235));
            g2.fillOval(cx - d / 2, cy - d / 2, d, d);
            g2.setColor(new Color(108, 36, 150));
            for (int i = 0; i < 6; i++) {
                double a = i * Math.PI / 3;
                int hx = cx + (int) Math.round(Math.cos(a) * 38);
                int hy = cy + (int) Math.round(Math.sin(a) * 38);
                g2.fillOval(hx - 12, hy - 12, 24, 24);
            }
            g2.fillOval(cx - 9, cy - 9, 18, 18);
            g2.dispose();
        }
    }

    // ---------- tombol membulat dengan efek hover ----------
    private static class TombolBulat extends JButton {
        private final Color warna;
        private boolean hover = false;

        TombolBulat(String teks, Color warna) {
            super(teks);
            this.warna = warna;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color c = warna;
            if (getModel().isPressed()) {
                c = warna.darker();
            } else if (hover) {
                c = warna.brighter();
            }
            g2.setColor(c);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ---------- border membulat untuk kolom isian ----------
    private static class BorderBulat implements Border {
        private final Color warna;

        BorderBulat(Color warna) {
            this.warna = warna;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(warna);
            g2.drawRoundRect(x, y, w - 1, h - 1, 14, 14);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(6, 14, 6, 14);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    private static void isiBulat(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(WARNA_FIELD);
        g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 14, 14);
        g2.dispose();
    }

    private void gayaField(javax.swing.text.JTextComponent f) {
        f.setOpaque(false);
        f.setForeground(WARNA_TEKS);
        f.setCaretColor(WARNA_TEKS);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(new BorderBulat(WARNA_GARIS));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setPreferredSize(new Dimension(300, 40));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        f.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                f.setBorder(new BorderBulat(WARNA_AKSEN));
            }

            @Override
            public void focusLost(FocusEvent e) {
                f.setBorder(new BorderBulat(WARNA_GARIS));
            }
        });
    }

    private void gayaLabel(javax.swing.JLabel l, int ukuran, boolean tebal, Color warna) {
        l.setFont(new Font("Segoe UI", tebal ? Font.BOLD : Font.PLAIN, ukuran));
        l.setForeground(warna);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        pnlHeader = new PanelKiri();
        lblJudul = new javax.swing.JLabel();
        lblSub = new javax.swing.JLabel();
        lblTag = new javax.swing.JLabel();
        pnlForm = new JPanel();
        lblSelamat = new javax.swing.JLabel();
        lblPetunjuk = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        lblPass = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                isiBulat(g, this);
                super.paintComponent(g);
            }
        };
        txtPassword = new javax.swing.JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                isiBulat(g, this);
                super.paintComponent(g);
            }
        };
        chkLihat = new javax.swing.JCheckBox();
        btnLogin = new TombolBulat("LOGIN", WARNA_AKSEN);
        btnKeluar = new TombolBulat("KELUAR", new Color(52, 48, 76));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login - Rental VCD");
        setResizable(false);

        // ---------- panel kiri (branding) ----------
        pnlHeader.setPreferredSize(new Dimension(300, 440));
        pnlHeader.setLayout(new BorderLayout());

        JPanel teksKiri = new JPanel();
        teksKiri.setOpaque(false);
        teksKiri.setLayout(new BoxLayout(teksKiri, BoxLayout.Y_AXIS));
        teksKiri.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 26, 44, 26));

        lblJudul.setText("RENTAL VCD");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblJudul.setForeground(Color.WHITE);
        lblJudul.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblSub.setText("Sistem Informasi Rental VCD");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(255, 255, 255, 215));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTag.setText("Kelola film, anggota & peminjaman dengan mudah");
        lblTag.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblTag.setForeground(new Color(255, 255, 255, 170));
        lblTag.setAlignmentX(Component.CENTER_ALIGNMENT);

        teksKiri.add(lblJudul);
        teksKiri.add(Box.createVerticalStrut(4));
        teksKiri.add(lblSub);
        teksKiri.add(Box.createVerticalStrut(10));
        teksKiri.add(lblTag);
        pnlHeader.add(teksKiri, BorderLayout.SOUTH);

        // ---------- panel kanan (form) ----------
        pnlForm.setBackground(WARNA_BG);
        pnlForm.setPreferredSize(new Dimension(420, 440));
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBorder(javax.swing.BorderFactory.createEmptyBorder(48, 44, 32, 44));

        lblSelamat.setText("Selamat Datang");
        gayaLabel(lblSelamat, 26, true, WARNA_TEKS);
        lblPetunjuk.setText("Silakan login untuk melanjutkan");
        gayaLabel(lblPetunjuk, 12, false, WARNA_REDUP);

        lblUser.setText("Username");
        gayaLabel(lblUser, 12, true, WARNA_REDUP);
        lblPass.setText("Password");
        gayaLabel(lblPass, 12, true, WARNA_REDUP);

        gayaField(txtUsername);
        gayaField(txtPassword);
        txtPassword.setEchoChar('\u2022');

        chkLihat.setText("Tampilkan password");
        chkLihat.setOpaque(false);
        chkLihat.setForeground(WARNA_REDUP);
        chkLihat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkLihat.setFocusPainted(false);
        chkLihat.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkLihat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLihatActionPerformed(evt);
            }
        });

        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        JPanel barisTombol = new JPanel(new GridLayout(1, 2, 12, 0));
        barisTombol.setOpaque(false);
        barisTombol.setAlignmentX(Component.LEFT_ALIGNMENT);
        barisTombol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        barisTombol.setPreferredSize(new Dimension(300, 42));
        barisTombol.add(btnLogin);
        barisTombol.add(btnKeluar);

        pnlForm.add(lblSelamat);
        pnlForm.add(Box.createVerticalStrut(4));
        pnlForm.add(lblPetunjuk);
        pnlForm.add(Box.createVerticalStrut(30));
        pnlForm.add(lblUser);
        pnlForm.add(Box.createVerticalStrut(6));
        pnlForm.add(txtUsername);
        pnlForm.add(Box.createVerticalStrut(16));
        pnlForm.add(lblPass);
        pnlForm.add(Box.createVerticalStrut(6));
        pnlForm.add(txtPassword);
        pnlForm.add(Box.createVerticalStrut(8));
        pnlForm.add(chkLihat);
        pnlForm.add(Box.createVerticalStrut(24));
        pnlForm.add(barisTombol);
        pnlForm.add(Box.createVerticalGlue());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(WARNA_BG);
        getContentPane().add(pnlHeader, BorderLayout.WEST);
        getContentPane().add(pnlForm, BorderLayout.CENTER);

        pack();
    }

    private void chkLihatActionPerformed(java.awt.event.ActionEvent evt) {
        // char 0 = tampilkan teks asli, bullet = sembunyikan
        txtPassword.setEchoChar(chkLihat.isSelected() ? (char) 0 : '•');
    }

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {
        prosesLogin();
    }

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private void prosesLogin() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password harus diisi!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ConfigDB db = new ConfigDB();
        if (db.koneksi == null) {
            return; // pesan kesalahan koneksi sudah ditampilkan oleh ConfigDB
        }

        String sql = "SELECT nama_lengkap, role FROM pengguna WHERE username=? AND password=MD5(?)";
        try (PreparedStatement ps = db.koneksi.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nama = rs.getString("nama_lengkap");
                    String role = rs.getString("role");
                    Sesi.mulai(user, nama, role);
                    db.koneksi.close();
                    JOptionPane.showMessageDialog(this,
                            "Login berhasil!\nSelamat datang, " + nama + " (" + Sesi.labelRole() + ").",
                            "Login Berhasil", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    frmUtama utama = new frmUtama();
                    utama.setTitle("Sistem Informasi Rental VCD - " + nama + " (" + Sesi.labelRole() + ")");
                    utama.setVisible(true);
                    return;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal memeriksa login. Pastikan tabel 'pengguna' sudah dibuat "
                    + "(jalankan sql/user_role.sql).\n\n" + e.getMessage(),
                    "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // login gagal
        percobaan++;
        if (percobaan >= MAKS_PERCOBAAN) {
            JOptionPane.showMessageDialog(this,
                    "Login gagal " + MAKS_PERCOBAAN + " kali. Aplikasi akan ditutup.",
                    "Akses Ditolak", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        JOptionPane.showMessageDialog(this,
                "Username atau password salah!\nSisa percobaan: " + (MAKS_PERCOBAAN - percobaan),
                "Login Gagal", JOptionPane.ERROR_MESSAGE);
        txtPassword.setText("");
        txtPassword.requestFocus();
    }

    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlForm;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JLabel lblSub;
    private javax.swing.JLabel lblTag;
    private javax.swing.JLabel lblSelamat;
    private javax.swing.JLabel lblPetunjuk;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblPass;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JCheckBox chkLihat;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnKeluar;
}
