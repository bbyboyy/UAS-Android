<?php
	
include 'koneksi.php';
	
class user{}
	
$projectname = trim($_POST["ProjName"]);
$projectdesc = trim($_POST["ProjDesc"]);
$start_date = trim($_POST["StartDate"]);
$end_date = trim($_POST["EndDate"]);

if ((empty($projectname)) || (empty($projectdesc)) || (empty($start_date)) || (empty($end_date))){
	$response = new user();
	$response->success = 0;
	$response->message = "Field tidak boleh kosong";
	die(json_encode($response));
} else {
	if (!empty($projectname)){
		$num_rows = mysqli_num_rows(mysqli_query($con, "SELECT * FROM projects WHERE namaproject='".$projectname."'"));

        if ($num_rows == 0){
		$query = mysqli_query($con, "INSERT INTO projects (namaproject, descpro, start, end) VALUES ('".$projectname."','".$projectdesc."', '".$start_date."', '".$end_date."')");
			if ($query){
				$response = new user();
				$response->success = 1;
				$response->message = "Menambah project berhasil";
				die(json_encode($response));
			}
		} else {
			$response = new user();
			$response->success = 0;
			$response->message = "Nama Project sudah ada";
			die(json_encode($response));
		}
	}
}

mysqli_close($con);

?>