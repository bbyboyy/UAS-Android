<?php 
require_once('koneksi.php');

$query = "SELECT COUNT(users.id) as jumlah, projects.namaproject FROM users JOIN userpro ON users.id = userpro.iduser JOIN projects ON projects.idproject = userpro.idproject GROUP BY projects.idproject;";

$res = mysqli_query($con, $query);

$result = array();

while ($row = mysqli_fetch_array($res)){
    $result[] = $row;
}

echo json_encode($result);

mysqli_close($con);

?>