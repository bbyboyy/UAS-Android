<?php
	
include 'koneksi.php';
	
class intern{}
	
$idintern = trim($_POST["idIntern"]);

if ((empty($idintern))){
	$response = new intern();
	$response->success = 0;
	$response->message = "ID tidak boleh kosong";
	die(json_encode($response));
} else {
	if (!empty($idintern)){
		$num_rows = mysqli_num_rows(mysqli_query($con, "SELECT * FROM userpro WHERE iduser='$idintern'"));
		if ($num_rows > 0){
			$query = mysqli_query($con, "DELETE FROM userpro WHERE iduser = '$idintern'");
			if($query){
				$response = new intern();
				$response->message1 = "Relasi Terhapus";
				$num_rows = mysqli_num_rows(mysqli_query($con, "SELECT * FROM users WHERE id='$idintern'"));
				if ($num_rows > 0){
					$query = mysqli_query($con, "DELETE FROM users WHERE id = '$idintern'");
					if($query){
						$response->success = 1;
						$response->message = "Berhasil Menghapus User";
						die(json_encode($response));
					}
				} else {
					$response->success = 0;
					$response->message = "User Tidak Terhapus";
					die(json_encode($response));
				}
			} else{
				$response = new intern();
				$response->success = 0;
				$response->message1 = "Relasi Tidak Terhapus";
				die(json_encode($response));
			}			
        } else{
            $response = new intern();
			$response->message1 = "Relasi Terhapus";
			$num_rows = mysqli_num_rows(mysqli_query($con, "SELECT * FROM users WHERE id='$idintern'"));
			if ($num_rows > 0){
				$query = mysqli_query($con, "DELETE FROM users WHERE id = '$idintern'");
				if($query){
					$response->success = 1;
					$response->message = "Berhasil Menghapus User";
					die(json_encode($response));
				}
			} else {
				$response->success = 0;
				$response->message = "User Tidak Terhapus";
				die(json_encode($response));
			}
        }	
	}
}

mysqli_close($con);

?>