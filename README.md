# katalon-petstore-web

Project web automation testing untuk aplikasi demo **JPetStore**
(<https://petstore.octoperf.com/actions/Catalog.action>) menggunakan **Katalon Studio**.

Project ini adalah Final Project Katalon Studio (Web Automation) yang menerapkan:
Hybrid Mode (Manual + Script), positive & negative testing, Data-Driven Testing (DDT),
dynamic data, Global Variable untuk credential, Test Suite, dan execution report.

---

## Tech Stack

| Item | Keterangan |
|---|---|
| Tool | Katalon Studio 10.4.3 |
| Bahasa | Groovy |
| Browser | Google Chrome (bisa juga Firefox / Edge) |
| Aplikasi yang diuji | JPetStore Demo – <https://petstore.octoperf.com> |

---

## Struktur Project

```
katalon-petstore-web/
├── Data/                         # Sumber test data (CSV) untuk DDT
├── Data Files/                   # Definisi Data File Katalon (.dat) -> menunjuk ke Data/*.csv
├── Keywords/petstore/            # Custom keyword (bagian "Script" dari Hybrid Mode)
│   ├── CommonKeywords.groovy     #   open browser, login, logout, verifikasi login, resolve token credential
│   ├── CartKeywords.groovy       #   add item to cart, parse & verifikasi harga
│   └── DataGenerator.groovy      #   generator data dinamis (registrasi akun)
├── Object Repository/            # Test Object dibuat manual per halaman (XPath, sebagian berparameter)
│   ├── Page_Header/  Page_Home/  Page_Login/  Page_Register/
│   ├── Page_Category/  Page_Product/  Page_Search/
│   └── Page_Cart/  Page_Checkout/
├── Profiles/default.glbl         # Global Variable: baseUrl, username, password, timeout
├── Scripts/                      # Script Groovy dari setiap test case
├── Test Cases/                   # Test case dikelompokkan per modul
│   ├── Authentication/  Catalog/  Cart/  Checkout/
├── Test Listeners/               # Logging eksekusi & cleanup browser saat test gagal
└── Test Suites/
    └── TS_PetStore_Regression    # Menjalankan seluruh test case + data binding DDT
```

---

## Test Coverage

| ID | Modul | Test Case | Tipe | Teknik | Iterasi |
|---|---|---|---|---|---|
| TC01 | Authentication | Login dengan credential valid, lalu logout | Positive | Global Variable | 1 |
| TC02 | Authentication | Login dengan credential tidak valid (username salah, password salah, field kosong) | Negative | DDT – `LoginInvalidData` | 6 |
| TC03 | Authentication | Registrasi akun baru → kembali ke halaman utama → login dengan akun baru → logout | Positive | Dynamic data (username unik tiap run) | 1 |
| TC04 | Catalog | Search produk: keyword valid, case-insensitive, tidak ditemukan, karakter spesial, keyword kosong | Positive + Negative | DDT – `SearchProductData` | 7 |
| TC05 | Catalog | Browse produk per kategori (Fish, Dogs, Cats, Reptiles, Birds) → detail produk | Positive | DDT – `CategoryData` | 5 |
| TC06 | Cart | Add item to cart → verifikasi deskripsi, qty, harga, sub total → remove item | Positive | DDT – `CartItemData` | 5 |
| TC07 | Cart | Update quantity: valid, besar, nol, negatif, non-numerik | Positive + Negative | DDT – `CartQuantityData` | 5 |
| TC08 | Checkout | Proceed to checkout tanpa login → ditolak & diarahkan ke Sign In | Negative | – | 1 |
| TC09 | Checkout | Login → add to cart → checkout → confirm → order berhasil | Positive (E2E) | Global Variable | 1 |

**Total: 9 test case, 32 iterasi eksekusi (19 positive, 13 negative).**

---

## Penerapan Ketentuan Final Project

| Ketentuan | Implementasi |
|---|---|
| Minimal 5 test case dengan **Hybrid Mode (Manual + Script)** | 9 test case. Step disusun dari built-in keyword `WebUI.*` + Test Object (tampil & bisa diedit di tab **Manual**), dilengkapi logika Groovy (if/else, loop, perhitungan harga) dan **custom keyword** di tab **Script**. |
| **Tidak menggunakan Record Web** | Semua Test Object dibuat manual dengan XPath yang stabil (atribut `name`, `id` container, teks) – bukan ID dinamis hasil recorder. Metadata test case: `recordOption = OTHER`. |
| Positive & negative case | Lihat kolom *Tipe* pada tabel Test Coverage. |
| **Data-Driven Testing (DDT)** | 5 Data File CSV di-binding ke variable test case pada level **Test Suite** (*Data Binding*), setiap baris data = 1 iterasi di report. |
| **Global Variable** untuk credential, hindari hardcode | `GlobalVariable.username`, `GlobalVariable.password`, `GlobalVariable.baseUrl`, `GlobalVariable.timeout` di `Profiles/default`. Data negative login memakai token `{GLOBAL_USERNAME}` / `{GLOBAL_PASSWORD}` yang di-resolve dari Global Variable, sehingga credential valid tidak ditulis di file data maupun script. |
| Jalankan seluruh test case melalui **Test Suite** | `Test Suites/TS_PetStore_Regression` berisi TC01–TC09. |
| Test Suite menghasilkan **execution report** | Katalon otomatis membuat report HTML, CSV, dan JUnit XML di folder `Reports/` setiap kali Test Suite dijalankan. |

---

## Test Data (DDT)

| Data File | Sumber CSV | Dipakai oleh | Kolom |
|---|---|---|---|
| `LoginInvalidData` | `Data/login_invalid_data.csv` | TC02 | scenario, username, password, expectedMessage |
| `SearchProductData` | `Data/search_product_data.csv` | TC04 | scenario, keyword, expectedResult, expectedProducts |
| `CategoryData` | `Data/category_data.csv` | TC05 | categoryId, categoryName, productId, productName |
| `CartItemData` | `Data/cart_item_data.csv` | TC06 | categoryId, productId, productName, itemId, itemDescription, listPrice |
| `CartQuantityData` | `Data/cart_quantity_data.csv` | TC07 | scenario, categoryId, productId, itemId, listPrice, quantity, expectedResult, expectedQuantity |

Untuk menambah skenario cukup tambahkan baris baru di file CSV – tidak perlu mengubah script.

Token khusus pada data:

| Token | Diganti dengan | Alasan |
|---|---|---|
| `{GLOBAL_USERNAME}` / `{GLOBAL_PASSWORD}` | `GlobalVariable.username` / `GlobalVariable.password` | Credential valid tidak ditulis di file data |
| `{EMPTY}` | string kosong | Katalon mengganti sel CSV yang kosong dengan *default value* variabel test case, sehingga nilai kosong harus ditulis eksplisit |

---

## Global Variable (Profile `default`)

| Nama | Default | Keterangan |
|---|---|---|
| `baseUrl` | `https://petstore.octoperf.com/actions/Catalog.action` | URL halaman utama |
| `username` | `j2ee` | Akun valid untuk login |
| `password` | `j2ee` | Password akun valid |
| `timeout` | `10` | Timeout (detik) untuk wait / verify |

> Ubah `username` & `password` di **Profiles > default** untuk memakai akun sendiri – tidak ada script yang perlu diubah.
> Saat Test Suite dijalankan, `@SetUp` di `Test Suites/TS_PetStore_Regression` memastikan akun tersebut bisa login;
> jika ditolak (mis. database server demo di-reset), akun diregistrasi ulang otomatis dengan credential yang sama.

---

## Cara Menjalankan

### Prasyarat
- Katalon Studio 10.x
- Google Chrome versi terbaru
- Koneksi internet (aplikasi yang diuji adalah website publik)

### Melalui Katalon Studio (GUI)
1. Clone repository:
   ```bash
   git clone https://github.com/<username>/katalon-petstore-web.git
   ```
2. Buka Katalon Studio → **File > Open Project** → pilih folder `katalon-petstore-web`.
3. Pastikan profile eksekusi **default** terpilih (dropdown profile di toolbar).
4. Buka **Test Suites > TS_PetStore_Regression**.
5. Klik tombol **Run** dan pilih **Chrome**.
6. Setelah selesai, buka tab **Reports** di Test Explorer untuk melihat hasil eksekusi.

### Melalui Command Line (Katalon Runtime Engine)
```bash
katalonc -noSplash -runMode=console -projectPath="<path>/katalon-petstore-web/katalon-petstore-web.prj" -retry=0 -testSuitePath="Test Suites/TS_PetStore_Regression" -browserType="Chrome" -executionProfile="default" -apiKey="<KATALON_API_KEY>" --config -webui.autoUpdateDrivers=true
```
> Eksekusi melalui command line membutuhkan lisensi Katalon Runtime Engine (KRE).

---

## Execution Report

Setiap eksekusi Test Suite menghasilkan report di:

```
Reports/<timestamp>/TS_PetStore_Regression/<timestamp>/
├── <timestamp>.html     # HTML report (ringkasan + detail step setiap iterasi)
├── <timestamp>.csv      # Ringkasan hasil dalam CSV
├── JUnit_Report.xml     # Format JUnit (untuk CI)
└── execution0.log       # Log detail eksekusi
```

Contoh report hasil eksekusi disimpan di folder [`docs/execution-report`](docs/execution-report).

---

## Catatan

- **TC03** membuat akun baru di website demo setiap kali dijalankan (username unik berbasis timestamp).
  JPetStore tidak menampilkan status login setelah registrasi (menu tetap "Sign In"), sehingga TC03
  memverifikasi akun baru dengan cara login menggunakan credential yang baru dibuat.
- **TC09** membuat order baru menggunakan akun pada Global Variable. Data kartu pembayaran memakai
  nilai *fake* default yang sudah diisi oleh aplikasi demo.
- Setiap test case membuka dan menutup browser sendiri sehingga bisa dijalankan terpisah maupun melalui
  Test Suite. Jika test gagal di tengah jalan, `Test Listeners/PetStoreTestListener` menutup browser
  agar iterasi berikutnya mulai dari kondisi bersih.
