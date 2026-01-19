package com.karyalinman;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class AppUtama extends JFrame {
    public static CardLayout cardLayout = new CardLayout();
    public static JPanel mainPanel = new JPanel(cardLayout);
    public static Map<String, List<Buku>> databaseBuku = new LinkedHashMap<>();
    public static List<String> antrianAktif = new ArrayList<>();
    public static Map<String, List<ItemKeranjang>> keranjangPelanggan = new HashMap<>();
    public static List<String> laporanTransaksi = new ArrayList<>();

    public AppUtama() {
        initData();
        setTitle("KaryaLinman Book Store");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPanel.add(new PanelUtama(), "UTAMA");
        mainPanel.add(new PanelKasir(), "KASIR");
        mainPanel.add(new PanelGudang(), "GUDANG");
        mainPanel.add(new PanelAdmin(), "ADMIN");

        add(mainPanel);
        setVisible(true);
    }

    private void initData() {
        String[] genres = {"Romantis", "Horor", "Misteri", "Petualangan", "Pendidikan"};

        // Judul Riil per Genre
        String[][] judulBuku = {
                // Romantis
                {"Hujan Bulan Juni", "Critical Eleven", "Antologi Rasa", "The Architecture of Love", "Dilan 1990",
                        "Milea", "Mariposa", "Dikta dan Hukum", "Garis Waktu", "Konspirasi Alam Semesta",
                        "Pulang", "Rindu", "Sepotong Senja", "Twivortiare", "Sunshine Becomes You",
                        "Winter in Tokyo", "Autumn in Paris", "Spring in London", "Summer in Seoul", "Kala",
                        "Amira", "Cinta di Dalam Gelas", "Padang Bulan", "Surat Kecil Untuk Tuhan", "Ayat-Ayat Cinta",
                        "Ketika Cinta Bertasbih", "Hafalan Shalat Delisa", "Moga Bunda Disayang Allah", "Satu Hari di 2018", "Bumi Manusia",
                        "Anak Semua Bangsa", "Jejak Langkah", "Rumah Kaca", "Perahu Kertas", "Rapijali",
                        "Aroma Karsa", "Filosofi Kopi", "Supernova", "Partikel", "Gelombang",
                        "Petir", "Akar", "Ksatria Puteri dan Bintang Jatuh", "Cantik itu Luka", "Lelaki Harimau",
                        "Seperti Dendam Rindu Harus Dibayar Tuntas", "O", "Tarian Bumi", "Saman", "Larung"},

                // Horor
                {"Danur", "Maddah", "Sunyaruri", "Asih", "Hendrick",
                        "Hans", "Janshen", "William", "Ivanna", "Peter",
                        "KKN di Desa Penari", "Sewu Dino", "Janur Ireng", "Lemah Layat", "Padusan Darah",
                        "Kisah Tanah Jawa", "Jagad Lelembut", "Pocong Rumah Sebelah", "Titisan", "Gerbang Dialog Danur",
                        "Wingit", "Ghost Writer", "Hantu Bangku Kosong", "Lonceng Kematian", "Misteri Rumah Tua",
                        "Penunggu Jenazah", "Tumbal", "Pesugihan", "Malam Jumat Kliwon", "Satu Suro",
                        "Suzzanna", "Kuntilanak Merah", "Sundel Bolong", "Genderuwo", "Wewe Gombel",
                        "Leak Bali", "Nyi Roro Kidul", "Keranda Terbang", "Pabrik Gula", "Rumah Kentang",
                        "Ambulans Tua", "Terowongan Casablanca", "Mall Klender", "Lintasan Kereta", "Kamar 308",
                        "Pohon Tua", "Sumur Tua", "Desa Gaib", "Hutan Terlarang", "Kuburan Massal"},

                // Misteri
                {"Sherlock Holmes", "Lupin", "Pembunuhan di Orient Express", "Misteri Roger Ackroyd", "Kematian di Nil",
                        "And Then There Were None", "Gadis dengan Tato Naga", "The Da Vinci Code", "Angels & Demons", "Inferno",
                        "The Lost Symbol", "Origin", "Digital Fortress", "Deception Point", "Gone Girl",
                        "The Girl on the Train", "Misteri Patung Garam", "Kucing Hitam", "Teka-teki Terakhir", "Saksi Mata",
                        "Bayangan di Balik Jendela", "Jejak Berdarah", "Konspirasi Gelap", "Ruang Terkunci", "Pesan Rahasia",
                        "Sandi Morse", "Detektif Cilik", "Misteri Lemari Tua", "Kunci yang Hilang", "Malam Berdarah",
                        "Topeng Kematian", "Surat Tanpa Nama", "Rahasia Pulau Terpencil", "Laboratorium Terlarang", "Eksperimen Gagal",
                        "Buku Harian Tua", "Cermin Retak", "Langkah Kaki di Atap", "Suara Tanpa Wujud", "Misteri Biara Lama",
                        "Kutukan Firaun", "Harta Karun Terkubur", "Pencurian Mahkota", "Skandal Besar", "Mata-mata Ekonomi",
                        "Operasi Senyap", "Infiltrasi", "Kode Rahasia", "Agenda Tersembunyi", "Dalang Kejahatan"},

                // Petualangan
                {"Laskar Pelangi", "Sang Pemimpi", "Edensor", "Maryamah Karpov", "5 CM",
                        "Negeri 5 Menara", "Ranah 3 Warna", "Rantau 1 Muara", "Titik Nol", "Garis Batas",
                        "Selimut Debu", "The Hobbit", "Lord of the Rings", "The Fellowship of the Ring", "The Two Towers",
                        "Return of the King", "Harry Potter", "Batu Bertuah", "Kamar Rahasia", "Tawanan Azkaban",
                        "Piala Api", "Orde Phoenix", "Pangeran Berdarah Campuran", "Relikui Kematian", "Narnia",
                        "Singa Penyihir dan Lemari", "Pangeran Caspian", "Petualangan Dawn Treader", "Kursi Perak", "Kuda dan Anak Manusia",
                        "Keponakan Penyihir", "Pertempuran Terakhir", "Percy Jackson", "Pencuri Petir", "Lautan Monster",
                        "Kutukan Titan", "Labirin Maut", "Dewa Terakhir", "Indiana Jones", "Harta Karun",
                        "Pengejaran Emas", "Ekspedisi Rimba", "Puncak Himalaya", "Lembah Amazon", "Gurun Sahara",
                        "Pelayaran Samudra", "Pulau Tak Berpenghuni", "Menembus Es", "Terjebak di Gua", "Kompas Ajaib"},

                // Pendidikan
                {"Matematika Dasar", "Fisika Modern", "Biologi Sel", "Kimia Organik", "Sejarah Dunia",
                        "Geografi Politik", "Ekonomi Makro", "Sosiologi Kontemporer", "Antropologi Budaya", "Psikologi Anak",
                        "Manajemen Bisnis", "Akuntansi Keuangan", "Hukum Perdata", "Hukum Pidana", "Teknik Informatika",
                        "Algoritma dan Struktur Data", "Basis Data", "Jaringan Komputer", "Kecerdasan Buatan", "Sistem Operasi",
                        "Filsafat Ilmu", "Logika Matematika", "Statistika Terapan", "Metodologi Penelitian", "Bahasa Indonesia",
                        "Bahasa Inggris Dasar", "Sastra Dunia", "Seni Rupa", "Teori Musik", "Pendidikan Kewarganegaraan",
                        "Agama dan Etika", "Kepemimpinan", "Komunikasi Massa", "Jurnalistik Dasar", "Public Speaking",
                        "Negosiasi Bisnis", "Pemasaran Digital", "Kewirausahaan", "Investasi Saham", "Perencanaan Keuangan",
                        "Gizi dan Kesehatan", "Anatomi Manusia", "Farmakologi", "Keperawatan Dasar", "Teknik Sipil",
                        "Arsitektur Hijau", "Pertanian Berkelanjutan", "Peternakan Modern", "Kelautan dan Perikanan", "Astronomi Dasar"}
        };

        for (int i = 0; i < genres.length; i++) {
            List<Buku> list = new ArrayList<>();
            for (int j = 0; j < 50; j++) {
                // Mengambil judul dari array di atas
                list.add(new Buku(judulBuku[i][j], 55000 + (j * 500), 20, genres[i]));
            }
            databaseBuku.put(genres[i], list);
        }
    }

    public static void pindahMenu(String nama) {
        cardLayout.show(mainPanel, nama);
        // Refresh data pada panel yang dituju
        Component[] comps = mainPanel.getComponents();
        for (Component c : comps) {
            if (c instanceof MenuPanel && c.isVisible()) {
                ((MenuPanel) c).refreshData();
            }
        }
    }

    public static void main(String[] args) {
        new AppUtama();
    }
}

class ItemKeranjang {
    Buku buku;
    int jumlah;
    ItemKeranjang(Buku b, int j) { this.buku = b; this.jumlah = j; }
}