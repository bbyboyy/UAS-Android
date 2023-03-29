-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 03, 2022 at 07:35 AM
-- Server version: 10.4.21-MariaDB
-- PHP Version: 8.0.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `xyzinternship`
--

-- --------------------------------------------------------

--
-- Table structure for table `projects`
--

CREATE TABLE `projects` (
  `idproject` int(11) NOT NULL,
  `namaproject` text NOT NULL,
  `descpro` text NOT NULL,
  `start` varchar(100) NOT NULL,
  `end` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `projects`
--

INSERT INTO `projects` (`idproject`, `namaproject`, `descpro`, `start`, `end`) VALUES
(1, 'Website Showroom Mobil Sinergi', 'Website Showroom Mobil Sinergi merupakan sebuah website yang melayani kebutuhan customer dalam hal pemesanan ataupun hanya sekedar melihat-lihat mobil apa yang sedang dijual di showroom.Selain itu website ini juga mempunyai fitur contact us dimana pelanggan bisa berkomunikasi langsung dengan bagian showroom untuk menanyakan perihal kendaraan yang mereka incar.', '30 Desember 2021', '3 Maret 2022'),
(2, 'Manajemen Warung Nasi Bu Ijuk', 'Warung Nasi bu Ijuk adalah warung tradisional yang belum punya sistem yang terkomputerisasi, sehingga semuanya masih dilakukan secara tradisional dan tidak rapih.Tim manajemen akan membantu Warung ijuk membangun sistem yang lebih baik serta terkomputerisasi', '5 januari 2022', '25 April 2022'),
(3, 'Manajemen Klinik Usada Insani', 'Klinik Usada Insani pada mulanya menggunakan sistem biasa (tradisional) untuk perhitungan biaya serta jadwal dokter, kita diminta untuk bisa membuatkan sistem yang running well serta punya on time yang 24 jam sehingga bisa melayani pasien secara optimal', '1 Februari 2022', '5 Juni 2022');

-- --------------------------------------------------------

--
-- Table structure for table `userpro`
--

CREATE TABLE `userpro` (
  `idrelasi` int(11) NOT NULL,
  `idproject` int(11) NOT NULL,
  `iduser` int(11) NOT NULL,
  `jobdesk` varchar(300) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `userpro`
--

INSERT INTO `userpro` (`idrelasi`, `idproject`, `iduser`, `jobdesk`) VALUES
(2, 2, 12, 'Capek 1'),
(3, 3, 6, 'Capek 2'),
(4, 1, 3, 'Capek 3'),
(5, 2, 5, 'Capek 4'),
(6, 2, 2, 'Nyoba'),
(7, 3, 2, 'Nyoba 1'),
(8, 1, 6, 'HEHEHE');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `nama` text NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `alamat` text NOT NULL,
  `email` text NOT NULL,
  `notelp` text NOT NULL,
  `divisi` text NOT NULL,
  `status` text NOT NULL,
  `about` varchar(150) NOT NULL,
  `foto` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `nama`, `username`, `password`, `alamat`, `email`, `notelp`, `divisi`, `status`, `about`, `foto`) VALUES
(1, 'Sipalingadmin', 'admin', 'admin123', 'tangerang kota', 'admin@xyz.com', '081232949506', 'admin', 'Karyawan Tetap', '', ''),
(2, 'Filbert Amadea Shan Noel', 'filbertxamadea', 'filbert321', 'Regensi Tangerang Poris 2', 'filbertxamadea@gmail.com', '0823234354654', 'IT Web', 'Magang', 'Saya Filbert Amadea, saya pekerja yang penuh tanggung jawab, dan perfeksionis', ''),
(3, 'DonixWawan', 'doniawan', 'doni123', 'Pantai mutiara', 'doniwan@gmail.com', '082345211234', 'IT Web', 'Magang', 'Saya Doni Awan, saya pekerja yang jujur dan sangat menjunjung ketepatan waktu', ''),
(4, 'Agus Yudhoyono', 'agusyudo', 'agus123', 'Pik 2 elit', 'agusyud@gmail.com', '0873647596723', 'IT Web', 'Magang', 'Saya Agus Yudhoyono, saya pekerja yang sangat kompeten dengan tanggung jawab yang diberikan', ''),
(5, 'Afiyah S. Arief', 'afiyahs', 'afi1234', 'Cimone Indah', 'afiyah@gmail.com', '082345234521', 'IT UX / UI', 'Magang', 'Saya Afiyah, saya sangat mengutamakan UI yang baik di aplikasi yang saya desain.', ''),
(6, 'Prisilia I. C. B. C.', 'prisiliaxd', 'prisil123', 'Cina town Square', 'prisilia@gmail.com', '025763845217', 'IT UX / UI', 'Magang', 'Saya Prisilia, ni hao! , saya orang yang bisa diberi banyak tanggung jawab dan siap mengerjakan apapun.', ''),
(7, 'Nobita Nobi', 'nobitaxxx', 'nobinobi123', 'Takaoka, Toyama', 'nobitanobi@gmail.com', '086738490323', 'Akutansi', 'Magang', 'Saya Nobita, saya sedikit malas jika bertemu orang yang kurang kompeten, dan saya suka mencari jalan pintas dalam masalah.', ''),
(8, 'Shizuka Minamoto', 'shizukaaaeee', 'shizukaka12', 'Takaoka, Toyama', 'shizukaee@gmail.com', '0865544332211', 'Akutansi', 'Magang', 'Saya Shizuka, saya sangat menyukai kerjasama tim dan saya pintar dalam hal Leadership', ''),
(9, 'Suneo Honekawa', 'suneooox', 'suneo123', 'Takaoka, Toyama', 'suneoneo@gmail.com', '089876345234', 'Akutansi', 'Magang', 'Saya Suneo, saya adalah orang yang periang dan bisa pintar beradaptasi dengan orang lain.', ''),
(10, 'Takeshi Gouda', 'giantxxx', 'Giant123', 'Takaoka, Toyama', 'giantdor@gmail.com', '081234567890', 'Manajemen', 'Magang', 'Saya Takeshi atau bisa dipanggil giant, saya orang yang berisik tapi suka diberikan tanggung jawab yang besar.', ''),
(11, 'Dekisugi Hidetoshi', 'dekisugigi', 'deki345', 'Takaoka, Toyama', 'dekisugi@gmail.com', '0876523231451', 'Manajemen', 'Magang', 'Saya Dekisugi, saya sangat pintar dalam mengolah angka serta mempredisiksi kerja sistem.', ''),
(12, 'Hashirama Senju', 'hashiramaaa', 'hashirama324', 'Konohagakure, Banten', 'hashiramarama@gmail.com', '085423485923', 'Manajemen', 'Magang', 'Saya Hashirama, saya suka menanggung pekerjaan orang dan bisa menyelesaikan pekerjaan dengan cepat.', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `projects`
--
ALTER TABLE `projects`
  ADD PRIMARY KEY (`idproject`);

--
-- Indexes for table `userpro`
--
ALTER TABLE `userpro`
  ADD PRIMARY KEY (`idrelasi`),
  ADD KEY `idproject` (`idproject`),
  ADD KEY `iduser` (`iduser`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `projects`
--
ALTER TABLE `projects`
  MODIFY `idproject` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `userpro`
--
ALTER TABLE `userpro`
  MODIFY `idrelasi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `userpro`
--
ALTER TABLE `userpro`
  ADD CONSTRAINT `userpro_ibfk_1` FOREIGN KEY (`iduser`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `userpro_ibfk_2` FOREIGN KEY (`idproject`) REFERENCES `projects` (`idproject`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;