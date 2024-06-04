-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jun 04, 2024 at 09:36 AM
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
('88503003-2212-11ef-b43f-00e18ce201d5', 'Hajime', NULL, 'Isayama', NULL);

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
  `image_url` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`id`, `isbn`, `title`, `category`, `author_id`, `image_url`) VALUES
(1, '1111111111111', 'Attack on Titan', 'fictional', '88503003-2212-11ef-b43f-00e18ce201d5', NULL),
(2, '2222222222', 'Book Test', 'fictional', '88503003-2212-11ef-b43f-00e18ce201d5', NULL),
(3, '3333333333333', 'Hello World', 'fictional', '88503003-2212-11ef-b43f-00e18ce201d5', NULL),
(4, '4444444444444', 'Goodbye World', 'fictional', '88503003-2212-11ef-b43f-00e18ce201d5', NULL),
(5, '5555555555555', 'Frieren: Beyond Journey\'s End', 'fictional', '88503003-2212-11ef-b43f-00e18ce201d5', NULL),
(6, '6667877843', 'Hello World Goodbye', 'fictional', '88503003-2212-11ef-b43f-00e18ce201d5', NULL);

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
(1, '2024-06-04', '2024-06-07', NULL, 1, 'df228bed-21af-11ef-b516-00e18ce201d5'),
(2, '2024-06-04', '2024-06-07', NULL, 5, 'df228bed-21af-11ef-b516-00e18ce201d5');

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

-- --------------------------------------------------------

--
-- Table structure for table `departments`
--

CREATE TABLE `departments` (
  `id` varchar(50) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `penalties`
--

CREATE TABLE `penalties` (
  `id` int(11) NOT NULL,
  `amount` decimal(10,2) UNSIGNED NOT NULL,
  `user_role` enum('student','teacher') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
('a12345600', 'c1-bcsad', 'c07ac68f-21ad-11ef-b516-00e18ce201d5'),
('a12345601', 'c1-bcsad', 'df228bed-21af-11ef-b516-00e18ce201d5');

-- --------------------------------------------------------

--
-- Table structure for table `teachers`
--

CREATE TABLE `teachers` (
  `id` varchar(100) NOT NULL,
  `department_id` varchar(50) NOT NULL,
  `user_id` char(36) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
('02b07017-21a7-11ef-b516-00e18ce201d5', 'Aaron', NULL, 'Melendres', NULL, 'student', 'password', NULL, 'aaron@gmail.com'),
('1d363d3d-21a9-11ef-b516-00e18ce201d5', 'Lorena', NULL, 'Sanchez', NULL, 'student', 'password', NULL, 'lorena@gmail.com'),
('372ae01a-21a1-11ef-b516-00e18ce201d5', 'Admin', NULL, 'User', NULL, 'admin', 'password', NULL, 'admin@gmail.com'),
('c07ac68f-21ad-11ef-b516-00e18ce201d5', 'Samantha', NULL, 'Oris', NULL, 'student', 'password', NULL, 'samantha@gmail.com'),
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
  ADD KEY `author_id` (`author_id`);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `book_borrows`
--
ALTER TABLE `book_borrows`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `book_penalties`
--
ALTER TABLE `book_penalties`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `penalties`
--
ALTER TABLE `penalties`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `books`
--
ALTER TABLE `books`
  ADD CONSTRAINT `books_ibfk_1` FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`) ON DELETE CASCADE;

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
