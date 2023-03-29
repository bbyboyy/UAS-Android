<?php
	
include 'koneksi.php';
	
class user{}
	
$fullname = trim($_POST["fullname"]);
$username = trim($_POST["username"]);
$password = trim($_POST["password"]);
$confirm_password = trim($_POST["confirm_password"]);
$address = trim($_POST["address"]);
$email = trim($_POST["email"]);
$phone = trim($_POST["phone"]);
$division = trim($_POST["division"]);
$status = trim($_POST["status"]);

if ((empty($fullname)) || (empty($username)) || (empty($password)) || (empty($address)) || (empty($email)) || (empty($phone)) || (empty($division)) || (empty($status))) {
	$response = new user();
	$response->success = 0;
	$response->message = "Field tidak boleh kosong";
	die(json_encode($response));
} else if ((empty($confirm_password)) || $password != $confirm_password) {
	$response = new user();
	$response->success = 0;
    $response->message = "Konfirmasi password harus sama";
	die(json_encode($response));
} else {
	if (!empty($username) && $password == $confirm_password){
		$num_rows = mysqli_num_rows(mysqli_query($con, "SELECT * FROM users WHERE username='".$username."'"));

        if ($num_rows == 0){
		$query = mysqli_query($con, "INSERT INTO users (nama, username, password, alamat, email, notelp, divisi, status) VALUES ('".$fullname."','".$username."', '".$password."', '".$address."', '".$email."', '".$phone."', '".$division."', '".$status."')");
			if ($query){
				$response = new user();
				$response->success = 1;
				$response->message = "Register berhasil";
				die(json_encode($response));
			}
		} else {
			$response = new user();
			$response->success = 0;
			$response->message = "Username sudah terdaftar";
			die(json_encode($response));
		}
	}
}

mysqli_close($con);

?>