<?php
    include_once('./DbConnect.php');

    if(isset($_POST['name']) || isset($_POST['detail']) || isset($_POST['price']) || isset($_POST['image'])){
        $Database = new Database();
        $conn = $Database->connect();

        $stmt = $conn->prepare('UPDATE furniture set name = ?, detail = ?, price = ?, image = ? WHERE ID = ?');
        $stmt->bind_param("sssss", $_POST['name'],$_POST['detail'],$_POST['price'],$_POST['image'], $_POST['id']);
        if($stmt->execute()){
            echo 'Update Sucess';
        }else{
            echo 'Update Failed' + $stmt->error;
        }
    }


?>