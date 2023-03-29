<?php
	
include_once "koneksi.php";

class user{}

$username = trim($_POST["username"]);
$password = trim($_POST["password"]);

if ((empty($username)) || (empty($password))) { 
	$response = new user();
	$response->success = 0;
	$response->message = "Username atau password kosong"; 
	die(json_encode($response));
}
	
$query = mysqli_query($con, "SELECT * FROM users WHERE username='$username' AND password='$password'");
	
$row = mysqli_fetch_array($query);
	
if (!empty($row)){
	$response = new user();
	$response->success = 1;
	$response->message = "Selamat datang ".$row['username'];
	$response->username = $row['username'];
	$response->id = $row['id'];
	$response->fullname = $row['nama'];
	$response->access = 0; 
	if($row['divisi'] == "admin"){
		$response->access = 1;
	}
	die(json_encode($response));
		
} else { 
	$response = new user();
	$response->success = 0;
	$response->message = "Username atau password salah";
	die(json_encode($response));
}
	
mysqli_close($con);

?>