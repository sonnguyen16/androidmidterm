<?php
    include_once('./DbConnect.php');

    if(isset($_POST['name']) || isset($_POST['detail']) || isset($_POST['price']) || isset($_POST['image'])){
        $Database = new Database();
        $conn = $Database->connect();

        $stmt = $conn->prepare('INSERT INTO furniture (name, detail, price, image) VALUES(?,?,?,?)');
        $stmt->bind_param("ssss", $_POST['name'],$_POST['detail'],$_POST['price'],$_POST['image']);
        if($stmt->execute()){
            echo 'Insert Sucess';
        }else{
            echo 'Inser Failed' + $stmt->error;
        }
    }


?>