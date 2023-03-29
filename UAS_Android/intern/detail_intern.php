<?php 
require_once('koneksi.php');

$iduser = $_POST["idintern"];

$query = "SELECT users.id, users.nama, users.divisi, users.email, users.notelp, users.alamat, users.about, users.performance, projects.namaproject FROM projects JOIN userpro ON projects.idproject = userpro.idproject JOIN users ON userpro.iduser = users.id WHERE userpro.iduser = '$iduser';";
			// query untuk select data yang ada di tabel users.
$res = mysqli_query($con, $query);

$result = array();

while ($row = mysqli_fetch_array($res)){
    $result[] = $row; //menampung hasil select didalam array row
}

if($result == null){
    $query = "SELECT users.id, users.nama, users.divisi, users.email, users.notelp, users.alamat, users.about, users.performance FROM users WHERE users.id = '$iduser'";
    $res = mysqli_query($con, $query);
    while ($row = mysqli_fetch_array($res)){
        $result[] = $row;  //fungsi untuk menampilkan data user sesuai dengan jumlah data
    }
}

echo json_encode($result);

mysqli_close($con);

?>