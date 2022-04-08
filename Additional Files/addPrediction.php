<?php
$server_name = "localhost";
$mysql_user = "root";
$mysql_pass = "";
$db_name = "CarML";
$manufacturer = $_POST["manufacturer"];
$model = $_POST["model"];
$year = $_POST["year"];
$prediction = $_POST["prediction"];

$con = mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name);
if(!$con)
{
    echo "Connection failed";
}
else
{   
    $sql = "UPDATE DD_Cars SET Prediction = $prediction WHERE Make LIKE '$manufacturer' AND Model LIKE '$model' AND Year LIKE $year";
    $result = mysqli_query($con, $sql) or die("Error with SQL " . mysqli_error($sql));
    // $result = mysqli_query($con, $sql) or die("Error with SQL " . mysqli_error($con));
    // mysqli_close($con);
}
    
?>