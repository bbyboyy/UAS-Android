<?php //fle php untuk menampilkan detail intern pada aplikasi
require_once('koneksi.php'); 

$iduser = $_POST["iduser"];

$query = "SELECT users.id, users.nama, users.divisi, users.email, users.notelp, users.alamat, users.about, users.performance, projects.namaproject FROM projects JOIN userpro ON projects.idproject = userpro.idproject JOIN users ON userpro.iduser = users.id WHERE userpro.iduser = '$iduser'";

$result = array();

$res = mysqli_query($con, $query);

while ($row = mysqli_fetch_array($res)){
    $result[] = $row;
}

if($result == null){
    $query = "SELECT users.id, users.nama, users.divisi, users.email, users.notelp, users.alamat, users.about, users.performance FROM users WHERE users.id = '$iduser'"; //colect iduser
    $res = mysqli_query($con, $query);
    while ($row = mysqli_fetch_array($res)){
        $result[] = $row; //fungsi ini akan menampilkan semua detail user dengan id yang bersangkutan.
    }
}

echo json_encode($result);

mysqli_close($con);

?>