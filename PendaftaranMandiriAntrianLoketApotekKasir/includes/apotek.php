<?php
include ('../config.php');
$antriloket = fetch_assoc(query("SELECT loket FROM antriapotek"));
$antriloket = $antriloket['loket'];
echo $antriloket;
?>
