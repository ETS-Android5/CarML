# CarML Seán Coll C18332623

# Requirements:
- XAMPP (or equivalent technology stack) (version 8.0.3 was used)
- Android 9 phone or emulator
- Android Studio Bumblebee | 2021.1.1 Patch 3
Build #AI-211.7628.21.2111.8309675, built on March 16, 2022
Runtime version: 11.0.11+0-b60-7590822 x86_64

# Files included in Additional Files folder:
- DD_Cars.sql
- DD_EncodedVals.sql
- addPrediction.php
- getCarDetails.php
- getEncodedValues.php
- getManufacturers.php
- getModels.php
- getYears.php
- pingServer.php

# Instructions before running the code:
1. Create a folder titled CarML in XAMPP/xamppfiles/htdocs
2. Put all the PHP scripts in XAMPP/xamppfiles/htdocs/CarML 
3. Launch XAMPP and start the MySQL Database and Apache Web Server services
4. Back on the welcome screen of XAMPP, click Go to Application
5. Click on phpMyAdmin in the top right corner
6. In the left panel click New and create a database called CarML using the utf8mb4_general_ci Collation
7. Click on the CarML database and click Import on the top row
8. Click Browse and select DD_Cars.sql, then click Go
9. Import DD_EncodedVals.sql
10. In DatabaseAccess.java change the value of DBURL to the IP address of the host on the network (the machine running XAMPP)

Note: if an image does not appear on the Result screen, the userAgent string in ImageLoader.java will need to be changed to the user agent of the phone/emulator.

# Video deomonstration:
