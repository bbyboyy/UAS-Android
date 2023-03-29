<?php
	
include 'koneksi.php';
	
class user{}
	
$query1 = mysqli_query($con, "SELECT AUTO_INCREMENT as idproject FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'xyzinternship' AND TABLE_NAME = 'projects'");
$r = mysqli_fetch_assoc($query1);
$idproject = $r['idproject'] - 1;
$idintern = trim($_POST["idIntern"]);
$jobdesc = trim($_POST["jobDesc"]);

if ((empty($idintern)) || (empty($jobdesc))){
	$response = new user();
	$response->success = 0;
	$response->message = "Field tidak boleh kosong";
	die(json_encode($response));
} else {
	if (!empty($idintern)){
		$num_rows = mysqli_num_rows(mysqli_query($con, "SELECT * FROM userpro WHERE userpro.iduser = '$idintern' AND userpro.idproject = '$idproject'"));
		if ($num_rows == 0){
			$query = mysqli_query($con, "INSERT INTO userpro (idproject, iduser, jobdesk) VALUES ('$idproject', '$idintern', '$jobdesc')");
			if ($query){
				$response = new user();
				$response->success = 1;
				$response->message = "Menambah relasi project berhasil";
				die(json_encode($response));
			}else{
				$response = new user();
				$response->success = 0;
				$response->message = "Gagal";
				die(json_encode($response));
			}
		} else {
			$response = new user();
			$response->success = 0;
			$response->message = "User sudah ada didalam Project";
			die(json_encode($response));
		}
	}
}

mysqli_close($con);

?>
