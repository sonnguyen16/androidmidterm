<?php
 /**
  * 
  */
 class Database
 {
    private $servername = "localhost";
    private $username = "root";
    private $password = "";
    private $conn = null;

     function __construct()
     {

     }

     function connect(){
         $this->conn = new mysqli($this->servername, $this->username, $this->password, 'test');

        if ($this->conn->connect_error) {
          die("Connection failed: " . $this->conn->connect_error);
        }

        return $this->conn;
     }
    
 }

?>