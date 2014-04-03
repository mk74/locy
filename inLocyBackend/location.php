<?php
	if (isset($_POST)) {
  		if (isset($_POST["wifi_scan_results"])){
			$scan_results = $_POST["wifi_scan_results"];
			$cs_fingerprint = "0c:d9:96:db:4e:d1"; //eduroam in library forth floor
			if (strpos($scan_results, $cs_fingerprint) !== false) {
				echo("CS school");
			}else{
				echo("no CS school");
			}
		}
	}
?>
