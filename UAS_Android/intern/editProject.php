<?php
	
include 'koneksi.php';
	
class proj{}
	
$idproject = trim($_POST["idProj"]);
$namaproject = trim($_POST["namaProj"]);
$descproject = trim($_POST["descProj"]);
$startdate = trim($_POST["startDate"]);
$enddate = trim($_POST["endDate"]);

if ((empty($idproject)) || (empty($namaproject)) || (empty($descproject)) || (empty($startdate)) || (empty($enddate))){
	$response = new proj();
	$response->success = 0;
	$response->message = "Field harus diisi!!";
	die(json_encode($response));
} else {
	if (!empty($idproject)){
		$num_rows = mysqli_num_rows(mysqli_query($con, "SELECT * FROM projects WHERE idproject='$idproject'"));
		if ($num_rows > 0){
			$query = mysqli_query($con, "UPDATE projects SET namaproject = '$namaproject', descpro = '$descproject', start = '$startdate', end = '$enddate' WHERE idproject = '$idproject'");
			if($query){
				$response = new proj();
                $response->success = 1;
				$response->message = "Update Berhasil!";	
                die(json_encode($response));
			} else {
                $response->success = 0;
				$response->message = "Project gagal diupdate :( ";
				die(json_encode($response));
				}
		} else{
			$response = new proj();
			$response->success = 0;
			$response->message = "Project tidak ada";
			die(json_encode($response));
		}			
	}		
}

mysqli_close($con);

?>