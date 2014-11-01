<?php
include("file_functions.php");

class Users {
    public $users;
	
	//put your code here
    // constructor
    public function __construct() {
		$users = array();
    }
 
    // destructor
    public function __destruct() {
    }
}

class User {
	public $id  = 0;
	public $name  = "";
	public $email  = "";
    public $gcm_regid = 0;
	public $created_at = "";
}

function addUser($name, $email, $gcm_regid)
{
	$fileFunctions = new FileFunctions;
	$user = new User();
	
	$users = getUsers();
	$id = count($users);
	$user->id = $id;
	$user->name = $name;
	$user->email = $email;
	$user->gcm_regid = $gcm_regid;
	$date = new DateTime(); //this returns the current date time
	$user->created_at = $date->format('Y-m-d-H-i-s');
	
	//Add the new user to the array
	//$gcm_regid will be the key
	//array_push($users, &user);
	$users[$id] = $user;
	$fileFunctions->serializeUsers($users);		
	return true;
}
	
function getUsers()
{
	$fileFunctions = new FileFunctions;
	$users = $fileFunctions->unserializeUsers();
	//var_dump($fileFunctions);
	return $users;
}

function getSpecificUser($id)
{
	$users = getUsers();
	return $users[$id];
}

function deleteUser($id)
{
	$fileFunctions = new FileFunctions;
	$users = getUsers();
	unset($users[$id]);
	$fileFunctions->serializeUsers($users);		
	//var_dump($fileFunctions);
}
?>