<?php 
require_once('koneksi.php');

$iduser = $_POST['iduser'];

$query = "SELECT * FROM users WHERE id = '$iduser'"; //menampilkan data dari tabel user berdasarkan yang di select.

$res = mysqli_query($con, $query);

$result = array();

while ($row = mysqli_fetch_array($res)){
    $result[] = $row; //fetching data hasil select diatas
}

echo json_encode($result);

mysqli_close($con);

?>