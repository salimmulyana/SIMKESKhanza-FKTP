<?php

if (preg_match ('/config.php/', basename($_SERVER['PHP_SELF']))) die ('Unable to access this script directly from browser!');

define ("DB_HOST","localhost");
define ("DB_USER","root");
define ("DB_PASS","password");
define ("DB_NAME","database");
define ("URL","http://192.168.196.82/AnjunganAntrianDisplayPhp");

define('BpjsApiUrl', 'https://new-api.bpjs-kesehatan.go.id:8080/new-vclaim-rest/');
define('ConsID', '');
define('SecretKey', '');

$connection = mysqli_connect(DB_HOST, DB_USER, DB_PASS, DB_NAME);


$getSettings = query("SELECT * FROM setting");
$dataSettings = fetch_assoc($getSettings);

$hari=fetch_array(query("SELECT DAYNAME(current_date())"));
$namahari="";
if($hari[0]=="Sunday"){
    $namahari="AKHAD";
}else if($hari[0]=="Monday"){
    $namahari="SENIN";
}else if($hari[0]=="Tuesday"){
   	$namahari="SELASA";
}else if($hari[0]=="Wednesday"){
    $namahari="RABU";
}else if($hari[0]=="Thursday"){
    $namahari="KAMIS";
}else if($hari[0]=="Friday"){
    $namahari="JUMAT";
}else if($hari[0]=="Saturday"){
    $namahari="SABTU";
}

$db = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
if($db->connect_error){
    die("Unable to connect database: " . $db->connect_error);
}

function escape($string) {
    global $connection;
    return mysqli_real_escape_string($connection, $string);
}

function query($sql) {
    global $connection;
    $query = mysqli_query($connection, $sql);
    confirm($query);
    return $query;
}

function confirm($query) {
    global $connection;
    if(!$query) {
        die("Query failed! " . mysqli_error($connection));
    }
}

function fetch_array($result) {
    return mysqli_fetch_array($result);
}

function fetch_assoc($result) {
    return mysqli_fetch_assoc($result);
}

function num_rows($result) {
    return mysqli_num_rows($result);
}

function insertTable($table_name, $insertvalue="") {
  $query1 = "";
  $query2 = "";
  if($insertvalue != ""){
    $i=0;
    foreach($insertvalue as $key => $item) {
      if($i == 0) {
        $query1 = $key;
        $query2 = "'".$item."'";
      }
      else{
        $query1 = $query1 . ", ".$key;
        $query2 = $query2 . ", '".$item."'";
      }
      $i++;
    }
  }

  $query = "INSERT INTO ".$table_name." (".$query1.") VALUES (".$query2.")";
  query($query);
}

// Get settings
$getSettings = query("SELECT * FROM setting");
$dataSettings = fetch_assoc($getSettings);

// Get date and time
date_default_timezone_set('Asia/Jakarta');
$tanggal    = date('d');
$bulan      = date('m');
$tahun      = date('Y');
$month      = date('Y-m');
$date       = date('Y-m-d');
$time       = date('H:i:s');
$nonbooking = date('His');
$date_time  = date('Y-m-d H:i:s');

// Namahari
$hari=fetch_array(query("SELECT DAYNAME(current_date())"));
$namahari="";
if($hari[0]=="Sunday"){
    $namahari="AKHAD";
}else if($hari[0]=="Monday"){
    $namahari="SENIN";
}else if($hari[0]=="Tuesday"){
   	$namahari="SELASA";
}else if($hari[0]=="Wednesday"){
    $namahari="RABU";
}else if($hari[0]=="Thursday"){
    $namahari="KAMIS";
}else if($hari[0]=="Friday"){
    $namahari="JUMAT";
}else if($hari[0]=="Saturday"){
    $namahari="SABTU";
}

header('Content-Type: text/html; charset=utf-8');

function redirect($location) {
    return header("Location: {$location}");
}
?>
