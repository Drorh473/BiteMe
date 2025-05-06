BiteMe
BiteMe is a Java-based desktop application developed using JavaFX, designed to offer an intuitive and responsive user interface for he system enables clients to place orders, track order status in real-time, and provides restaurant staff and management with tools to receive orders and generate performance reports The application integrates seamlessly with a backend server through RESTful APIs, facilitating efficient data management and operations.

Table of Contents
Features

Technologies Used

Installation

Usage

Project Structure

Contributing

License

Features
User-Friendly Interface: Developed with JavaFX to ensure a smooth and intuitive user experience.

RESTful API Integration: Communicates with the backend server using RESTful APIs for data operations.

MySQL Database Support: Utilizes MySQL for robust and reliable data storage.

Modular Architecture: Clean separation between client, server, and shared resources for maintainability.

Unit Testing: Comprehensive unit tests implemented using JUnit to ensure code reliability.

Technologies Used
Java: Core programming language.

JavaFX: For building the graphical user interface.

MySQL: Relational database management system.

RESTful APIs: For communication between client and server.

JUnit: Framework for unit testing.

Maven/Gradle: Build and dependency management tools.

Installation
Prerequisites
Java Development Kit (JDK) 8 or higher

MySQL Server

Maven or Gradle

Git

Steps
Clone the Repository:

bash
Copy
Edit
git clone https://github.com/Drorh473/BiteMe.git
cd BiteMe
Set Up the Database:

Create a new MySQL database:

sql
Copy
Edit
CREATE DATABASE biteme_db;
Import the provided SQL schema and data:

bash
Copy
Edit
mysql -u your_username -p biteme_db < path_to_schema.sql
Update database credentials in the application's configuration files as needed.

Build the Project:

Using Maven:

bash
Copy
Edit
mvn clean install
Using Gradle:

bash
Copy
Edit
gradle build
Run the Application:

Navigate to the client module directory:

bash
Copy
Edit
cd BiteMeClient2
Execute the application:

bash
Copy
Edit
java -jar target/BiteMeClient2.jar
Usage
Upon launching the application:

Login: Enter your credentials to access the system.

Navigate: Use the menu to access different features such as [e.g., "viewing orders", "managing menu items"].

Perform Actions: Interact with the application to perform desired operations.

Logout: Ensure to log out after completing your tasks.

Note: Replace the above steps with actual usage instructions relevant to your application.

Project Structure
bash
Copy
Edit
BiteMe/
├── BiteMeClient2/      # JavaFX client application
├── BiteMeServer2/      # Backend server handling RESTful APIs
├── Common/             # Shared resources between client and server
├── OCSF/               # Open Communication Server Framework
└── README.md           # Project documentation
Contributing
Contributions are welcome! To contribute:

Fork the repository.

Create a new branch:

bash
Copy
Edit
git checkout -b feature/YourFeature
Commit your changes:

bash
Copy
Edit
git commit -m "Add YourFeature"
Push to the branch:

bash
Copy
Edit
git push origin feature/YourFeature
Open a pull request detailing your changes.

Please ensure your code adheres to the project's coding standards and includes appropriate tests.

License
This project is licensed under the [Your License Name] License. See the LICENSE file for details.
