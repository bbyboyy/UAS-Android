<?php 
define('HOST', 'localhost');
define('USER', 'root');
define('PASS', '');
define('DATABASE', 'xyzinternship');

$con = mysqli_connect(HOST,USER,PASS,DATABASE) or die('Unable to Connect');
?>