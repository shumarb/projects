# BookZone

## Overview
BookZone is a platform serving librarians at the Singapore Book Collectors club in their book catalogue. Its key features include:

+ Adding a book to the catalogue.
+ Deleting a book from the catalogue.
+ Displaying all books in the catalogue.
+ Displaying all Special books in the catalogue.
+ Editing a book in the catalogue.
+ Marking a book as Special.
+ Removing a book from Specials and retaining it in the catalogue.

## Setup
1. Ensure your local machine is configured with Git, Java, Eclipse IDE, MySQL, and MySQL Workbench.
2. Clone this repository onto your local machine.
3. Open the Eclipse IDE.
4. Select File.
5. Select Import.
6. Select Maven
7. Select Existing Maven Project.
8. At Root Directory, select Browse.
9. Navigate to the folder of the cloned git repository the folder of the clone file.
10. Select the folder in Step 9. You will see the BookZone project in the Package Explorer.
11. Change lines 6 & 7 of application.properties to the username and password of your MySQL database.
12. Create a schema in your MySQL Workbench called book_zone.

## Instructions
1. Select the > icon of the BookZone project.
2. Select the > icon of the src/main/java/ folder.
3. Select the > icon of the com.bookzone package.
4. Right-click BookZoneApplication.java.
5. Select Run As.
6. Select 1. Java Application.
7. Open a web browser.
8. Enter localhost:9001
9. You will see the Index page.
10. Register a BookZone account via an email address that ends with sgbookcollectors.com
11. Log into BookZone.
12. Use the BookZone portal to add, edit, delete, or indicate a book as Special.