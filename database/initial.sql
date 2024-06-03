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

CREATE TABLE IF NOT EXISTS sections (
  id VARCHAR(50) PRIMARY KEY, -- "acsad", "bcsad", "ccsad"
  name VARCHAR(255) NOT NULL, -- "ACSAD", "BCSAD", "CCSAD"
  level ENUM('elementary', 'junior-high-school', 'senior-high-school', 'college') NOT NULL
);

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

CREATE TABLE IF NOT EXISTS students (
  id VARCHAR(100) PRIMARY KEY, -- Use student no.

  -- Use `section_level_id` as it's already related to its corresponding section and year level.
  -- It's impossible for a student to not have a section or a year level.
  section_level_id VARCHAR(100) NOT NULL,
  user_id CHAR(36) NOT NULL,

  FOREIGN KEY(section_level_id) REFERENCES section_levels(id) ON DELETE CASCADE,
  FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

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

