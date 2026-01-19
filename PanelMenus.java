package com.karyalinman;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// ==========================================
// 1. MENU UTAMA
// ==========================================
class PanelUtama extends MenuPanel {
    private String antrianSaya = null;

    public PanelUtama() { refreshData(); }

    @Override
    public void refreshData() {
        removeAll();
        // Navigasi Atas
        JPanel topNav = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topNav.setBackground(pinkPastel);
        JButton btnAdmin = new JButton("ADMIN");
        JButton btnGudang = new JButton("GUDANG");
        JButton btnKasir = new JButton("KASIR");

        btnAdmin.addActionListener(e -> AppUtama.pindahMenu("ADMIN"));
        btnGudang.addActionListener(e -> AppUtama.pindahMenu("GUDANG"));
        btnKasir.addActionListener(e -> AppUtama.pindahMenu("KASIR"));

        topNav.add(btnAdmin); topNav.add(btnGudang); topNav.add(btnKasir);
        add(topNav, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.setBackground(pinkPastel);

        // Bagian Antrian
        JPanel pnlAntrian = new JPanel();
        pnlAntrian.setBackground(pinkPastel);
        pnlAntrian.setBorder(BorderFactory.createTitledBorder("AMBIL NOMOR ANTRIAN (1-10)"));
        for (int i = 1; i <= 10; i++) {
            String no = String.format("%02d", i);
            JButton bAntri = new JButton(no);
            bAntri.setEnabled(!AppUtama.antrianAktif.contains(no));
            bAntri.addActionListener(e -> {
                antrianSaya = no;
                AppUtama.antrianAktif.add(no);
                AppUtama.keranjangPelanggan.put(no, new ArrayList<>());
                refreshData();
            });
            pnlAntrian.add(bAntri);
        }
        centerPanel.add(pnlAntrian);

        // Bagian Genre
        JPanel pnlGenre = new JPanel(new GridLayout(1, 5, 10, 10));
        pnlGenre.setBackground(pinkPastel);
        String[] genres = {"Romantis", "Horor", "Misteri", "Petualangan", "Pendidikan"};
        for (String g : genres) {
            JButton bg = new JButton(g, new ImageIcon("res/icon_" + g.toLowerCase() + ".png"));
            bg.setVerticalTextPosition(SwingConstants.BOTTOM);
            bg.setHorizontalTextPosition(SwingConstants.CENTER);
            bg.addActionListener(e -> {
                if (antrianSaya == null) JOptionPane.showMessageDialog(this, "AMBIL ANTRIAN DULU!");
                else tampilkanBuku(g);
            });
            pnlGenre.add(bg);
        }
        centerPanel.add(pnlGenre);
        add(centerPanel, BorderLayout.CENTER);
        revalidate(); repaint();
    }

    private void tampilkanBuku(String genre) {
        JDialog win = new JDialog();
        win.setTitle("Genre: " + genre + " | Antrian: " + antrianSaya);
        win.setSize(600, 500);
        win.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Judul", "Harga", "Stok"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Buku b : AppUtama.databaseBuku.get(genre)) {
            model.addRow(new Object[]{b.getJudul(), b.getHarga(), b.getStok()});
        }
        JTable table = new JTable(model);
        win.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnBeli = new JButton("MASUKKAN KERANJANG (+)");
        btnBeli.setBackground(hijauNeon);
        btnBeli.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Buku b = AppUtama.databaseBuku.get(genre).get(row);
                if (b.getStok() > 0) {
                    AppUtama.keranjangPelanggan.get(antrianSaya).add(new ItemKeranjang(b, 1));
                    b.setStok(b.getStok() - 1);
                    model.setValueAt(b.getStok(), row, 2);
                    JOptionPane.showMessageDialog(win, "Buku Berhasil Masuk Keranjang!");
                } else {
                    JOptionPane.showMessageDialog(win, "Stok Habis!");
                }
            }
        });
        win.add(btnBeli, BorderLayout.SOUTH);
        win.setLocationRelativeTo(null);
        win.setVisible(true);
    }
}

// ==========================================
// 2. MENU KASIR
// ==========================================
class PanelKasir extends MenuPanel {
    @Override
    public void refreshData() {
        removeAll();
        JButton btnBack = new JButton("BACK");
        btnBack.addActionListener(e -> AppUtama.pindahMenu("UTAMA"));
        add(btnBack, BorderLayout.NORTH);

        if (AppUtama.antrianAktif.isEmpty()) {
            JLabel lbl = new JLabel("TIDAK ADA ANTRIAN YANG PERLU DIPROSES", SwingConstants.CENTER);
            add(lbl, BorderLayout.CENTER);
        } else {
            String current = AppUtama.antrianAktif.get(0);
            JButton btnLayani = new JButton("LAYANI PELANGGAN NOMOR: " + current);
            btnLayani.setFont(new Font("Arial", Font.BOLD, 20));
            btnLayani.addActionListener(e -> prosesBayar(current));
            add(btnLayani, BorderLayout.CENTER);
        }
        revalidate(); repaint();
    }

    private void prosesBayar(String no) {
        double total = 0;
        StringBuilder struk = new StringBuilder("     KARYALINMAN BOOKSTORE\n");
        struk.append("--------------------------------\n");
        struk.append("Tgl: ").append(new Date()).append("\n");
        struk.append("No Antrian: ").append(no).append("\n");
        struk.append("--------------------------------\n");

        for (ItemKeranjang item : AppUtama.keranjangPelanggan.get(no)) {
            struk.append(item.buku.getJudul()).append("\n");
            struk.append("   1 x Rp ").append(item.buku.getHarga()).append("\n");
            total += item.buku.getHarga();
        }
        struk.append("--------------------------------\n");
        struk.append("TOTAL BELANJA: Rp ").append(total).append("\n");

        String bayarStr = JOptionPane.showInputDialog(null, struk + "MASUKKAN UANG PEMBAYARAN:", "PEMBAYARAN KASIR", JOptionPane.QUESTION_MESSAGE);
        if (bayarStr != null) {
            double bayar = Double.parseDouble(bayarStr);
            double kembali = bayar - total;
            String strukFinal = struk.toString() + "TUNAI: Rp " + bayar + "\nKEMBALI: Rp " + kembali + "\n\nTERIMAKASIH TELAH BERBELANJA";

            JTextArea area = new JTextArea(strukFinal);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "STRUK PEMBAYARAN", JOptionPane.INFORMATION_MESSAGE);

            AppUtama.laporanTransaksi.add("Antrian " + no + " | Total: " + total + " | " + new Date());
            AppUtama.antrianAktif.remove(no);
            AppUtama.keranjangPelanggan.remove(no);
            refreshData();
        }
    }
}

// ==========================================
// 3. MENU GUDANG
// ==========================================
class PanelGudang extends MenuPanel {
    @Override
    public void refreshData() {
        removeAll();
        JButton btnBack = new JButton("BACK");
        btnBack.addActionListener(e -> AppUtama.pindahMenu("UTAMA"));
        add(btnBack, BorderLayout.NORTH);

        JPanel pnlGenre = new JPanel(new GridLayout(1, 5, 5, 5));
        pnlGenre.setBackground(pinkPastel);
        for (String g : AppUtama.databaseBuku.keySet()) {
            JButton b = new JButton(g);
            b.addActionListener(e -> bukaManajemenStok(g));
            pnlGenre.add(b);
        }
        add(pnlGenre, BorderLayout.CENTER);

        JButton btnTambahBaru = new JButton("TAMBAH BUKU BARU (+)");
        btnTambahBaru.setBackground(hijauNeon);
        btnTambahBaru.addActionListener(e -> {
            JTextField fJudul = new JTextField();
            JTextField fHarga = new JTextField();
            JTextField fStok = new JTextField();
            JComboBox<String> fGenre = new JComboBox<>(AppUtama.databaseBuku.keySet().toArray(new String[0]));

            Object[] form = {"Judul:", fJudul, "Harga:", fHarga, "Stok:", fStok, "Genre:", fGenre};
            int res = JOptionPane.showConfirmDialog(null, form, "INPUT BUKU BARU", JOptionPane.OK_CANCEL_OPTION);

            if (res == JOptionPane.OK_OPTION) {
                String g = (String) fGenre.getSelectedItem();
                AppUtama.databaseBuku.get(g).add(new Buku(fJudul.getText(), Double.parseDouble(fHarga.getText()), Integer.parseInt(fStok.getText()), g));
                JOptionPane.showMessageDialog(this, "Buku Berhasil Ditambahkan ke Menu Utama!");
            }
        });
        add(btnTambahBaru, BorderLayout.SOUTH);
        revalidate(); repaint();
    }

    private void bukaManajemenStok(String genre) {
        JDialog win = new JDialog();
        win.setTitle("Edit Stok: " + genre);
        win.setSize(600, 400);

        String[] col = {"Judul", "Stok", "Aksi"};
        DefaultTableModel model = new DefaultTableModel(col, 0);
        for (Buku b : AppUtama.databaseBuku.get(genre)) {
            model.addRow(new Object[]{b.getJudul(), b.getStok(), "EDIT"});
        }
        JTable table = new JTable(model);

        JPanel pnlTombol = new JPanel();
        JButton btnPlus = new JButton("+ TAMBAH STOK");
        btnPlus.setBackground(hijauNeon);
        JButton btnMin = new JButton("- KURANG STOK");
        btnMin.setBackground(merahNeon);

        btnPlus.addActionListener(e -> {
            int r = table.getSelectedRow();
            if(r != -1) {
                Buku b = AppUtama.databaseBuku.get(genre).get(r);
                b.setStok(b.getStok() + 1);
                model.setValueAt(b.getStok(), r, 1);
            }
        });

        btnMin.addActionListener(e -> {
            int r = table.getSelectedRow();
            if(r != -1) {
                Buku b = AppUtama.databaseBuku.get(genre).get(r);
                if(b.getStok() > 0) {
                    b.setStok(b.getStok() - 1);
                    model.setValueAt(b.getStok(), r, 1);
                }
            }
        });

        pnlTombol.add(btnPlus); pnlTombol.add(btnMin);
        win.add(new JScrollPane(table), BorderLayout.CENTER);
        win.add(pnlTombol, BorderLayout.SOUTH);
        win.setLocationRelativeTo(null);
        win.setVisible(true);
    }
}

// ==========================================
// 4. MENU ADMIN
// ==========================================
class PanelAdmin extends MenuPanel {
    private boolean loggedIn = false;

    @Override
    public void refreshData() {
        if (!loggedIn) {
            JTextField user = new JTextField();
            JPasswordField pass = new JPasswordField();
            Object[] msg = {"Username:", user, "Password:", pass};
            int opt = JOptionPane.showConfirmDialog(null, msg, "ADMIN LOGIN", JOptionPane.OK_CANCEL_OPTION);
            if (opt == JOptionPane.OK_OPTION && user.getText().equals("sayabos") && new String(pass.getPassword()).equals("181818")) {
                loggedIn = true;
                showDashboard();
            } else {
                AppUtama.pindahMenu("UTAMA");
            }
        } else {
            showDashboard();
        }
    }

    private void showDashboard() {
        removeAll();
        JButton btnBack = new JButton("BACK");
        btnBack.addActionListener(e -> { loggedIn = false; AppUtama.pindahMenu("UTAMA"); });
        add(btnBack, BorderLayout.NORTH);

        JPanel pnlLap = new JPanel(new GridLayout(1, 2, 20, 20));
        pnlLap.setBackground(pinkPastel);

        JButton btnGudang = new JButton("LAPORAN GUDANG");
        JButton btnDuit = new JButton("LAPORAN PENDAPATAN");

        btnGudang.addActionListener(e -> {
            JDialog win = new JDialog();
            win.setTitle("LAPORAN STOK (READ-ONLY)");
            win.setSize(700, 500);
            JTabbedPane tabs = new JTabbedPane();
            for (String g : AppUtama.databaseBuku.keySet()) {
                DefaultTableModel m = new DefaultTableModel(new Object[]{"Judul", "Sisa Stok"}, 0) {
                    @Override public boolean isCellEditable(int r, int c) { return false; }
                };
                for (Buku b : AppUtama.databaseBuku.get(g)) m.addRow(new Object[]{b.getJudul(), b.getStok()});
                tabs.addTab(g, new JScrollPane(new JTable(m)));
            }
            win.add(tabs);
            win.setLocationRelativeTo(null);
            win.setVisible(true);
        });

        btnDuit.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("LAPORAN PENDAPATAN\n==================\n");
            double totalAll = 0;
            for(String s : AppUtama.laporanTransaksi) {
                sb.append(s).append("\n");
                try { totalAll += Double.parseDouble(s.split("Total: ")[1].split(" \\|")[0]); } catch(Exception ex){}
            }
            sb.append("==================\nTOTAL: Rp ").append(totalAll);
            JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(sb.toString())));
        });

        pnlLap.add(btnGudang); pnlLap.add(btnDuit);
        add(pnlLap, BorderLayout.CENTER);
        revalidate(); repaint();
    }
}