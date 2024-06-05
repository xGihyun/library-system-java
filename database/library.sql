-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jun 05, 2024 at 03:03 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `library`
--

-- --------------------------------------------------------

--
-- Table structure for table `authors`
--

CREATE TABLE `authors` (
  `id` char(36) NOT NULL DEFAULT uuid(),
  `first_name` varchar(255) NOT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) NOT NULL,
  `suffix_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `authors`
--

INSERT INTO `authors` (`id`, `first_name`, `middle_name`, `last_name`, `suffix_name`) VALUES
('88503003-2212-11ef-b43f-00e18ce201d5', 'Hajime', NULL, 'Isayama', NULL),
('e635beaa-227f-11ef-9ec9-00e18ce201d5', 'Sun', NULL, 'Tzu', NULL),
('e635c168-227f-11ef-9ec9-00e18ce201d5', 'Anne', NULL, 'Frank', NULL),
('e635c1bb-227f-11ef-9ec9-00e18ce201d5', 'Michelle', NULL, 'Obama', NULL),
('e635c2aa-227f-11ef-9ec9-00e18ce201d5', 'Tara', NULL, 'Westover', NULL),
('e635c2c8-227f-11ef-9ec9-00e18ce201d5', 'James', NULL, 'Clear', NULL),
('e635c2e4-227f-11ef-9ec9-00e18ce201d5', 'Donald', NULL, 'Knuth', NULL),
('e635c2fc-227f-11ef-9ec9-00e18ce201d5', 'Patrick', NULL, 'Fitzpatrick', NULL),
('e635c319-227f-11ef-9ec9-00e18ce201d5', 'Philip', NULL, 'Kotler', NULL),
('e635c333-227f-11ef-9ec9-00e18ce201d5', 'Kevin', 'Lane', 'Keller', NULL),
('e635c34f-227f-11ef-9ec9-00e18ce201d5', 'Thomas', NULL, 'Cormen', NULL),
('e635c368-227f-11ef-9ec9-00e18ce201d5', 'Gilbert', NULL, 'Strang', NULL),
('e635c385-227f-11ef-9ec9-00e18ce201d5', 'Gabriel', 'García', 'Márquez', NULL),
('e635c39f-227f-11ef-9ec9-00e18ce201d5', 'Madeleine', NULL, 'L\'Engle', NULL),
('e635c481-227f-11ef-9ec9-00e18ce201d5', 'Markus', NULL, 'Zusak', NULL),
('e635c49d-227f-11ef-9ec9-00e18ce201d5', 'Leo', NULL, 'Tolstoy', NULL),
('e635c4b5-227f-11ef-9ec9-00e18ce201d5', 'Frank', NULL, 'Herbert', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `id` int(11) NOT NULL,
  `isbn` varchar(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `category` enum('fictional','non-fictional','academic') NOT NULL,
  `author_id` char(36) NOT NULL,
  `image_url` text DEFAULT NULL,
  `copyright` int(11) NOT NULL,
  `publisher_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`id`, `isbn`, `title`, `category`, `author_id`, `image_url`, `copyright`, `publisher_id`) VALUES
(7, '0060883287', 'One Hundred Years of Solitude', 'fictional', 'e635c385-227f-11ef-9ec9-00e18ce201d5', 'one-hundred-years-of-solitude.jpg', 1967, 1),
(36, '0312367541', 'A Wrinkle in Time', 'fictional', 'e635c39f-227f-11ef-9ec9-00e18ce201d5', 'a-wrinkle-in-time.jpg', 1962, 2),
(37, '0375831003', 'The Book Thief', 'fictional', 'e635c481-227f-11ef-9ec9-00e18ce201d5', 'the-book-thief.jpg', 2005, 3),
(38, '0140447934', 'War and Peace', 'fictional', 'e635c49d-227f-11ef-9ec9-00e18ce201d5', 'war-and-peace.jpg', 1865, 4),
(62, '0441013593', 'Dune', 'fictional', 'e635c4b5-227f-11ef-9ec9-00e18ce201d5', 'dune.jpg', 1865, 5),
(63, '1599869772', 'The Art of War', 'non-fictional', 'e635beaa-227f-11ef-9ec9-00e18ce201d5', 'the-art-of-war.jpg', 1963, 6),
(64, '9780141032', 'The Diary of a Young Girl', 'non-fictional', 'e635c168-227f-11ef-9ec9-00e18ce201d5', 'the-diary-of-a-young-girl.jpg', 1947, 7),
(65, '9781524763', 'Becoming', 'non-fictional', 'e635c1bb-227f-11ef-9ec9-00e18ce201d5', 'becoming.jpg', 2018, 8),
(66, '9780399590', 'Educated: A Memoir', 'non-fictional', 'e635c2aa-227f-11ef-9ec9-00e18ce201d5', 'educated.jpg', 2018, 9),
(67, '9780735212', 'Atomic Habits', 'non-fictional', 'e635c2c8-227f-11ef-9ec9-00e18ce201d5', 'atomic-habits.jpg', 2018, 10),
(68, '0201896831', 'The Art of Computer Programming', 'academic', 'e635c2e4-227f-11ef-9ec9-00e18ce201d5', 'the-art-of-computer-programming.png', 1962, 11),
(69, '0821847916', 'Advanced Calculus', 'academic', 'e635c2fc-227f-11ef-9ec9-00e18ce201d5', 'advanced-calculus.jpg', 1988, 12),
(70, '0132102926', 'Marketing Management', 'academic', 'e635c319-227f-11ef-9ec9-00e18ce201d5', 'marketing-management.jpg', 2009, 13),
(71, '0262032937', 'Introduction to Algorithms', 'academic', 'e635c34f-227f-11ef-9ec9-00e18ce201d5', 'introduction-to-algorithms.jpg', 2001, 14),
(72, '0073057920', 'Linear Algebra and Its Applications', 'academic', 'e635c368-227f-11ef-9ec9-00e18ce201d5', 'linear-algebra-and-its-applications.jpg', 1980, 15);

-- --------------------------------------------------------

--
-- Table structure for table `book_borrows`
--

CREATE TABLE `book_borrows` (
  `id` int(11) NOT NULL,
  `borrowed_at` date NOT NULL DEFAULT curdate(),
  `due_date` date NOT NULL,
  `returned_at` date DEFAULT NULL,
  `book_id` int(11) NOT NULL,
  `user_id` char(36) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `book_borrows`
--

INSERT INTO `book_borrows` (`id`, `borrowed_at`, `due_date`, `returned_at`, `book_id`, `user_id`) VALUES
(13, '2024-06-04', '2024-06-07', '2024-06-05', 36, 'df228bed-21af-11ef-b516-00e18ce201d5'),
(15, '2024-05-27', '2024-05-30', '2024-06-05', 67, 'df228bed-21af-11ef-b516-00e18ce201d5'),
(16, '2024-06-05', '2024-06-08', '2024-06-06', 36, '7f5fa34a-c8b2-4a17-8656-99afe45d204d'),
(17, '2024-06-05', '2024-06-08', NULL, 38, '7f5fa34a-c8b2-4a17-8656-99afe45d204d'),
(18, '2024-06-05', '2024-06-08', NULL, 66, '7f5fa34a-c8b2-4a17-8656-99afe45d204d'),
(19, '2024-06-05', '2024-06-08', NULL, 65, '7f5fa34a-c8b2-4a17-8656-99afe45d204d'),
(20, '2024-06-05', '2024-06-08', NULL, 64, '7f5fa34a-c8b2-4a17-8656-99afe45d204d'),
(21, '2024-06-05', '2024-06-08', '2024-06-05', 36, 'df228bed-21af-11ef-b516-00e18ce201d5'),
(22, '2024-06-05', '2024-06-08', NULL, 62, 'b10af538-22b2-11ef-899a-1413338a5c68'),
(23, '2024-06-05', '2024-06-08', NULL, 63, 'b10af538-22b2-11ef-899a-1413338a5c68'),
(24, '2024-06-05', '2024-06-08', '2024-06-05', 37, 'df228bed-21af-11ef-b516-00e18ce201d5'),
(26, '2024-05-30', '2024-06-02', NULL, 7, 'df228bed-21af-11ef-b516-00e18ce201d5');

-- --------------------------------------------------------

--
-- Table structure for table `book_penalties`
--

CREATE TABLE `book_penalties` (
  `id` int(11) NOT NULL,
  `payment_amount` decimal(10,2) UNSIGNED NOT NULL,
  `book_borrow_id` int(11) NOT NULL,
  `penalty_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `book_penalties`
--

INSERT INTO `book_penalties` (`id`, `payment_amount`, `book_borrow_id`, `penalty_id`) VALUES
(4, 120.00, 15, 1);

-- --------------------------------------------------------

--
-- Table structure for table `departments`
--

CREATE TABLE `departments` (
  `id` varchar(50) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `departments`
--

INSERT INTO `departments` (`id`, `name`) VALUES
('ccis', 'College of Computing and Information Sciences'),
('cit', 'College of Information Technology'),
('cs', 'College of Science');

-- --------------------------------------------------------

--
-- Table structure for table `penalties`
--

CREATE TABLE `penalties` (
  `id` int(11) NOT NULL,
  `amount` decimal(10,2) UNSIGNED NOT NULL,
  `user_role` enum('student','teacher') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `penalties`
--

INSERT INTO `penalties` (`id`, `amount`, `user_role`) VALUES
(1, 20.00, 'student'),
(2, 0.00, 'teacher');

-- --------------------------------------------------------

--
-- Table structure for table `publishers`
--

CREATE TABLE `publishers` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `publishers`
--

INSERT INTO `publishers` (`id`, `name`) VALUES
(1, 'HarperCollins Publishers'),
(2, 'Farrar, Straus and Giroux'),
(3, 'Random House, Inc.'),
(4, 'Penguin Books'),
(5, 'Ace Books'),
(6, 'Oxford University Press'),
(7, 'Doubleday'),
(8, 'Crown'),
(9, 'Random House'),
(10, 'Avery'),
(11, 'Addison-Wesley Professional'),
(12, 'John Wiley & Sons, Inc.'),
(13, 'Pearson Education, Inc.'),
(14, 'MIT Press'),
(15, 'Harcourt, Inc.');

-- --------------------------------------------------------

--
-- Table structure for table `sections`
--

CREATE TABLE `sections` (
  `id` varchar(50) NOT NULL,
  `name` varchar(255) NOT NULL,
  `level` enum('elementary','junior-high-school','senior-high-school','college') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `sections`
--

INSERT INTO `sections` (`id`, `name`, `level`) VALUES
('acsad', 'ACSAD', 'college'),
('apdev', 'APDEV', 'college'),
('bcsad', 'BCSAD', 'college'),
('ccsad', 'CCSAD', 'college'),
('dcsad', 'DCSAD', 'college');

-- --------------------------------------------------------

--
-- Table structure for table `section_levels`
--

CREATE TABLE `section_levels` (
  `id` varchar(100) NOT NULL,
  `section_id` varchar(50) NOT NULL,
  `year_level_id` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `section_levels`
--

INSERT INTO `section_levels` (`id`, `section_id`, `year_level_id`) VALUES
('c1-acsad', 'acsad', 'c1'),
('c1-apdev', 'apdev', 'c1'),
('c1-bcsad', 'bcsad', 'c1'),
('c1-ccsad', 'ccsad', 'c1'),
('c1-dcsad', 'dcsad', 'c1');

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `id` varchar(100) NOT NULL,
  `section_level_id` varchar(100) NOT NULL,
  `user_id` char(36) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`id`, `section_level_id`, `user_id`) VALUES
('a11111111', 'c1-bcsad', '79c9c30a-fc01-4f71-9f8d-5cecda72cc4d'),
('A12344924', 'c1-apdev', '4be89f3f-22b1-11ef-899a-1413338a5c68'),
('A12345574', 'c1-bcsad', '4be87058-22b1-11ef-899a-1413338a5c68'),
('A12345595', 'c1-bcsad', 'cf0ef484-22af-11ef-899a-1413338a5c68'),
('A12345600', 'c1-bcsad', 'c07ac68f-21ad-11ef-b516-00e18ce201d5'),
('a12345601', 'c1-bcsad', 'df228bed-21af-11ef-b516-00e18ce201d5'),
('A12345613', 'c1-acsad', '4be894b0-22b1-11ef-899a-1413338a5c68'),
('A12345642', 'c1-bcsad', '4be87a0b-22b1-11ef-899a-1413338a5c68'),
('A12345643', 'c1-acsad', '98d5639a-22b0-11ef-899a-1413338a5c68'),
('A12345678', 'c1-bcsad', '3842aaa4-22b0-11ef-899a-1413338a5c68'),
('A12345750', 'c1-acsad', '4be8a483-22b1-11ef-899a-1413338a5c68'),
('A12345923', 'c1-acsad', '4be899f4-22b1-11ef-899a-1413338a5c68'),
('A62345602', 'c1-acsad', '4be88f48-22b1-11ef-899a-1413338a5c68'),
('K12151881', 'c1-bcsad', '4be88152-22b1-11ef-899a-1413338a5c68'),
('K12153553', 'c1-bcsad', '4be888c9-22b1-11ef-899a-1413338a5c68');

-- --------------------------------------------------------

--
-- Table structure for table `teachers`
--

CREATE TABLE `teachers` (
  `id` varchar(100) NOT NULL,
  `department_id` varchar(50) NOT NULL,
  `user_id` char(36) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `teachers`
--

INSERT INTO `teachers` (`id`, `department_id`, `user_id`) VALUES
('T111111111', 'ccis', 'b10af003-22b2-11ef-899a-1413338a5c68'),
('t12345', 'ccis', '7f5fa34a-c8b2-4a17-8656-99afe45d204d'),
('T22222222', 'ccis', 'b10ae8cd-22b2-11ef-899a-1413338a5c68'),
('T33333333', 'ccis', 'b10af538-22b2-11ef-899a-1413338a5c68');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` char(36) NOT NULL DEFAULT uuid(),
  `first_name` varchar(255) NOT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) NOT NULL,
  `suffix_name` varchar(255) DEFAULT NULL,
  `role` enum('student','teacher','admin') NOT NULL,
  `password` text NOT NULL,
  `avatar_url` text DEFAULT NULL,
  `email` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `first_name`, `middle_name`, `last_name`, `suffix_name`, `role`, `password`, `avatar_url`, `email`) VALUES
('372ae01a-21a1-11ef-b516-00e18ce201d5', 'Admin', NULL, 'User', NULL, 'admin', 'password', NULL, 'admin@gmail.com'),
('3842aaa4-22b0-11ef-899a-1413338a5c68', 'Lorena', NULL, 'Sanchez', NULL, 'student', 'password', NULL, 'lorena@gmail.com'),
('4be87058-22b1-11ef-899a-1413338a5c68', 'Rhenz', NULL, 'Ganotice', NULL, 'student', 'password', NULL, 'rhenz@gmail.com'),
('4be87a0b-22b1-11ef-899a-1413338a5c68', 'Shammy', NULL, 'Suyat', NULL, 'student', 'password', NULL, 'shammy@gmail.com'),
('4be88152-22b1-11ef-899a-1413338a5c68', 'Dan Ike', NULL, 'Estoque', NULL, 'student', 'password', NULL, 'dan@gmail.com'),
('4be888c9-22b1-11ef-899a-1413338a5c68', 'Craig', NULL, 'Naguit', NULL, 'student', 'password', NULL, 'craig@gmail.com'),
('4be88f48-22b1-11ef-899a-1413338a5c68', 'Julian Carlo', NULL, 'Ausa', NULL, 'student', 'password', NULL, 'julian@gmail.com'),
('4be894b0-22b1-11ef-899a-1413338a5c68', 'Charles', NULL, 'Lao', NULL, 'student', 'password', NULL, 'charles@gmail.com'),
('4be899f4-22b1-11ef-899a-1413338a5c68', 'Althea', NULL, 'Magpantay', NULL, 'student', 'password', NULL, 'althea@gmail.com'),
('4be89f3f-22b1-11ef-899a-1413338a5c68', 'Ashley', NULL, 'Gonzales', NULL, 'student', 'password', NULL, 'ashley@gmail.com'),
('4be8a483-22b1-11ef-899a-1413338a5c68', 'Rhussel', NULL, 'Combo', NULL, 'student', 'password', NULL, 'rhussel@gmail.com'),
('79c9c30a-fc01-4f71-9f8d-5cecda72cc4d', 'Eric', NULL, 'Lor', NULL, 'teacher', 'password', NULL, 'elor@gmail.com'),
('7f5fa34a-c8b2-4a17-8656-99afe45d204d', 'Hitori', NULL, 'Bocchi', NULL, 'teacher', 'password', NULL, 'bocchi@gmail.com'),
('98d5639a-22b0-11ef-899a-1413338a5c68', 'Emmarlon', NULL, 'Ogoc', NULL, 'student', 'password', NULL, 'emmarlon@gmail.com'),
('b10ae8cd-22b2-11ef-899a-1413338a5c68', 'Alexander', NULL, 'Pahayahay', NULL, 'teacher', 'password', NULL, 'alexander@gmail.com'),
('b10af003-22b2-11ef-899a-1413338a5c68', 'Jomariss', NULL, 'Plan', NULL, 'teacher', 'password', NULL, 'jomariss@gmail.com'),
('b10af538-22b2-11ef-899a-1413338a5c68', 'Rommel', NULL, 'Dorin', NULL, 'teacher', 'password', NULL, 'rommel@gmail.com'),
('c07ac68f-21ad-11ef-b516-00e18ce201d5', 'Samantha', NULL, 'Oris', NULL, 'student', 'password', NULL, 'samantha@gmail.com'),
('cf0ef484-22af-11ef-899a-1413338a5c68', 'Aaron Kenneth', NULL, 'Melendres', NULL, 'student', 'password', NULL, 'aaron@gmail.com'),
('df228bed-21af-11ef-b516-00e18ce201d5', 'Giordan', NULL, 'Nuez', NULL, 'student', 'password', NULL, 'gior@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `year_levels`
--

CREATE TABLE `year_levels` (
  `id` varchar(50) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `year_levels`
--

INSERT INTO `year_levels` (`id`, `name`) VALUES
('c1', 'First Year College'),
('c2', 'Second Year College'),
('c3', 'Third Year College'),
('c4', 'Fourth Year College'),
('g1', 'Grade 1'),
('g10', 'Grade 10'),
('g11', 'Grade 11'),
('g2', 'Grade 2'),
('g3', 'Grade 3'),
('g4', 'Grade 4'),
('g5', 'Grade 5'),
('g6', 'Grade 6'),
('g7', 'Grade 7'),
('g8', 'Grade 8'),
('g9', 'Grade 9');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `authors`
--
ALTER TABLE `authors`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `isbn` (`isbn`),
  ADD KEY `author_id` (`author_id`),
  ADD KEY `publisher_id` (`publisher_id`);

--
-- Indexes for table `book_borrows`
--
ALTER TABLE `book_borrows`
  ADD PRIMARY KEY (`id`),
  ADD KEY `book_id` (`book_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `book_penalties`
--
ALTER TABLE `book_penalties`
  ADD PRIMARY KEY (`id`),
  ADD KEY `book_borrow_id` (`book_borrow_id`),
  ADD KEY `penalty_id` (`penalty_id`);

--
-- Indexes for table `departments`
--
ALTER TABLE `departments`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `penalties`
--
ALTER TABLE `penalties`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `publishers`
--
ALTER TABLE `publishers`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sections`
--
ALTER TABLE `sections`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `section_levels`
--
ALTER TABLE `section_levels`
  ADD PRIMARY KEY (`id`),
  ADD KEY `section_id` (`section_id`),
  ADD KEY `year_level_id` (`year_level_id`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`id`),
  ADD KEY `section_level_id` (`section_level_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `teachers`
--
ALTER TABLE `teachers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `department_id` (`department_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `year_levels`
--
ALTER TABLE `year_levels`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `books`
--
ALTER TABLE `books`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=73;

--
-- AUTO_INCREMENT for table `book_borrows`
--
ALTER TABLE `book_borrows`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `book_penalties`
--
ALTER TABLE `book_penalties`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `penalties`
--
ALTER TABLE `penalties`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `publishers`
--
ALTER TABLE `publishers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `books`
--
ALTER TABLE `books`
  ADD CONSTRAINT `books_ibfk_1` FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `publisher_id` FOREIGN KEY (`publisher_id`) REFERENCES `publishers` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `book_borrows`
--
ALTER TABLE `book_borrows`
  ADD CONSTRAINT `book_borrows_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `book_borrows_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `book_penalties`
--
ALTER TABLE `book_penalties`
  ADD CONSTRAINT `book_penalties_ibfk_1` FOREIGN KEY (`book_borrow_id`) REFERENCES `book_borrows` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `book_penalties_ibfk_2` FOREIGN KEY (`penalty_id`) REFERENCES `penalties` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `section_levels`
--
ALTER TABLE `section_levels`
  ADD CONSTRAINT `section_levels_ibfk_1` FOREIGN KEY (`section_id`) REFERENCES `sections` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `section_levels_ibfk_2` FOREIGN KEY (`year_level_id`) REFERENCES `year_levels` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `students`
--
ALTER TABLE `students`
  ADD CONSTRAINT `students_ibfk_1` FOREIGN KEY (`section_level_id`) REFERENCES `section_levels` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `students_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `teachers`
--
ALTER TABLE `teachers`
  ADD CONSTRAINT `teachers_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `teachers_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
