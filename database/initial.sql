-- Authenticated users
CREATE TABLE IF NOT EXISTS users (
  id CHAR(36) PRIMARY KEY DEFAULT uuid(), -- UUID()

  first_name VARCHAR(255) NOT NULL,
  middle_name VARCHAR(255),
  last_name VARCHAR(255) NOT NULL,
  suffix_name VARCHAR(255), -- e.g. Jr., III., etc.
  role ENUM('student', 'teacher', 'admin') NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password TEXT NOT NULL,
  avatar_url TEXT
);

-- Possible departments for teachers
CREATE TABLE IF NOT EXISTS departments (
  -- The `name` in abbreviation and lower case
  -- e.g. "ccis"
  id VARCHAR(50) PRIMARY KEY, 

  -- e.g. "College of Computing and Information Sciences"
  name VARCHAR(255) NOT NULL 
);

CREATE TABLE IF NOT EXISTS teachers (
  id VARCHAR(100) PRIMARY KEY, -- Use employee ID (something similar to a student no.)

  department_id VARCHAR(50) NOT NULL,
  user_id CHAR(36) NOT NULL,

  FOREIGN KEY(department_id) REFERENCES departments(id) ON DELETE CASCADE,
  FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS year_levels (
  -- e.g. "g8", "g11", "1yc" etc.
  -- Feel free to come up with your own formula
  id VARCHAR(50) PRIMARY KEY, 

  -- e.g. "Grade 8", "Grade 11", "1st Year College" etc.
  name VARCHAR(255) NOT NULL 
);

INSERT INTO year_levels (id, name)
VALUES
    ('g1', 'Grade 1'),
    ('g2', 'Grade 2'),
    ('g3', 'Grade 3'),
    ('g4', 'Grade 4'),
    ('g5', 'Grade 5'),
    ('g6', 'Grade 6'),
    ('g7', 'Grade 7'),
    ('g8', 'Grade 8'),
    ('g9', 'Grade 9'),
    ('g10', 'Grade 10'),
    ('g11', 'Grade 11'),
    ('c1', 'First Year College'),
    ('c2', 'Second Year College'),
    ('c3', 'Third Year College'),
    ('c4', 'Fourth Year College');

CREATE TABLE IF NOT EXISTS sections (
  id VARCHAR(50) PRIMARY KEY, -- "acsad", "bcsad", "ccsad"
  name VARCHAR(255) NOT NULL, -- "ACSAD", "BCSAD", "CCSAD"
  level ENUM('elementary', 'junior-high-school', 'senior-high-school', 'college') NOT NULL
);

INSERT INSERT INTO sections (
  id, name, level
) VALUES ( "acsad", "ACSAD", "college" ), ("bcsad", "BCSAD", "college"), ("ccsad", "CCSAD", "college"), ("dcsad", "DCSAD", "college")

-- List of sections with their corresponding year level
CREATE TABLE IF NOT EXISTS section_levels (
  -- FORMULA: `year_level_id` + "-" + `section_id`
  -- e.g. "1-bcsad", "4-bcsad", OR "I-BCSAD", "IV-CCSAD" OR "g11-hello-world", "g12-goodbye-world"
  id VARCHAR(100) PRIMARY KEY, 

  section_id VARCHAR(50) NOT NULL,
  year_level_id VARCHAR(50) NOT NULL,

  FOREIGN KEY(section_id) REFERENCES sections(id) ON DELETE CASCADE,
  FOREIGN KEY(year_level_id) REFERENCES year_levels(id) ON DELETE CASCADE
);

INSERT INTO section_levels (id, section_id, year_level_id) 
VALUES ("c1-acsad", "acsad", "c1"), ("c1-bcsad", "bcsad", "c1"), ("c1-ccsad", "ccsad", "c1"), ("c1-dcsad", "dcsad", "c1")

CREATE TABLE IF NOT EXISTS students (
  id VARCHAR(100) PRIMARY KEY, -- Use student no.

  -- Use `section_level_id` as it's already related to its corresponding section and year level.
  -- It's impossible for a student to not have a section or a year level.
  section_level_id VARCHAR(100) NOT NULL,
  user_id CHAR(36) NOT NULL,

  FOREIGN KEY(section_level_id) REFERENCES section_levels(id) ON DELETE CASCADE,
  FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO students (id, section_level_id, user_id)
VALUES ("a12345601", "c1-bcsad", "df228bed-21af-11ef-b516-00e18ce201d5"),
("a12345600", "c1-bcsad", "c07ac68f-21ad-11ef-b516-00e18ce201d5");

CREATE TABLE IF NOT EXISTS authors (
  id CHAR(36) PRIMARY KEY DEFAULT uuid(), -- UUID()

  first_name VARCHAR(255) NOT NULL,
  middle_name VARCHAR(255),
  last_name VARCHAR(255) NOT NULL,
  suffix_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS books (
  id INT PRIMARY KEY AUTO_INCREMENT, 
  isbn VARCHAR(20) NOT NULL UNIQUE, 
  title VARCHAR(255) NOT NULL,
  category ENUM('fictional', 'non-fictional', 'academic') NOT NULL,

  author_id CHAR(36) NOT NULL,

  FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE
);

-- List of borrowed books
CREATE TABLE IF NOT EXISTS book_borrows (
  id INT PRIMARY KEY AUTO_INCREMENT,
  borrowed_at DATE NOT NULL DEFAULT CURDATE(),
  due_date DATE NOT NULL, -- Only students have due date
  returned_at DATE,

  book_id INT NOT NULL,
  user_id CHAR(36) NOT NULL,

  FOREIGN KEY(book_id) REFERENCES books(id) ON DELETE CASCADE,
  FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);


-- Not sure about this one yet
-- The idea is to store the corresponding penalties for each `user_role`, considering the possibility of penalty amounts changing
-- The penalty `amount` for students will be `20.00` and `0.00` for teachers
CREATE TABLE IF NOT EXISTS penalties (
  id INT PRIMARY KEY AUTO_INCREMENT,
  amount DECIMAL(10, 2) UNSIGNED NOT NULL,
  user_role ENUM('student', 'teacher')
);

-- List of penalties
CREATE TABLE IF NOT EXISTS book_penalties (
  id INT PRIMARY KEY AUTO_INCREMENT,
  payment_amount DECIMAL(10, 2) UNSIGNED NOT NULL,

  book_borrow_id INT NOT NULL,
  penalty_id INT NOT NULL,

  FOREIGN KEY(book_borrow_id) REFERENCES book_borrows(id) ON DELETE CASCADE,
  FOREIGN KEY(penalty_id) REFERENCES penalties(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS publishers (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL
);

INSERT INTO `publishers`(`name`) VALUES ('HarperCollins Publishers'), 
('Farrar, Straus and Giroux'),
('Random House, Inc.'),
('Penguin Books'),
('Ace Books'),
('Oxford University Press'),
('Doubleday'),
('Crown'),
('Random House'),
('Avery'),
('Addison-Wesley Professional'),
('John Wiley & Sons, Inc.'),
('Pearson Education, Inc.'),
('MIT Press'),
('Harcourt, Inc.');

INSERT INTO authors (first_name, middle_name, last_name, suffix_name)
VALUES
('Sun', NULL, 'Tzu', NULL),
('Anne', NULL, 'Frank', NULL),
('Michelle', NULL, 'Obama', NULL),
('Tara', NULL, 'Westover', NULL),
('James', NULL, 'Clear', NULL),
('Donald', NULL, 'Knuth', NULL),
('Patrick', NULL, 'Fitzpatrick', NULL),
('Philip', NULL, 'Kotler', NULL),
('Kevin', 'Lane', 'Keller', NULL),
('Thomas', NULL, 'Cormen', NULL),
('Gilbert', NULL, 'Strang', NULL),
('Gabriel', 'García', 'Márquez', NULL),
('Madeleine', NULL, "L'Engle", NULL),
('Markus', NULL, 'Zusak', NULL),
('Leo', NULL, 'Tolstoy', NULL),
('Frank', NULL, 'Herbert', NULL);

INSERT INTO `books`(`isbn`, `title`, `category`, `author_id`, `image_url`, `copyright`, publisher_id) 
VALUES 
('0312367541', 'A Wrinkle in Time', 'fictional', 'e635c39f-227f-11ef-9ec9-00e18ce201d5', 'a-wrinkle-in-time.jpg', '1962', 2),
('0375831003', 'The Book Thief', 'fictional', 'e635c481-227f-11ef-9ec9-00e18ce201d5', 'the-book-thief.jpg', '2005', 3),
('0140447934', 'War and Peace', 'fictional', 'e635c49d-227f-11ef-9ec9-00e18ce201d5', 'war-and-peace.jpg', '1865', 4),
('0441013593', 'Dune', 'fictional', 'e635c4b5-227f-11ef-9ec9-00e18ce201d5', 'dune.jpg', '1865', 5),
('1599869772', 'The Art of War', 'non-fictional', 'e635beaa-227f-11ef-9ec9-00e18ce201d5', 'the-art-of-war.jpg', '1963', 6),
('9780141032', 'The Diary of a Young Girl', 'non-fictional', 'e635c168-227f-11ef-9ec9-00e18ce201d5', 'the-diary-of-a-young-girl.jpg', '1947', 7),
('9781524763', 'Becoming', 'non-fictional', 'e635c1bb-227f-11ef-9ec9-00e18ce201d5', 'becoming.jpg', '2018', 8),
('9780399590', 'Educated: A Memoir', 'non-fictional', 'e635c2aa-227f-11ef-9ec9-00e18ce201d5', 'educated.jpg', '2018', 9),
('9780735212', 'Atomic Habits', 'non-fictional', 'e635c2c8-227f-11ef-9ec9-00e18ce201d5', 'atomic-habits.jpg', '2018', 10),
('0201896831', 'The Art of Computer Programming', 'academic', 'e635c2e4-227f-11ef-9ec9-00e18ce201d5', 'the-art-of-computer-programming.png', '1962', 11),
('0821847916', 'Advanced Calculus', 'academic', 'e635c2fc-227f-11ef-9ec9-00e18ce201d5', 'advanced-calculus.jpg', '1988', 12),
('0132102926', 'Marketing Management', 'academic', 'e635c319-227f-11ef-9ec9-00e18ce201d5', 'marketing-management.jpg', '2009', 13),
('0262032937', 'Introduction to Algorithms', 'academic', 'e635c34f-227f-11ef-9ec9-00e18ce201d5', 'introduction-to-algorithms.jpg', '2001', 14),
('0073057920', 'Linear Algebra and Its Applications', 'academic', 'e635c368-227f-11ef-9ec9-00e18ce201d5', 'linear-algebra-and-its-applications.jpg', '1980', 15);


