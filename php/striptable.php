<?php

require_once("dom/simple_html_dom.php");
require_once("camark.php");
$html = markscraper("09pw03");
$html = str_get_html($html);

$secondtable	=	$html->find('table',2);	
$td_array				=	array();
$tr_array				=	array();
$table_array		=	array();
$all_array			=	array();
foreach($secondtable->next_sibling()->find('table') as $table)
 {
	foreach($table->find('tr') as $tr)
	{
	//$tr=$table->find('tr',1);
	foreach( $tr->find('td') as $element)
	  {
			$column	=	 $element->next_sibling()->plaintext;
			if($column==null || $column=="")
				$column="*";
			if((strpos($column,'Max')===FALSE) && $column!=null)
				array_push($td_array,$column);
		}
		if(count($td_array)>1) //previously !=0
	  	array_push($tr_array,$td_array);
		$td_array = array();
	}
	if(count($tr_array)!=0)
		array_push($table_array,$tr_array);
	 $tr_array	=	array();
 }


array_push($all_array,$table_array);
echo json_encode($table_array);



?>

