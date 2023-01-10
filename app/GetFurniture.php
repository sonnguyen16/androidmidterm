<?php
    include_once('./DbConnect.php');

    class Furniture{
        function __construct($id, $name, $detail, $price, $image){
            $this->id = $id;
            $this->name = $name;
            $this->detail = $detail;
            $this->price = $price;
            $this->image = $image;
        }
    }

    $Database = new Database();
    $conn = $Database->connect();
    $array = array();

    $stmt = $conn->prepare("SELECT * FROM furniture");
    $stmt->execute();
    $stmt->bind_result($id, $name, $detail, $price, $image);
    while($stmt->fetch()){
        array_push($array, new Furniture($id, $name, $detail, $price, $image));
    }

    echo json_encode($array);


?>