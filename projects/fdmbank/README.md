# FDMBank

## Overview
FDMBank is a online banking platform for customers to manage their finances via multiple bank accounts and credit cards, as well as redeeming gifts based on points accrued.

## Setup
1. Ensure your local machine is configured with `Git, Java, Eclipse IDE, MySQL, and MySQL Workbench`.
2. Download the [zip file](https://github.com/shumarb/projects/tree/main/projects/bookZone/bookZone.zip) of the project ot your local machine.
3. Unzip the zip file. You will see a folder called `bookZone`.
4. Open the `Eclipse IDE`.
5. Select `File`.
6. Select `Import`.
7. Select `Maven`.
8. Select `Existing Maven Project`.
9. At `Root Directory`, select `Browse`.
10. Navigate to the folder of the unzipped `creditcardproject` project.
11. Select the folder in Step 9. You will see the `bookZone` project in the Package Explorer.
12. Change `lines 6 & 7` of `application.properties` to the `username` and `password` of your `MySQL database` respectively.
13. Create a `schema` in your `MySQL Workbench` with a name of your choice.
14. Update `line 5 of application.properties` with the `name of the schema in step 13`. schema (Example: If the name of the schema is `fdmbank`, update line 5 of application.properties to `spring.datasource.url=jdbc:mysql://localhost:3306/fdmbank`).

## Instructions
1. Select the `>` icon of the `creditcardproject` project.
2. Select the `>` icon of the `src/main/java/` folder.
3. Select the `>` icon of the `com.bookzone` package.
4. Right-click `CreditCardProjectApplication.java`.
5. Select `Run As`.
6. Select `1. Java Application`.
7. Open a web browser.
8. Enter `localhost:9001`.
9. You will see the `Index page`.
10. `Register` an account.
11. `Log` into `FDMBank`.
12. Navigate through `FDBank` and use the functionalities.
