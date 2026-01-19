package com.karyalinman;

public class Buku {
    private String judul;
    private double harga;
    private int stok;
    private String genre;

    public Buku(String judul, double harga, int stok, String genre) {
        this.judul = judul;
        this.harga = harga;
        this.stok = stok;
        this.genre = genre;
    }

    public String getJudul() { return judul; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
    public String getGenre() { return genre; }
}