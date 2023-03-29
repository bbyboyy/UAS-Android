<?php 
require_once('koneksi.php');

$idproject = $_POST['idproj'];

$query = "SELECT projects.namaproject as namaproj, projects.descpro as descpro, projects.start as startdate, projects.end as enddate, users.nama as namaintern from projects JOIN userpro ON projects.idproject = userpro.idproject JOIN users ON users.id = userpro.iduser WHERE projects.idproject = '$idproject'";

$res = mysqli_query($con, $query);

$result = array();

while ($row = mysqli_fetch_array($res)){
    $result[] = $row;
}

echo json_encode($result);

mysqli_close($con);

?>