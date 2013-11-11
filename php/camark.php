<?php
require_once("scraper.php");

$scraper = new Scraper();


$url="http://studzone.psgtech.edu/CommonPage.aspx";


//$result = $scraper->aspFormPost($url,$array);


function regexExtract($text, $regex, $regs, $nthValue) {
            if (preg_match($regex, $text, $regs)) {
                $result = $regs[$nthValue];
            } else {
                $result = "";
            }
            return $result;
        }

       

function markscraper($rollno)
{
$ch = curl_init();
curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
curl_setopt($ch, CURLOPT_COOKIEJAR, "/tmp/cookieFileName");
curl_setopt($ch, CURLOPT_URL,"http://studzone.psgtech.edu");
curl_setopt($ch, CURLOPT_FOLLOWLOCATION, TRUE);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
$buf1 = curl_exec ($ch);

  $regexViewstate = '/__VIEWSTATE\" value=\"(.*)\"/i';
  $regexEventVal  = '/__EVENTVALIDATION\" value=\"(.*)\"/i';
$regs=array();
  $viewstate = regexExtract($buf1,$regexViewstate,$regs,1);
  $eventval =  regexExtract($buf1, $regexEventVal,$regs,1);

$data = "__EVENTTARGET=".rawurlencode("DgdLinks:_ctl5").'&'.
							"__EVENTARGUMENT=links&".
							"TxtRollNo=&".
							"__VIEWSTATE=".rawurlencode($viewstate);
						 

curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
curl_setopt($ch, CURLOPT_COOKIEFILE, "/tmp/cookieFileName");
curl_setopt($ch, CURLOPT_URL,"http://studzone.psgtech.edu/CommonPage.aspx");
curl_setopt($ch, CURLOPT_POST,1);
curl_setopt($ch, CURLOPT_POSTFIELDS,$data);

$result = curl_exec($ch);

  $regexViewstate = '/__VIEWSTATE\" value=\"(.*)\"/i';
  $regexEventVal  = '/__EVENTVALIDATION\" value=\"(.*)\"/i';
  $regs=array();
  $viewstate = regexExtract($result,$regexViewstate,$regs,1);
  $eventval =  regexExtract($result,$regexEventVal,$regs,1);



$data ="__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE=".rawurlencode($viewstate)."&TxtRollNo={$rollno}&BtnSubmit=Submit";

curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
curl_setopt($ch, CURLOPT_COOKIEFILE, "/tmp/cookieFileName");
curl_setopt($ch, CURLOPT_URL,"http://studzone.psgtech.edu/CommonPage.aspx");
curl_setopt($ch, CURLOPT_POST,1);
curl_setopt($ch, CURLOPT_POSTFIELDS,$data);

$result = curl_exec($ch);

return $result;
}
?>
