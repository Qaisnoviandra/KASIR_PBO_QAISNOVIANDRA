package kasirtoko;

import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class PelangganMenu extends JFrame {
    private JComboBox<String> cmbProduk;
    private JTextField txtJumlah, txtTotal;
    private JButton btnBeli, btnLogout;
    private JTable tableTransaksi;
    private DefaultTableModel model;
    
    // 🎨 Modern Color Palette: Biru-Putih-Abu + Aksen Balance
    private final Color PRIMARY_BLUE = new Color(59, 130, 246);
    private final Color PRIMARY_DARK = new Color(30, 64, 175);
    private final Color PRIMARY_LIGHT = new Color(219, 234, 254);
    private final Color BG_GRADIENT_TOP = new Color(248, 250, 252);
    private final Color BG_GRADIENT_BOTTOM = new Color(241, 245, 249);
    private final Color CARD_WHITE = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private final Color TEXT_SECONDARY = new Color(71, 85, 105);
    private final Color BORDER_SOFT = new Color(203, 213, 225);
    private final Color BORDER_FOCUS = new Color(59, 130, 246);
    private final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private final Color ACCENT_GREEN = new Color(34, 197, 94);
    private final Color SHADOW_COLOR = new Color(0, 0, 0, 8);
    
    // ✅ Font helper dengan fallback
    private Font getFont(String name, int style, int size) {
        try {
            return new Font(name, style, size);
        } catch (Exception e) {
            return new Font("SansSerif", style, size);
        }
    }
    
    public PelangganMenu() {
        initUI();
        loadProduk();
        loadTransaksi();
        setVisible(true);
    }
    
    private void initUI() {
        setTitle("MENU PELANGGAN - BELANJA PRODUK");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(BG_GRADIENT_TOP);
        
        // === HEADER PANEL ===
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_BLUE, 0, getHeight(), PRIMARY_DARK);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(new Color(255, 255, 255, 25));
                g2d.fillOval(-80, -80, 250, 250);
                g2d.fillOval(getWidth() - 120, getHeight() - 100, 200, 200);
            }
        };
        headerPanel.setBounds(0, 0, 900, 90);
        headerPanel.setLayout(null);
        headerPanel.setOpaque(false);
        
        JLabel lblTitle = new JLabel("🛒 Belanja Produk");
        lblTitle.setFont(getFont("Poppins", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(35, 25, 400, 35);
        headerPanel.add(lblTitle);
        
        JLabel lblSubtitle = new JLabel("Pilih produk, masukkan jumlah, dan belanja dengan mudah");
        lblSubtitle.setFont(getFont("Poppins", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(255, 255, 255, 220));
        lblSubtitle.setBounds(35, 55, 500, 25);
        headerPanel.add(lblSubtitle);
        
        btnLogout = new JButton("🚪 Logout");
        btnLogout.setFont(getFont("Poppins", Font.BOLD, 12));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(new Color(239, 68, 68));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setOpaque(true);
        btnLogout.setBounds(750, 28, 120, 38);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setBorder(new RoundedBorder(10, new Color(220, 38, 38)));
        
        btnLogout.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnLogout.setBackground(new Color(185, 28, 28));
                btnLogout.setBorder(new RoundedBorder(10, new Color(185, 28, 28)));
            }
            public void mouseExited(MouseEvent e) {
                btnLogout.setBackground(new Color(239, 68, 68));
                btnLogout.setBorder(new RoundedBorder(10, new Color(220, 38, 38)));
            }
        });
        headerPanel.add(btnLogout);
        add(headerPanel);
        
        // === PANEL PEMILIHAN PRODUK ===
        JPanel panelPilih = createModernCard(30, 110, 840, 140);
        panelPilih.setLayout(null);
        
        JLabel lblJudulPilih = new JLabel("🛍️ Pilih Produk");
        lblJudulPilih.setFont(getFont("Poppins", Font.BOLD, 16));
        lblJudulPilih.setForeground(TEXT_PRIMARY);
        lblJudulPilih.setBounds(25, 15, 200, 30);
        panelPilih.add(lblJudulPilih);
        
        JLabel lblSep1 = new JLabel();
        lblSep1.setBackground(PRIMARY_LIGHT);
        lblSep1.setBounds(25, 50, 790, 2);
        lblSep1.setOpaque(true);
        panelPilih.add(lblSep1);
        
        JLabel lblProduk = new JLabel("Produk");
        lblProduk.setFont(getFont("Poppins", Font.PLAIN, 12));
        lblProduk.setForeground(TEXT_SECONDARY);
        lblProduk.setBounds(25, 65, 80, 25);
        panelPilih.add(lblProduk);
        
        cmbProduk = createModernComboBox();
        cmbProduk.setBounds(25, 88, 520, 40);
        panelPilih.add(cmbProduk);
        
        JLabel lblJumlah = new JLabel("Jumlah");
        lblJumlah.setFont(getFont("Poppins", Font.PLAIN, 12));
        lblJumlah.setForeground(TEXT_SECONDARY);
        lblJumlah.setBounds(570, 65, 80, 25);
        panelPilih.add(lblJumlah);
        
        txtJumlah = createModernTextField();
        txtJumlah.setHorizontalAlignment(JTextField.CENTER);
        txtJumlah.setBounds(570, 88, 120, 40);
        panelPilih.add(txtJumlah);
        
        JLabel lblInfo = new JLabel("💡 Masukkan jumlah, lalu klik BELI");
        lblInfo.setFont(getFont("Poppins", Font.ITALIC, 11));
        lblInfo.setForeground(new Color(148, 163, 184));
        lblInfo.setBounds(710, 95, 110, 25);
        panelPilih.add(lblInfo);
        add(panelPilih);
        
        // === PANEL TOTAL & BUTTON ===
        JPanel panelBayar = createModernCard(30, 270, 840, 85);
        panelBayar.setLayout(null);
        
        JLabel lblTotal = new JLabel("💰 Total Bayar:");
        lblTotal.setFont(getFont("Poppins", Font.BOLD, 14));
        lblTotal.setForeground(TEXT_SECONDARY);
        lblTotal.setBounds(25, 22, 140, 25);
        panelBayar.add(lblTotal);
        
        txtTotal = new JTextField();
        txtTotal.setFont(getFont("Poppins", Font.BOLD, 18));
        txtTotal.setForeground(ACCENT_GREEN);
        txtTotal.setBackground(PRIMARY_LIGHT);
        txtTotal.setHorizontalAlignment(JTextField.RIGHT);
        txtTotal.setEditable(false);
        txtTotal.setBorder(new RoundedBorder(10, ACCENT_GREEN));
        txtTotal.setBounds(175, 18, 220, 45);
        panelBayar.add(txtTotal);
        
        btnBeli = new JButton("🛒 BELI SEKARANG");
        btnBeli.setFont(getFont("Poppins", Font.BOLD, 14));
        btnBeli.setForeground(Color.WHITE);
        btnBeli.setBackground(ACCENT_GREEN);
        btnBeli.setFocusPainted(false);
        btnBeli.setBorderPainted(false);
        btnBeli.setContentAreaFilled(false);
        btnBeli.setOpaque(true);
        btnBeli.setBounds(420, 18, 395, 48);
        btnBeli.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBeli.setBorder(new RoundedBorder(12, ACCENT_GREEN));
        
        btnBeli.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnBeli.setBackground(new Color(22, 163, 74));
                btnBeli.setBorder(new RoundedBorder(12, new Color(22, 163, 74)));
            }
            public void mouseExited(MouseEvent e) {
                btnBeli.setBackground(ACCENT_GREEN);
                btnBeli.setBorder(new RoundedBorder(12, ACCENT_GREEN));
            }
            public void mousePressed(MouseEvent e) {
                btnBeli.setBackground(new Color(21, 128, 61));
            }
            public void mouseReleased(MouseEvent e) {
                btnBeli.setBackground(new Color(22, 163, 74));
            }
        });
        panelBayar.add(btnBeli);
        add(panelBayar);
        
        // === RIWAYAT SECTION ===
        JLabel lblRiwayat = new JLabel("📋 Riwayat Pembelian Anda");
        lblRiwayat.setFont(getFont("Poppins", Font.BOLD, 18));
        lblRiwayat.setForeground(TEXT_PRIMARY);
        lblRiwayat.setBounds(30, 375, 400, 35);
        add(lblRiwayat);
        
        // === TABLE RIWAYAT ===
        JPanel panelTable = createModernCard(30, 415, 840, 240);
        panelTable.setLayout(new BorderLayout(0, 0));
        
        String[] kolom = {"ID", "Produk", "Jumlah", "Total", "Tanggal"};
        model = new DefaultTableModel(kolom, 0);
        tableTransaksi = new JTable(model);
        tableTransaksi.setFont(getFont("Poppins", Font.PLAIN, 12));
        tableTransaksi.setRowHeight(35);
        tableTransaksi.setSelectionBackground(PRIMARY_LIGHT);
        tableTransaksi.setSelectionForeground(TEXT_PRIMARY);
        tableTransaksi.setGridColor(BORDER_SOFT);
        tableTransaksi.setShowGrid(true);
        
        tableTransaksi.getTableHeader().setFont(getFont("Poppins", Font.BOLD, 12));
        tableTransaksi.getTableHeader().setBackground(PRIMARY_BLUE);
        tableTransaksi.getTableHeader().setForeground(Color.WHITE);
        tableTransaksi.getTableHeader().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        tableTransaksi.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        JScrollPane scroll = new JScrollPane(tableTransaksi);
        scroll.setBorder(null);
        scroll.setViewportBorder(null);
        scroll.setBackground(CARD_WHITE);
        panelTable.add(scroll, BorderLayout.CENTER);
        add(panelTable);
        
        // === FOOTER ===
        JLabel lblFooter = new JLabel("© 2024 Aplikasi Kasir Toko | Selamat Belanja! 🎉", SwingConstants.CENTER);
        lblFooter.setFont(getFont("Poppins", Font.PLAIN, 10));
        lblFooter.setForeground(TEXT_SECONDARY);
        lblFooter.setBounds(0, 660, 900, 20);
        add(lblFooter);
        
        // === ACTION LISTENERS ===
        btnBeli.addActionListener(e -> beliProduk());
        btnLogout.addActionListener(e -> logout());
        
        txtJumlah.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });
    }
    
    // ✅ Helper: Create modern card panel
    private JPanel createModernCard(int x, int y, int w, int h) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(SHADOW_COLOR);
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 20, 20);
                g2d.setColor(CARD_WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2d.setColor(PRIMARY_BLUE);
                g2d.fillRoundRect(0, 0, getWidth(), 3, 16, 16);
            }
        };
        card.setBounds(x, y, w, h);
        card.setOpaque(false);
        card.setLayout(null);
        return card;
    }
    
    // ✅ Helper: Modern TextField
    private JTextField createModernTextField() {
        JTextField tf = new JTextField();
        tf.setFont(getFont("Poppins", Font.PLAIN, 13));
        tf.setBorder(new RoundedBorder(10, BORDER_SOFT));
        tf.setBackground(new Color(248, 250, 252));
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(PRIMARY_BLUE);
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                tf.setBorder(new RoundedBorder(10, BORDER_FOCUS));
                tf.setBackground(Color.WHITE);
            }
            public void focusLost(FocusEvent e) {
                tf.setBorder(new RoundedBorder(10, BORDER_SOFT));
                tf.setBackground(new Color(248, 250, 252));
            }
        });
        return tf;
    }
    
    // ✅ Helper: Modern ComboBox (SIMPLE VERSION - NO ERROR)
    private JComboBox<String> createModernComboBox() {
        JComboBox<String> cb = new JComboBox<>();
        cb.setFont(getFont("Poppins", Font.PLAIN, 13));
        cb.setBackground(new Color(248, 250, 252));
        cb.setForeground(TEXT_PRIMARY);
        cb.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cb.setBorder(new RoundedBorder(10, BORDER_SOFT));
        
        // Custom renderer for dropdown items
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
              label.setFont(new Font("Poppins", Font.PLAIN, 13));
                if (isSelected) {
                    label.setBackground(PRIMARY_BLUE);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(CARD_WHITE);
                    label.setForeground(TEXT_PRIMARY);
                }
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                return label;
            }
        });
        
        // Focus effect
        cb.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                cb.setBorder(new RoundedBorder(10, BORDER_FOCUS));
                cb.setBackground(Color.WHITE);
            }
            public void focusLost(FocusEvent e) {
                cb.setBorder(new RoundedBorder(10, BORDER_SOFT));
                cb.setBackground(new Color(248, 250, 252));
            }
        });
        
        return cb;
    }
    
    // ✅ Custom Rounded Border class
    class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;
        
        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius/2, radius/2, radius/2, radius/2);
        }
        
        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
    
    // === METHOD loadProduk (TETAP SAMA) ===
    private void loadProduk() {
        try {
            String sql = "SELECT id, nama, harga FROM produk WHERE stok > 0 ORDER BY nama ASC";
            Statement stmt = koneksi.getKoneksi().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            cmbProduk.removeAllItems();
            cmbProduk.addItem("-- Pilih Produk --");
            while (rs.next()) {
                String item = rs.getInt("id") + " - " + rs.getString("nama") + 
                             " | Rp " + String.format("%,d", rs.getInt("harga"));
                cmbProduk.addItem(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error load produk: " + e.getMessage());
        }
    }
    
    // === METHOD beliProduk (TETAP SAMA) ===
    private void beliProduk() {
        try {
            if (cmbProduk.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "⚠️ Silakan pilih produk terlebih dahulu!", 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (txtJumlah.getText().isEmpty() || Integer.parseInt(txtJumlah.getText()) <= 0) {
                JOptionPane.showMessageDialog(this, "⚠️ Masukkan jumlah yang valid!", 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String selected = cmbProduk.getSelectedItem().toString();
            int idProduk = Integer.parseInt(selected.split(" - ")[0]);
            int jumlah = Integer.parseInt(txtJumlah.getText());
            
            String sqlCek = "SELECT nama, harga, stok FROM produk WHERE id = ?";
            PreparedStatement psCek = koneksi.getKoneksi().prepareStatement(sqlCek);
            psCek.setInt(1, idProduk);
            ResultSet rs = psCek.executeQuery();
            
            if (rs.next()) {
                String namaProduk = rs.getString("nama");
                int harga = rs.getInt("harga");
                int stok = rs.getInt("stok");
                
                if (jumlah > stok) {
                    JOptionPane.showMessageDialog(this, 
                        "❌ Stok " + namaProduk + " tidak cukup!\nTersedia: " + stok, 
                        "Stok Habis", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int total = harga * jumlah;
                
                String sqlUpdate = "UPDATE produk SET stok = stok - ? WHERE id = ?";
                PreparedStatement psUpdate = koneksi.getKoneksi().prepareStatement(sqlUpdate);
                psUpdate.setInt(1, jumlah);
                psUpdate.setInt(2, idProduk);
                psUpdate.executeUpdate();
                
                String sqlTrans = "INSERT INTO transaksi (id_produk, jumlah, total) VALUES (?, ?, ?)";
                PreparedStatement psTrans = koneksi.getKoneksi().prepareStatement(sqlTrans);
                psTrans.setInt(1, idProduk);
                psTrans.setInt(2, jumlah);
                psTrans.setInt(3, total);
                psTrans.executeUpdate();
                
                txtTotal.setText("Rp " + String.format("%,d", total));
                JOptionPane.showMessageDialog(this, 
                    "✅ Pembelian berhasil!\n\n📦 Produk: " + namaProduk + 
                    "\n🔢 Jumlah: " + jumlah + 
                    "\n💰 Total: Rp " + String.format("%,d", total),
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
                loadProduk();
                loadTransaksi();
                txtJumlah.setText("");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "⚠️ Masukkan angka yang valid!", "Error Input", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // === METHOD loadTransaksi (TETAP SAMA) ===
    private void loadTransaksi() {
        model.setRowCount(0);
        try {
            String sql = "SELECT t.id, p.nama, t.jumlah, t.total, t.tanggal " +
                        "FROM transaksi t " +
                        "JOIN produk p ON t.id_produk = p.id " +
                        "ORDER BY t.tanggal DESC";
            Statement stmt = koneksi.getKoneksi().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getInt("jumlah"),
                    "Rp " + String.format("%,d", rs.getInt("total")),
                    rs.getTimestamp("tanggal")
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // === METHOD logout (TETAP SAMA) ===
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "🚪 Yakin ingin logout?", "Konfirmasi", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new login();
        }
    }
}