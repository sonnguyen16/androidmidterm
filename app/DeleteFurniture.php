<?php
    include_once('./DbConnect.php');

    if(isset($_POST['id'])){
        $Database = new Database();
        $conn = $Database->connect();

        $stmt = $conn->prepare('DELETE from furniture WHERE ID = ?');
        $stmt->bind_param("s", $_POST['id']);
        if($stmt->execute()){
            echo 'Delete Sucess';
        }else{
            echo 'Delete Failed' + $stmt->error;
        }
    }


?>