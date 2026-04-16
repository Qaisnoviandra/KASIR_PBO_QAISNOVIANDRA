package kasirtoko;

import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class login extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;
    private JLabel lblUser, lblPass, lblTitle, lblIcon, lblFooter, lblSubtitle;
    private JPanel panelForm, panelHeader, panelContent;
    
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
    
    // ✅ FONT HELPER: Fallback jika Poppins tidak tersedia
    private Font getFont(String name, int style, int size) {
        try {
            Font font = new Font(name, style, size);
            // Cek apakah font bisa di-load (opsional, tapi aman)
            return font;
        } catch (Exception e) {
            return new Font("SansSerif", style, size); // Fallback
        }
    }
    
    public login() {
        try {
            initUI();
        } catch (Exception e) {
            // ✅ Fallback UI jika ada error kritis
            JOptionPane.showMessageDialog(null, 
                "Gagal memuat tampilan modern: " + e.getMessage() + "\nMenggunakan tampilan dasar...", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
            createSimpleUI();
        }
        // ✅ Pastikan selalu visible
        setVisible(true);
    }
    
    private void initUI() {
        setTitle("LOGIN - APLIKASI KASIR TOKO");
        setSize(480, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(BG_GRADIENT_TOP);
        
        // === HEADER PANEL ===
        panelHeader = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                try {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    GradientPaint gp = new GradientPaint(0, 0, PRIMARY_BLUE, 0, getHeight(), PRIMARY_DARK);
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    g2d.setColor(new Color(255, 255, 255, 30));
                    g2d.fillOval(-50, -50, 200, 200);
                    g2d.fillOval(getWidth() - 100, getHeight() - 80, 150, 150);
                } catch (Exception e) {
                    // Fallback: gambar polos jika custom painting error
                    super.paintComponent(g);
                    setBackground(PRIMARY_BLUE);
                }
            }
        };
        panelHeader.setBounds(0, 0, 480, 160);
        panelHeader.setLayout(null);
        panelHeader.setOpaque(false);
        
        lblIcon = new JLabel("🛒", SwingConstants.CENTER);
        lblIcon.setFont(getFont("Segoe UI Emoji", Font.PLAIN, 55));
        lblIcon.setForeground(Color.WHITE);
        lblIcon.setBounds(0, 20, 480, 60);
        panelHeader.add(lblIcon);
        
        lblTitle = new JLabel("APLIKASI KASIR", SwingConstants.CENTER);
        lblTitle.setFont(getFont("Poppins", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 75, 480, 35);
        panelHeader.add(lblTitle);
        
        lblSubtitle = new JLabel("Silakan login untuk melanjutkan", SwingConstants.CENTER);
        lblSubtitle.setFont(getFont("Poppins", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(255, 255, 255, 200));
        lblSubtitle.setBounds(0, 105, 480, 25);
        panelHeader.add(lblSubtitle);
        
        add(panelHeader);
        
        // === CONTENT PANEL ===
        panelContent = new JPanel();
        panelContent.setBackground(new Color(0, 0, 0, 0));
        panelContent.setBounds(0, 140, 480, 500);
        panelContent.setLayout(null);
        add(panelContent);
        
        // === FORM CARD ===
        panelForm = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                try {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2d.setColor(SHADOW_COLOR);
                    g2d.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 20, 20);
                    
                    g2d.setColor(CARD_WHITE);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                    
                    g2d.setColor(PRIMARY_BLUE);
                    g2d.fillRoundRect(0, 0, getWidth(), 4, 16, 16);
                } catch (Exception e) {
                    super.paintComponent(g);
                    setBackground(CARD_WHITE);
                    setBorder(BorderFactory.createLineBorder(PRIMARY_BLUE, 2));
                }
            }
        };
        panelForm.setBounds(40, 20, 400, 420);
        panelForm.setLayout(null);
        panelForm.setOpaque(false);
        panelContent.add(panelForm);
        
        // Form Title
        JLabel lblFormTitle = new JLabel("🔐 Login Akun");
        lblFormTitle.setFont(getFont("Poppins", Font.BOLD, 18));
        lblFormTitle.setForeground(TEXT_PRIMARY);
        lblFormTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblFormTitle.setBounds(0, 30, 400, 35);
        panelForm.add(lblFormTitle);
        
        // Separator
        JLabel lblSeparator = new JLabel();
        lblSeparator.setBackground(PRIMARY_LIGHT);
        lblSeparator.setBounds(80, 70, 240, 2);
        lblSeparator.setOpaque(true);
        panelForm.add(lblSeparator);
        
        // === USERNAME ===
        lblUser = new JLabel("Username");
        // ✅ PERBAIKAN FONT: Pakai helper + constant yang valid
        lblUser.setFont(getFont("Poppins", Font.PLAIN, 12));
        lblUser.setForeground(TEXT_SECONDARY);
        lblUser.setBounds(40, 95, 100, 25);
        panelForm.add(lblUser);
        
        txtUser = createModernTextField();
        txtUser.setBounds(40, 120, 320, 48);
        panelForm.add(txtUser);
        
        // === PASSWORD ===
        lblPass = new JLabel("Password");
        // ✅ PERBAIKAN FONT: Font.BOLD (kapital semua)
        lblPass.setFont(getFont("Poppins", Font.BOLD, 12));
        lblPass.setForeground(TEXT_SECONDARY);
        lblPass.setBounds(40, 180, 100, 25);
        panelForm.add(lblPass);
        
        txtPass = createModernPasswordField();
        txtPass.setBounds(40, 205, 320, 48);
        panelForm.add(txtPass);
        
        // === LOGIN BUTTON ===
        btnLogin = new JButton("Masuk Sekarang 🚀");
        btnLogin.setFont(getFont("Poppins", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(PRIMARY_BLUE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setOpaque(true);
        btnLogin.setBounds(40, 280, 320, 52);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBorder(new RoundedBorder(12, PRIMARY_BLUE));
        
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(PRIMARY_DARK);
                btnLogin.setBorder(new RoundedBorder(12, PRIMARY_DARK));
            }
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(PRIMARY_BLUE);
                btnLogin.setBorder(new RoundedBorder(12, PRIMARY_BLUE));
            }
            public void mousePressed(MouseEvent e) {
                btnLogin.setBackground(new Color(25, 55, 150));
            }
            public void mouseReleased(MouseEvent e) {
                btnLogin.setBackground(PRIMARY_DARK);
            }
        });
        panelForm.add(btnLogin);
        
        // Info Text
        JLabel lblInfo = new JLabel("💡 Default: admin/admin123 atau budi/budi123");
        lblInfo.setFont(getFont("Poppins", Font.ITALIC, 10));
        lblInfo.setForeground(new Color(148, 163, 184));
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        lblInfo.setBounds(0, 345, 400, 25);
        panelForm.add(lblInfo);
        
        // Footer
        lblFooter = new JLabel("© 2024 Aplikasi Kasir Toko | Developed with ❤️", SwingConstants.CENTER);
        lblFooter.setFont(getFont("Poppins", Font.PLAIN, 10));
        lblFooter.setForeground(TEXT_SECONDARY);
        lblFooter.setBounds(0, 590, 480, 20);
        add(lblFooter);
        
        // Action Listeners
        btnLogin.addActionListener(e -> doLogin());
        txtUser.addActionListener(e -> doLogin());
        txtPass.addActionListener(e -> doLogin());
        txtUser.requestFocus();
    }
    
    // ✅ Fallback UI sederhana jika modern UI gagal
    private void createSimpleUI() {
        setTitle("LOGIN - APLIKASI KASIR TOKO");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Username:"));
        txtUser = new JTextField();
        panel.add(txtUser);
        panel.add(new JLabel("Password:"));
        txtPass = new JPasswordField();
        panel.add(txtPass);
        btnLogin = new JButton("Login");
        panel.add(new JLabel());
        panel.add(btnLogin);
        
        add(panel, BorderLayout.CENTER);
        btnLogin.addActionListener(e -> doLogin());
        txtUser.addActionListener(e -> doLogin());
        txtPass.addActionListener(e -> doLogin());
    }
    
    // Helper method: Modern TextField
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
    
    // Helper method: Modern PasswordField
    private JPasswordField createModernPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(getFont("Poppins", Font.PLAIN, 13));
        pf.setBorder(new RoundedBorder(10, BORDER_SOFT));
        pf.setBackground(new Color(248, 250, 252));
        pf.setForeground(TEXT_PRIMARY);
        pf.setCaretColor(PRIMARY_BLUE);
        pf.setEchoChar('•');
        
        pf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                pf.setBorder(new RoundedBorder(10, BORDER_FOCUS));
                pf.setBackground(Color.WHITE);
            }
            public void focusLost(FocusEvent e) {
                pf.setBorder(new RoundedBorder(10, BORDER_SOFT));
                pf.setBackground(new Color(248, 250, 252));
            }
        });
        return pf;
    }
    
    // Custom Rounded Border class
    class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;
        
        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            try {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            } catch (Exception e) {
                // Fallback: border biasa
                g.setColor(color);
                g.drawRect(x, y, width - 1, height - 1);
            }
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
    
    // === METHOD doLogin (TETAP SAMA) ===
    private void doLogin() {
        try {
            String username = txtUser.getText().trim();
            String password = new String(txtPass.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                showNotification("⚠️ Username dan password tidak boleh kosong!", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String sql = "SELECT role FROM users WHERE username=? AND password=?";
            PreparedStatement ps = koneksi.getKoneksi().prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String role = rs.getString("role");
                btnLogin.setText("✅ Loading...");
                btnLogin.setEnabled(false);
                
                SwingUtilities.invokeLater(() -> {
                    dispose();
                    if (role.equals("admin")) {
                        new AdminMenu();
                    } else {
                        new PelangganMenu();
                    }
                });
            } else {
                showNotification("❌ Username atau password salah!", JOptionPane.ERROR_MESSAGE);
                txtPass.setText("");
                txtPass.requestFocus();
            }
        } catch (SQLException e) {
            showNotification("❌ Error koneksi database: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // === METHOD showNotification ===
    private void showNotification(String message, int messageType) {
        JOptionPane pane = new JOptionPane(message, messageType);
        JDialog dialog = pane.createDialog(this, "Notifikasi");
        dialog.setFont(getFont("Poppins", Font.PLAIN, 12));
        
        for (Component c : dialog.getComponents()) {
            if (c instanceof JPanel) {
                for (Component comp : ((JPanel) c).getComponents()) {
                    if (comp instanceof JButton) {
                        JButton btn = (JButton) comp;
                        btn.setFont(getFont("Poppins", Font.BOLD, 11));
                        btn.setBackground(messageType == JOptionPane.ERROR_MESSAGE ? new Color(239, 68, 68) : 
                                         messageType == JOptionPane.WARNING_MESSAGE ? ACCENT_ORANGE : PRIMARY_BLUE);
                        btn.setForeground(Color.WHITE);
                        btn.setFocusPainted(false);
                        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
                    }
                }
            }
        }
        dialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        // ✅ Set Look and Feel dengan error handling
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Abaikan jika L&F gagal, tetap jalan dengan default
            System.err.println("Look and Feel tidak tersedia: " + e.getMessage());
        }
        
        SwingUtilities.invokeLater(() -> new login());
    }
}