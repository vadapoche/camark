<?php
require_once("dom/simple_html_dom.php");

$html = file_get_html('data.html');
echo $html->children(0);


?>
