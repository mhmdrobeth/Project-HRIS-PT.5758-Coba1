# Project HRIS - KelolaUserApp 🚀
**Sistem Informasi SDM (HRIS) Berbasis Android**

Halo! Ini adalah proyek pengembangan aplikasi HRIS sederhana yang saya bangun menggunakan **Kotlin** dan **Room Database**. Aplikasi ini dirancang untuk membantu HR atau Admin perusahaan dalam mengelola data karyawan, penggajian, hingga proses rekrutmen secara lokal (offline-first).

Proyek ini dibuat sebagai bagian dari pembelajaran pengembangan aplikasi Android modern dengan arsitektur yang bersih dan efisien.

---

## 🛠️ Fitur Utama

- **Manajemen Karyawan (Admin Focus)**:
  - Create, Read, Update, & Delete (CRUD) data karyawan.
  - Monitoring status karyawan (Aktif/Hadir/Terlambat).
- **Sistem Penggajian (Payroll)**:
  - Pencatatan gaji pokok dan tunjangan.
  - Riwayat gaji bulanan dengan format mata uang Rupiah otomatis.
- **Modul Rekrutmen**:
  - Manajemen daftar kandidat pelamar kerja secara terorganisir.
- **HR AI Analytics (Beta)**:
  - Analisis data sederhana untuk melihat statistik SDM di dashboard.
- **Local Persistence**:
  - Menggunakan **Room Database** (Versi 2) untuk penyimpanan data yang aman di dalam perangkat.

---

## 🏗️ Teknologi yang Digunakan

- **Bahasa**: [Kotlin](https://kotlinlang.org/)
- **Database**: [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- **UI Framework**: Material Design 3 (M3)
- **Library Lainnya**:
  - Retrofit (Persiapan integrasi API)
  - ViewBinding & ConstraintLayout
  - RecyclerView & Adapters

---

## 📸 Tampilan Aplikasi
*(Tambahkan screenshot aplikasi di sini nanti)*

| Dashboard Utama | Edit Profil Karyawan | Daftar Payroll |
|---|---|---|
| [Screenshot 1] | [Screenshot 2] | [Screenshot 3] |

---

## ⚙️ Cara Menjalankan Proyek

1. Clone repositori ini:
   ```bash
   git clone https://github.com/mhmdrobeth/Project-HRIS-PT.5758-Coba1.git
   ```
2. Buka di **Android Studio** (Versi Hedgehog atau yang terbaru).
3. Biarkan Gradle melakukan sinkronisasi otomatis.
4. Jalankan menggunakan Emulator atau Perangkat Android asli (Min SDK 24 / Android 7.0).

---

## 📝 Catatan Pengembangan
Proyek ini masih dalam tahap pengembangan. Fokus utama saat ini adalah penguatan validasi data dan pengembangan logika analitik yang lebih mendalam untuk fitur "HR AI".

**Author:** [Nama/Username Anda]  
*Project ini dikembangkan untuk tujuan belajar dan portofolio pengembangan Android.*
