<?php
include("config.php");

class FileFunctions {
 
    //put your code here
    // constructor
    public function __construct() {
    }
 
    // destructor
    public function __destruct() {
    }
 
    /**
     * Storing new user
     * returns user details
     */
    public function serializeUsers($users) {
		//Serialize the new object
		$objData = serialize($users);
		if (is_writable(FILE_NAME)) {
			$fp = fopen(FILE_NAME, "w"); 
			fwrite($fp, $objData); 
			fclose($fp);
		}
    }
 
    /**
     * Getting all users
     */
    public function unserializeUsers() {
        $obj = unserialize(file_get_contents(FILE_NAME));
		return $obj;
    }
}
 
?>