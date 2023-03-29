<?php 
require_once('koneksi.php');

$query = "SELECT * FROM users JOIN userpro ON users.id = userpro.iduser";

$res = mysqli_query($con, $query);

$result = array();

while ($row = mysqli_fetch_array($res)){
    $result[] = $row;
}

echo json_encode($result);

mysqli_close($con);

?>