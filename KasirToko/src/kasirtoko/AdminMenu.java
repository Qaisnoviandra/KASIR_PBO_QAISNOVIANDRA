package kasirtoko;

import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class AdminMenu extends JFrame {
    private JTable tableProduk;
    private DefaultTableModel modelProduk;
    private JTextField txtCari, txtNama, txtHarga, txtStok;
    private JButton btnTambah, btnEdit, btnHapus, btnRefresh, btnLogout;
    
    private final Color PRIMARY_BLUE = new Color(59, 130, 246);
    private final Color PRIMARY_DARK = new Color(30, 64, 175);
    private final Color PRIMARY_LIGHT = new Color(219, 234, 254);
    private final Color BG_GRADIENT_TOP = new Color(248, 250, 252);
    private final Color CARD_WHITE = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private final Color TEXT_SECONDARY = new Color(71, 85, 105);
    private final Color BORDER_SOFT = new Color(203, 213, 225);
    private final Color BORDER_FOCUS = new Color(59, 130, 246);
    private final Color ACCENT_ORANGE = new Color(249, 115, 22);
    private final Color ACCENT_GREEN = new Color(34, 197, 94);
    private final Color ACCENT_RED = new Color(239, 68, 68);
    private final Color SHADOW_COLOR = new Color(0, 0, 0, 8);
    
    private Font getFont(String name, int style, int size) {
        try { return new Font(name, style, size); } 
        catch (Exception e) { return new Font("SansSerif", style, size); }
    }
    
    public AdminMenu() {
        initUI();
        loadProduk();
        setVisible(true);
    }
    
    private void initUI() {
        setTitle("ADMIN DASHBOARD - KELOLA TOKO");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(BG_GRADIENT_TOP);
        
        // === HEADER ===
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_BLUE, 0, getHeight(), PRIMARY_DARK);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(new Color(255,255,255,20));
                g2d.fillOval(-100, -100, 300, 300);
                g2d.fillOval(getWidth()-150, getHeight()-120, 250, 250);
            }
        };
        header.setBounds(0, 0, 1100, 100);
        header.setLayout(null);
        header.setOpaque(false);
        
        JLabel lblTitle = new JLabel("⚙️ Admin Dashboard");
        lblTitle.setFont(getFont("Poppins", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(35, 25, 400, 40);
        header.add(lblTitle);
        
        JLabel lblSubtitle = new JLabel("Kelola produk, transaksi, dan pengaturan toko");
        lblSubtitle.setFont(getFont("Poppins", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(255,255,255,220));
        lblSubtitle.setBounds(35, 60, 450, 25);
        header.add(lblSubtitle);
        
        btnLogout = new JButton("🚪 Logout");
        btnLogout.setFont(getFont("Poppins", Font.BOLD, 12));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(ACCENT_RED);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setOpaque(true);
        btnLogout.setBounds(950, 30, 120, 40);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220,38,38), 2, true),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        btnLogout.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnLogout.setBackground(new Color(185,28,28)); }
            public void mouseExited(MouseEvent e) { btnLogout.setBackground(ACCENT_RED); }
        });
        header.add(btnLogout);
        add(header);
        
        // === STATS CARDS ===
        JPanel panelStats = new JPanel(new GridLayout(1, 4, 15, 0));
        panelStats.setBackground(new Color(0,0,0,0));
        panelStats.setBounds(30, 115, 1040, 90);
        panelStats.add(createStatCard("📦 Total Produk", "128", PRIMARY_BLUE, ACCENT_ORANGE));
        panelStats.add(createStatCard("🧾 Transaksi", "342", ACCENT_GREEN, PRIMARY_LIGHT));
        panelStats.add(createStatCard("👥 Pengguna", "89", ACCENT_ORANGE, PRIMARY_LIGHT));
        panelStats.add(createStatCard("💰 Pendapatan", "Rp 24,5Jt", ACCENT_GREEN, PRIMARY_LIGHT));
        add(panelStats);
        
        // === SEARCH & ACTION BAR ===
        JPanel panelAction = createModernCard(30, 225, 1040, 70);
        panelAction.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        
        JPanel searchPanel = new JPanel(new BorderLayout(5,0));
        searchPanel.setBackground(new Color(0,0,0,0));
        searchPanel.setPreferredSize(new Dimension(350, 40));
        
        JLabel lblSearchIcon = new JLabel("🔍");
        lblSearchIcon.setFont(getFont("Segoe UI Emoji", Font.PLAIN, 14));
        searchPanel.add(lblSearchIcon, BorderLayout.WEST);
        
        txtCari = new JTextField("Cari produk...");
        txtCari.setFont(getFont("Poppins", Font.PLAIN, 13));
        txtCari.setForeground(TEXT_SECONDARY);
        txtCari.setBorder(null);
        txtCari.setBackground(new Color(248, 250, 252));
        txtCari.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txtCari.getText().equals("Cari produk...")) txtCari.setText("");
                txtCari.setForeground(TEXT_PRIMARY);
            }
            public void focusLost(FocusEvent e) {
                if (txtCari.getText().isEmpty()) {
                    txtCari.setText("Cari produk...");
                    txtCari.setForeground(TEXT_SECONDARY);
                }
            }
        });
        searchPanel.add(txtCari, BorderLayout.CENTER);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_SOFT, 2, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panelAction.add(searchPanel);
        
        btnTambah = createActionButton("➕ Tambah", ACCENT_GREEN);
        btnEdit = createActionButton("✏️ Edit", PRIMARY_BLUE);
        btnHapus = createActionButton("🗑️ Hapus", ACCENT_RED);
        btnRefresh = createActionButton("🔄 Refresh", TEXT_SECONDARY);
        
        panelAction.add(btnTambah);
        panelAction.add(btnEdit);
        panelAction.add(btnHapus);
        panelAction.add(Box.createHorizontalStrut(10));
        panelAction.add(btnRefresh);
        add(panelAction);
        
        // === FORM INPUT ===
        JPanel panelForm = createModernCard(30, 315, 1040, 110);
        panelForm.setLayout(null);
        
        JLabel lblFormTitle = new JLabel("📝 Input Data Produk");
        lblFormTitle.setFont(getFont("Poppins", Font.BOLD, 14));
        lblFormTitle.setForeground(TEXT_PRIMARY);
        lblFormTitle.setBounds(20, 15, 200, 25);
        panelForm.add(lblFormTitle);
        
        String[] labels = {"Nama Produk", "Harga (Rp)", "Stok"};
        txtNama = createInputField();
        txtHarga = createInputField();
        txtStok = createInputField();
        JTextField[] fields = {txtNama, txtHarga, txtStok};
        
        for (int i = 0; i < 3; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(getFont("Poppins", Font.PLAIN, 11));
            lbl.setForeground(TEXT_SECONDARY);
            lbl.setBounds(20 + (i * 330), 45, 100, 20);
            panelForm.add(lbl);
            
            fields[i].setBounds(20 + (i * 330), 65, 300, 35);
            panelForm.add(fields[i]);
        }
        
        JButton btnSimpan = new JButton("💾 Simpan");
        btnSimpan.setFont(getFont("Poppins", Font.BOLD, 12));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setBackground(PRIMARY_BLUE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_BLUE, 2, true),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        btnSimpan.setBounds(880, 65, 130, 35);
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSimpan.addActionListener(e -> simpanProduk());
        panelForm.add(btnSimpan);
        add(panelForm);
        
        // === TABLE PRODUK ===
        JLabel lblTableTitle = new JLabel("📋 Daftar Produk");
        lblTableTitle.setFont(getFont("Poppins", Font.BOLD, 16));
        lblTableTitle.setForeground(TEXT_PRIMARY);
        lblTableTitle.setBounds(30, 445, 200, 30);
        add(lblTableTitle);
        
        JPanel panelTable = createModernCard(30, 480, 1040, 220);
        panelTable.setLayout(new BorderLayout());
        
        String[] cols = {"ID", "Nama Produk", "Harga", "Stok", "Aksi"};
        modelProduk = new DefaultTableModel(cols, 0);
        
        tableProduk = new JTable(modelProduk);
        tableProduk.setFont(getFont("Poppins", Font.PLAIN, 12));
        tableProduk.setRowHeight(50);
        tableProduk.setSelectionBackground(PRIMARY_LIGHT);
        tableProduk.setSelectionForeground(TEXT_PRIMARY);
        tableProduk.setGridColor(BORDER_SOFT);
        tableProduk.getTableHeader().setFont(getFont("Poppins", Font.BOLD, 12));
        tableProduk.getTableHeader().setBackground(PRIMARY_BLUE);
        tableProduk.getTableHeader().setForeground(Color.WHITE);
        tableProduk.getTableHeader().setPreferredSize(new Dimension(0, 45));
        tableProduk.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableProduk.getColumnModel().getColumn(1).setPreferredWidth(250);
        tableProduk.getColumnModel().getColumn(2).setPreferredWidth(150);
        tableProduk.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableProduk.getColumnModel().getColumn(4).setPreferredWidth(180);
        
        // === RENDERER UNTUK KOLOM AKSI (SIMPLE - TANPA EDITOR RIBET) ===
        tableProduk.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
                panel.setBackground(CARD_WHITE);
                
                JButton btnEdit = new JButton("✏️");
                btnEdit.setToolTipText("Edit");
                btnEdit.setFont(new Font("Poppins", Font.BOLD, 11));
                btnEdit.setForeground(Color.WHITE);
                btnEdit.setBackground(PRIMARY_BLUE);
                btnEdit.setFocusPainted(false);
                btnEdit.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_BLUE, 2, true),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
                btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnEdit.setName("btnEdit_" + row);
                
                JButton btnDelete = new JButton("🗑️");
                btnDelete.setToolTipText("Hapus");
                btnDelete.setFont(new Font("Poppins", Font.BOLD, 11));
                btnDelete.setForeground(Color.WHITE);
                btnDelete.setBackground(ACCENT_RED);
                btnDelete.setFocusPainted(false);
                btnDelete.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_RED, 2, true),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
                btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnDelete.setName("btnDelete_" + row);
                
                panel.add(btnEdit);
                panel.add(btnDelete);
                return panel;
            }
        });
        
        // === MOUSE LISTENER UNTUK HANDLE KLIK TOMBOL (SIMPLE & ERROR-FREE) ===
        tableProduk.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableProduk.rowAtPoint(e.getPoint());
                int col = tableProduk.columnAtPoint(e.getPoint());
                
                if (col == 4 && row >= 0) {
                    Point point = e.getPoint();
                    Rectangle cellRect = tableProduk.getCellRect(row, col, false);
                    int xInCell = point.x - cellRect.x;
                    
                    if (xInCell < 90) {
                        editProduk(row);
                    } else {
                        hapusProduk(row);
                    }
                }
            }
        });
        
        JScrollPane scroll = new JScrollPane(tableProduk);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CARD_WHITE);
        panelTable.add(scroll, BorderLayout.CENTER);
        add(panelTable);
        
        // === FOOTER ===
        JLabel lblFooter = new JLabel("© 2024 Admin Dashboard | Kelola dengan bijak 🎯", SwingConstants.CENTER);
        lblFooter.setFont(getFont("Poppins", Font.PLAIN, 10));
        lblFooter.setForeground(TEXT_SECONDARY);
        lblFooter.setBounds(0, 710, 1100, 20);
        add(lblFooter);
        
        // === ACTION LISTENERS ===
        btnTambah.addActionListener(e -> simpanProduk());
        btnRefresh.addActionListener(e -> loadProduk());
        btnLogout.addActionListener(e -> logout());
        txtCari.addActionListener(e -> cariProduk());
    }
    
    private JPanel createStatCard(String title, String value, Color bg, Color accent) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(SHADOW_COLOR);
                g2d.fillRoundRect(3,3,getWidth()-6,getHeight()-6,16,16);
                g2d.setColor(CARD_WHITE);
                g2d.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
                g2d.setColor(accent);
                g2d.fillRoundRect(0,0,getWidth(),4,16,16);
            }
        };
        card.setLayout(null);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBackground(new Color(248,250,252)); }
            public void mouseExited(MouseEvent e) { card.setBackground(CARD_WHITE); }
        });
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(getFont("Poppins", Font.PLAIN, 12));
        lblTitle.setForeground(TEXT_SECONDARY);
        lblTitle.setBounds(20, 15, 200, 20);
        card.add(lblTitle);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(getFont("Poppins", Font.BOLD, 24));
        lblValue.setForeground(bg);
        lblValue.setBounds(20, 35, 200, 35);
        card.add(lblValue);
        
        return card;
    }
    
    private JPanel createModernCard(int x, int y, int w, int h) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(SHADOW_COLOR);
                g2d.fillRoundRect(4,4,getWidth()-8,getHeight()-8,20,20);
                g2d.setColor(CARD_WHITE);
                g2d.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
                g2d.setColor(PRIMARY_BLUE);
                g2d.fillRoundRect(0,0,getWidth(),3,16,16);
            }
        };
        card.setBounds(x,y,w,h);
        card.setOpaque(false);
        return card;
    }
    
    private JButton createActionButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(getFont("Poppins", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg, 2, true),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }
    
    private JTextField createInputField() {
        JTextField tf = new JTextField();
        tf.setFont(getFont("Poppins", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_SOFT, 2, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        tf.setBackground(new Color(248,250,252));
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(PRIMARY_BLUE);
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                tf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_FOCUS, 2, true),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
                tf.setBackground(Color.WHITE);
            }
            public void focusLost(FocusEvent e) {
                tf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_SOFT, 2, true),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
                tf.setBackground(new Color(248,250,252));
            }
        });
        return tf;
    }
    
    private void loadProduk() {
        modelProduk.setRowCount(0);
        try {
            String sql = "SELECT id, nama, harga, stok FROM produk ORDER BY nama ASC";
            Statement stmt = koneksi.getKoneksi().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("nama"),
                    "Rp " + String.format("%,d", rs.getInt("harga")),
                    rs.getInt("stok"),
                    "Aksi"
                };
                modelProduk.addRow(row);
            }
        } catch (Exception e) { 
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error load produk: " + e.getMessage());
        }
    }
    
    private void cariProduk() {
        String keyword = txtCari.getText().toLowerCase();
        if (keyword.isEmpty() || keyword.equals("cari produk...")) {
            loadProduk();
            return;
        }
        modelProduk.setRowCount(0);
        try {
            String sql = "SELECT id, nama, harga, stok FROM produk WHERE LOWER(nama) LIKE ? ORDER BY nama ASC";
            PreparedStatement ps = koneksi.getKoneksi().prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("nama"),
                    "Rp " + String.format("%,d", rs.getInt("harga")),
                    rs.getInt("stok"),
                    "Aksi"
                };
                modelProduk.addRow(row);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void simpanProduk() {
        String nama = txtNama.getText().trim();
        String hargaStr = txtHarga.getText().trim();
        String stokStr = txtStok.getText().trim();
        if (nama.isEmpty() || hargaStr.isEmpty() || stokStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int harga = Integer.parseInt(hargaStr);
            int stok = Integer.parseInt(stokStr);
            String sql = "INSERT INTO produk (nama, harga, stok) VALUES (?, ?, ?)";
            PreparedStatement ps = koneksi.getKoneksi().prepareStatement(sql);
            ps.setString(1, nama);
            ps.setInt(2, harga);
            ps.setInt(3, stok);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Produk berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            txtNama.setText(""); txtHarga.setText(""); txtStok.setText("");
            loadProduk();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "⚠️ Harga dan stok harus angka!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void editProduk(int row) {
        if (row < 0) return;
        int id = (int) modelProduk.getValueAt(row, 0);
        String nama = JOptionPane.showInputDialog(this, "Nama Produk:", modelProduk.getValueAt(row, 1));
        if (nama == null) return;
        String hargaStr = JOptionPane.showInputDialog(this, "Harga:", modelProduk.getValueAt(row, 2).toString().replace("Rp ", "").replace(",", ""));
        if (hargaStr == null) return;
        String stokStr = JOptionPane.showInputDialog(this, "Stok:", modelProduk.getValueAt(row, 3));
        if (stokStr == null) return;
        try {
            int harga = Integer.parseInt(hargaStr);
            int stok = Integer.parseInt(stokStr);
            String sql = "UPDATE produk SET nama=?, harga=?, stok=? WHERE id=?";
            PreparedStatement ps = koneksi.getKoneksi().prepareStatement(sql);
            ps.setString(1, nama);
            ps.setInt(2, harga);
            ps.setInt(3, stok);
            ps.setInt(4, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Produk berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loadProduk();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void hapusProduk(int row) {
        if (row < 0) return;
        int id = (int) modelProduk.getValueAt(row, 0);
        String nama = modelProduk.getValueAt(row, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this, 
            "🗑️ Yakin ingin menghapus produk \"" + nama + "\"?", 
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM produk WHERE id=?";
                PreparedStatement ps = koneksi.getKoneksi().prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "✅ Produk berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadProduk();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "🚪 Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) { dispose(); new login(); }
    }
    
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
        catch (Exception e) { e.printStackTrace(); }
        SwingUtilities.invokeLater(() -> new AdminMenu());
    }
}