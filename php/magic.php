<?php

// you'll need to use your own credentials 
$username = "jared.leo@nice.org.uk";
$password = "matnov-sonsuZ-8senje";
$base_url = "https://api.magicapp.org/";
$base_api_url = $base_url."api/v1/";
$auth_url = $base_url."/auth/login";

// get auth token

$auth_post = "email=".urlencode($username)."&password=".urlencode($password);

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $auth_url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_POST, 2);
curl_setopt($ch, CURLOPT_POSTFIELDS, $auth_post);

$result = curl_exec($ch);
if (curl_errno($ch)) print curl_error($ch);

$json = json_decode($result, true);
$auth_token = $json['token'];

print("auth token: ". $auth_token ."\n"); 


// get data !

$auth_header = array_merge($auth_header, array("Bearer: ". $auth_token));

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $base_api_url."/guidelines/2");
curl_setopt($ch, CURLOPT_HTTPHEADER, $auth_header);
curl_setopt($ch, CURLOPT_REFERER, $auth_url);
    
$result = curl_exec($ch);
var_dump(json_decode($result, true));
    
curl_close($ch);
?>
