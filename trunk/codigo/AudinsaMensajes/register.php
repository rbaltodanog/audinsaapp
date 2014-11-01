<?php
include("user.php");
include("GCM.php");
 // response json
$json = array();
 
/**
 * Registering a user device
 * Store reg id in text file
 */
if (isset($_POST["name"]) && isset($_POST["email"]) && isset($_POST["regId"])) {
    $name = $_POST["name"];
    $email = $_POST["email"];
    $gcm_regid = $_POST["regId"]; // GCM Registration ID
	
    // Store user details in text file 
    $gcm = new GCM();
    $res = addUser($name, $email, $gcm_regid);
 
    $registation_ids = array($gcm_regid);
    $message = array("product" => "shirt");
 
    $result = $gcm->send_notification($registation_ids, $message);
 
    echo $result;
} else {
    // user details missing
}
?>