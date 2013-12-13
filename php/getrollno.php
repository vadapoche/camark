<?php
require_once("dom/simple_html_dom.php");
require_once("camark.php");
$html = markscraper(trim($_REQUEST['rollno']));
$html = str_get_html($html);

$secondtable  = $html->find('table',2);
$secondtable->children(2)->plaintext;

$name =  $html->getElementById("TbStudInfo")->children(0)->children(2)->plaintext;
$rollno =  $html->getElementById("TbStudInfo")->children(0)->children(5)->plaintext;



?>
