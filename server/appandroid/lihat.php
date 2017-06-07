<?php
	require_once 'include/DB_Function.php';
	
	$db = new DB_Function();
	
		$result = $db->requestPohon();
		if($result){
			$response["error"] = false;
			$response["pohon"] = array();
			while ($pohon = $result->fetch_assoc()){
				//$temp = array();
				
				/*$temp["uuid"] = $pohon["uuid"];
				$temp["id"] = $pohon["id"];
				$temp["jenis_pohon"] = $pohon["jenis_pohon"];
				$temp["usia_pohon"] = $pohon["usia_pohon"];
				$temp["kondisi_pohon"] = $pohon["kondisi_pohon"];
				$temp["latitude"] = $pohon["latitude"];
				$temp["longitude"] = $pohon["longitude"];
				$temp["foto_pohon"] = $pohon["foto_pohon"];
				$temp["tgl"] = $pohon["tgl"];
				$temp["keterangan"] = $pohon["keterangan"]; */
				array_push($response ["pohon"], $pohon);
			}
			
			echo json_encode($response);
		}else{
			$response["error"] = true;
			$response["error_msg"] = "Terjadi kesalahan saat melakukan registrasi";
			echo json_encode($response);
		}
?>