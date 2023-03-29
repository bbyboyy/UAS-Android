<?php 
require_once('koneksi.php');

$iduser = $_POST["iduser"];

$query = "SELECT COUNT(users.id) as jumlah, projects.namaproject FROM users JOIN userpro ON userpro.iduser = users.id JOIN projects ON userpro.idproject = projects.idproject WHERE projects.idproject = (SELECT userpro.idproject from userpro WHERE userpro.iduser = '$iduser')";

$res = mysqli_query($con, $query);

$result = array();

while ($row = mysqli_fetch_array($res)){
    $result[] = $row;
}

echo json_encode($result);

mysqli_close($con);

?>