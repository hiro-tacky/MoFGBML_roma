$dataName = "pima"
$parallelCores = 12
$algorithmID = "ranked"
$experimentID = "trial"
$logFileName = $algorithmID + "_log.txt"

Start-Transcript ./results/$algorithmID/$dataName/$logFileName -Append
for($i=0; $i -lt 3; $i++){
	for($j=0; $j -lt 10; $j++){
		$DtraFileName = "a" + $i + "_" + $j + "_" + $dataName + "-10tra.dat"
		$DtstFileName = "a" + $i + "_" + $j + "_" + $dataName + "-10tst.dat"
		Java -jar target/MoFGBML-23.0.0-SNAPSHOT.jar $dataName $algorithmID $experimentID$i$j $parallelCores dataset\$dataName\$DtraFileName dataset\$dataName\$DtstFileName
		Write-Output "Java -jar target/MoFGBML-23.0.0-SNAPSHOT.jar $dataName $algorithmID $experimentID$i$j $parallelCores dataset\$dataName\$DtraFileName dataset\$dataName\$DtstFileName"
	}
}
Pause