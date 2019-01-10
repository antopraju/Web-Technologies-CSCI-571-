<!DOCTYPE HTML>

<?php 
	//To include the geoHash File which will take latitude and longitude as inputs and give the geoCode.
	include "geoHash.php";
?>

<?php 

	//Define all the Form Parameters and Url Parameters.

	$keyword = "";
	$retainDistance = "";
	$locn = ""; 

	//Give distance a default size of 10.

	$distance = "10";
	$retainLocation = "";
	$retainKeyword = "";
	$category = "default";
	$geoHash = "";	
	$segmentId = "";
	$lat = "0";
	$from = "";
	$google_api_id = "";
	$defaultURL = "";
	$searchType = "";
	$radiusValue = "";
	$keywordValue = "";
	$locationToGeoCodeURL = "";
	$lon = "0";
	$eventRequired = "";
	$clearForm = 0;
	$eventStatus = "";

	//Define all the API's and Access Keys.

	$googleGeoCodeURL = "https://maps.googleapis.com/maps/api/geocode/json?";
	$ticketmaster_access_id = "GfAAasFz3JZu04YljkCaq0nGuy4hz3cb";
	$ticketMasterSearchURL = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=GfAAasFz3JZu04YljkCaq0nGuy4hz3cb";
	$full_event_details_url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=GfAAasFz3JZu04YljkCaq0nGuy4hz3cb";
	$full_venue_details_url = "https://app.ticketmaster.com/discovery/v2/venues?apikey=GfAAasFz3JZu04YljkCaq0nGuy4hz3cb&keyword=Los%20Angeles%20Memorial%20Coliseum";

	//To convert the Space Separated Keyword to + Separated url.

	function urlEncoding($str) {
    	return urlencode($str);
	}

?>

<?php

	if(!empty($_POST)){

		$keyword =  urlEncoding($_POST["keyword"]);
		$retainKeyword = $_POST["keyword"];


		$category = $_POST["category"];
		$distance = $_POST["distance"];
		$retainDistance = $distance;

		//locn - location to search
		$locn = $_POST["locn"];
		$retainLocation = $_POST['locn'];

		//From here will give value of "here".
		$from = $_POST["from"];

		//Latitute and longitude of the current place.
		$lat = $_POST["lat"];
		$lon = $_POST["lon"];

		//To check weather the form is submited.
		$submit = $_POST["eventRequired"];
		$eventRequired = $_POST["eventRequired"];
		$event_id = $_POST['event_id'];
		$venue_name = $_POST['venue_name'];

		//To replace the space separated venue name by %20 separated venue name.
		$venue_name = rawurlencode($venue_name);

		//Build the event Search URL.
		$full_event_details_url = "https://app.ticketmaster.com/discovery/v2/events/";
		$ticketmaster_access_id = "GfAAasFz3JZu04YljkCaq0nGuy4hz3cb";
		$full_event_details_url = $full_event_details_url.$event_id."?apikey=".$ticketmaster_access_id;
		$full_venue_details_url = "https://app.ticketmaster.com/discovery/v2/venues?apikey=GfAAasFz3JZu04YljkCaq0nGuy4hz3cb&keyword=";
		$full_venue_details_url = $full_venue_details_url.$venue_name;
		$google_api_id = "AIzaSyAI23cKPZfphDztkBoixmq1yBh2PNNibzU";


		
		if ($from == "here" || $locn == ""){
			
			//Get the GeoHash Code

			$geoHash = encode($lat, $lon);
        }

        if ($distance == ""){

			// If Distance is not entered then Set it to default of 10 Miles.

        	$distance = "10";
    	}

        if ($from != "here"){

        	//Build the GeoLocation Google API.

        	$locationToGeoCodeURL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
			$key_param = "key=".$google_api_id;

        	$googleGeoCodeURL = $locationToGeoCodeURL.urlEncoding($locn)."&".$key_param;
        	$googleLocation = json_decode(file_get_contents($googleGeoCodeURL));

        	if ($googleLocation->status == "OK") {
        		
        		//If the Status is succesfully retrived.

				$lat = $googleLocation ->results[0]->geometry->location->lat;

				$lon = $googleLocation->results[0]->geometry->location->lng;
				$geoHash = encode($lat, $lon);
				//echo "  " . $geoHash;

			}else if($googleLocation->status == "ZERO_RESULTS"){

				//If the entered GeoLocation is Not Valid.

				echo "<div id='getNotFound'><center><b>Sorry Could not find the GeoLocation of $locn. Please try Again.</b><center></div>";
				$clearForm = 5;
			}
        }


        // Build the event Search URL.

		if($category == "default") $segmentId = "";
		else if($category == "music")	$segmentId = "KZFzniwnSyZfZ7v7nJ";
		else if($category == "sports")	$segmentId = "KZFzniwnSyZfZ7v7nE";
		else if($category == "artsandtheatre") $segmentId = "KZFzniwnSyZfZ7v7na";
		else if($category == "film") $segmentId = "KZFzniwnSyZfZ7v7nn";
		else if($category == "miscellaneous") $segmentId = "KZFzniwnSyZfZ7v7n1";

		$defaultURL = "https://app.ticketmaster.com/discovery/v2/events.json?";
		$key_param = "apikey=".$ticketmaster_access_id."&";
		$keywordValue = "keyword=".$keyword."&";
		$searchType = "segmentId=".$segmentId."&";
		$radiusValue = "radius=".$distance."&unit=miles&";
		$geoPoint = "geoPoint=".$geoHash;
		
		
		$ticketMasterSearchURL = $defaultURL.$key_param.$keywordValue.$searchType.$radiusValue.$geoPoint;
		//echo "  " . $ticketMasterSearchURL;
		$eventStatus = "search";
	}

	//When the event in result table is selected, Now display the Event Details.

	if($eventRequired == "eventSelected"){

		//When now event is clicked now display the event details HTML.
		$eventStatus = "eventDetails";
	}

?>

<html>
	<head>
		<title> Event Search </title>

	<style>

		/* Event Search Form Styling */ 

		*,body,html{

			padding: 0;
			margin: 0;	
		}


		.event-Heading{

			text-align: center;
			font-style: italic;
			font-weight: 550;
			font-size: 33px;
		}


		.searchForm{

			background-color: #FAFAFA;
			position: relative;
			margin-right: auto;
			border: 2px solid gray;
			top: 28px;
			width: 650px;
			height: 220px;
			margin-left: auto;
		}


		.underline{

			border-bottom: 2px solid lightgray; 
			margin: auto;
			padding-top: 4px;
			width: 98%;
		}
		

		.form{

			padding-top: 15px;
		}

		.inputTable td{

			padding-left: 12px;
		}

		#searchButton{

			margin-left: 65px;
			padding-left: 5px;
			padding-right: 5px;
		}

		#clearButton{
			padding-left: 5px;
			padding-right: 5px;
		}

		a{
			text-decoration: none;
			color: black;
		}

		a:hover{
   			color: #d3d3d3;
		}

		/* Search Results Table Styling */

		#search-results-table{
	
			margin: auto;
			width: 85%;
			margin-top: 72px;
			table-layout: center;
			border: 2px #d3d3d3 solid; 
			border-collapse: collapse;
		}

		#search-results-table td, th{

			border: 2px #d3d3d3 solid;
			border-collapse: collapse;
			padding-top: 3px;
			padding-bottom: 3px;
			padding-left: 5px; 

		}


		#table-row-1{

			width: 10%; 
		}

		#table-row-2{

			width: 10%;
		}

		#table-row-3{

			width: 40%; 
		}

		#table-row-4{

			width: 10%; 
		}

		#table-row-5{

			width: 40%; 
		}
		
		/* Event Details Styling */

		#event-details-div{

			width: 900px;
			margin: auto;
			display: block;
		}

		.event-info-withoutSeatMap{

			width: 30%;
			margin: auto;
		}

		.event-info-withoutSeatMap h4{
			margin:0;
			padding: 2px;
			font-size: 20px;
		}

		.event-info-withoutSeatMap a{
			text-decoration: none;
			color: black;
		}

		.event-info-withoutSeatMap a:hover{
			color: #d3d3d3;
		}

		.event-info{
			float: left;
		}


		.event-info h4{
			margin:0;
			padding: 2px;
			font-size: 20px;
		}

		.event-info a{
			text-decoration: none;
			color: black;
		}

		.event-info a:hover{
			color: #d3d3d3;
		}

		/*This is to maintain the length of the Artist/Team Line to prevent the long text from pushing the image down. */

		.longText{
			max-width: 300px;
		}

		.seatMapDiv{
			float: right;
			overflow: hidden;
		}

		.seatMapDiv img{
			margin-top: 50px;
			margin-right: 50px;
		}

		/* Venue Details Styling */

		#venue-detials-div{
			clear: left;
			
		}

		.venue-details-html table{
			width: 900px;
			margin: auto;
		}

		.venue-details-html table td, th{

			border: 1px solid black;
			border-collapse: collapse;
		}

		.venue-details-html td, th{
			padding-top: 3px;
			padding-bottom: 3px;
			padding-left: 5px; 
		}

		#venueLeftCol{
			width:30%;
		}

		.container_all{
			width: 900px;
			margin: auto;
		}

		.style-arrow{
			margin-top: 5px;
			margin-bottom: 5px;
			width: 5%;
			height: 5%;
		}

		.venueDetailsClass{
			width: 900px;
			margin: auto;
		}


		#venue-click-open{

		}

		#venue-click-close{
			display: none;
		}

		#photo-open-text{

		}

		#photo-close-text{
			display: none;
		}

		#venue-up-arrow{
			display: none;
		}

		#venue-down-arrow{

		}

		#downArrowPhoto{

		}

		#upArrowPhoto{
			display: none;
		}


		#venue-table{
			margin: auto;
			width: 100%;
			border: 1px solid #d3d3d3;
			border-collapse: collapse;
			display: none;
		}

		#venue-table th{
			font-weight: normal;
		}

		#venue-table td, th{
			border: 2px solid #d3d3d3;
			border-collapse: collapse;
			width: 750px;
			padding-top: 3px;
			padding-bottom: 3px;
		}

		#venueMap{
			width: 380px;
			height: 300px;
			margin-top: 5px;
			margin-bottom: 5px;
			margin: auto;
		}

		#venue-table .travellingMode{

			margin: 0;
			padding: 0;
			font-size: 15px;
			display: inline-block;
			float: left;
			margin-top: 25px;
			margin-left: 30px;
		}

		#venue-table .travellingMode li{

			list-style-type: none;
			background-color: #F0F0F0;
			padding-top: 2.5px;
			padding-bottom: 2.5px;
			padding-left: 2.5px;
		}

		#venue-table .travellingMode li:hover{

			color: lightgray;
		}

		#venuePhotos{

			width: 80%;
			margin: auto;
			display: none;
		}

		#venuePhotos tr{
			outline: thin solid;
		}

		#venuePhotos td{
			margin-bottom: 20px;
		}


		/* Maps Styling */

		#mapOnVenueClicked{

			position: absolute;
			width: 600px;
			height: 600px;
			display: none;
		}


		#map{

		  width: 300px;
		  height: 380px;
		  position: absolute;
	    }

		#travelMode{

			width: 100px;
			position: absolute;
			display: none;
		}


		#travelMode .travellingMode{

			list-style-type: none;
		}


		#travelMode .travellingMode li{

			background-color: #F0F0F0;
			padding-bottom: 2px;
			padding-left: 2px;
			padding-top: 2px;
		}


		#travelMode .travellingMode li a{

			text-decoration: none;
			color: black;
		}

		#travelMode .travellingMode li:hover{

			background-color: lightgray;
		}

	</style>

	</head>

	<body>

		<!-- This is the Event Search Form -->

		<div class = "searchForm">
			<h2 class = "event-Heading"> Events Search </h2>
			<div class = "underline"> </div>

			<form class = "form" id = "form" action = "" method = "post">

				<table class="inputTable">
					<tr>
						<td><b>Keyword</b></td>
						<td><input type = "text" name = "keyword" id = "keyword" required></td>
					</tr>

					<tr>
						<td><b>Category</b></td>
						<td><select name = "category" id = "category">
						<option value = "default"> Default </option>
						<option value = "music"> Music </option>
						<option value = "sports"> Sports </option>
						<option value = "artsandtheatre"> Arts &amp; Theatre </option>
						<option value = "film"> Film </option>
						<option value = "miscellaneous"> Miscellaneous </option></select>
						</td>
					</tr>

					<tr>
						<td><b>Distance (miles)</b></td>
						<td><input type = "text" name = "distance" id = "distance" placeholder = "10"></td>
						<td><b>from</b></td>
						<td><input type = "radio" name = "from" value = "here" id = "hereRadio" onclick="document.getElementById('locationName').readOnly = true; document.getElementById('locationName').required = false" checked>Here</td>
					</tr>

					<tr>
						<td></td>
						<td></td>
						<td></td>
						<td><input type = "radio" name = "from" value = "locn" id = "locationRadio" onclick = "document.getElementById('locationName').readOnly = false; document.getElementById('locationName').required = true"> <input type = "text" name = "locn" id = "locationName" placeholder = "location" readonly></td>
					</tr>

					<tr>
						<td><button type = "submit" name = "searchButton" id = "searchButton" disabled = "">Search</button></td>
						<td><input type = "button" id = "clearButton" name = "clear" value = "Clear" onclick = "formClear(0)"></td>
					</tr>

					<!-- Here the Hidden type in HTML is used to pass values from JavaScript to PHP -->

					<tr>
						<td><input type="hidden" name="lat" id = "lat"></td>
						<td><input type="hidden" name="lon" id = "lon"></td>
						<td><input type="hidden" name="event_id" id = "event_id"></td>
						<td><input type="hidden" name="venue_name" id = "venue_name"></td>
						<td><input type="hidden" name="eventRequired" id = "eventRequired" value = "submitted"></td> 
					</tr>
				</table>

			</form>
		</div>

		<!-- This is the Google Map, which is displayed when the venue of the Search Table is Clicked -->

		<div class="mapOnVenueClicked" id="mapOnVenueClicked">

			<div id="map">
				<!-- The Google Map comes Here -->
			</div>


			<div class="travelMode" id="travelMode">
			<ul class="travellingMode">
				<li><a href = "#/" onclick="calculateAndDisplayRoute(directionsService, directionsDisplay, 'WALKING');"> Walk there </a></li>
				<li><a href = "#/" onclick="calculateAndDisplayRoute(directionsService, directionsDisplay, 'BICYCLING');"> Bike there </a></li>
				<li><a href = "#/" onclick="calculateAndDisplayRoute(directionsService, directionsDisplay, 'DRIVING');"> Drive there </a></li>
			</ul>
			</div>

		</div>

		<div id="search-results-table-div">
			
			<table id="search-results-table">
				
			</table>
		</div>

		<div class="container_all">
			<div class="top-Div" id="event-details-div">

			</div>

			<div class="bottom-Div" id="venue-detials-div">

			</div>
		</div>

		<!-------------------------------------- Java Script ------------------------------------------->

		<script type="text/javascript">
			
			var lat = "";
			var lon = "";
			var keyword = "";
			var category = "";
			var distance = "";
			var locn = "";
			var here = "";
			var directionsDisplay;
			var directionsService;
			
        	//To clear the Form after the button clear is pressed.

			function formClear(clearError){

            	document.getElementById('keyword').value = "";
        		document.getElementById('distance').value = "";
        		document.getElementById('category').value = "default";
        		document.getElementById('search-results-table').style.display = "none";
        		document.getElementById('venue-detials-div').style.display = "none";
        		document.getElementById('event-details-div').style.display = "none";


        		if (document.getElementById('locationRadio').checked == true){

        			document.getElementById('locationName').value = "";
        			document.getElementById('locationName').readOnly = true;
        			document.getElementById('locationRadio').checked = false; 
        			document.getElementById('hereRadio').checked = true; 
        		}

        		if(clearError != 5){
        			document.getElementById('getNotFound').style.display = 'none';
        		}
       
        	}

        	//To get the client location.

        	function getTheGeoLocation(path, success, error){

			    var xhr = new XMLHttpRequest();
			    xhr.onreadystatechange = function()
			    {
			        if(xhr.readyState === XMLHttpRequest.DONE) {
			            if(xhr.status === 200) {
			                if(success)
			                    success(JSON.parse(xhr.responseText));			 
			            }else{
			                if(error)
			                	error(xhr);
			            }
			        }
			    };
			    xhr.open("GET", path, false);
			    xhr.send();
			}

			//To enable Submit button after getting the Client's Location.

			function enableSubmit(){
        		document.getElementById('searchButton').disabled = false; 
        	}

        	//When the browser loads.

        	window.onload = function executeAtStart(){

        		var clearForm = <?php echo $clearForm; ?>;

				if (clearForm == 5){ 
					formClear(5);
				}

				//Get the GeoLocation of the Client and enable the Submit Buttion.

        		getTheGeoLocation('http://ip-api.com/json?',
			        function(data){ 
			        	document.getElementById('lat').value = data['lat'];
						document.getElementById('lon').value = data['lon'];
						enableSubmit();
			        },
			        function(xhr){ console.error(xhr); console.log("Connect to the Internet and Try Again"); }
				);

        		// Get the values entered in the Form to retain the Form Values.

				keyword = '<?php echo $keyword ?>';
        		category = '<?php echo $category ?>';
        		distance = '<?php echo $distance ?>';
        		retainDistance = '<?php echo $retainDistance ?>';
        		from = '<?php echo $from ?>';
        		retainLocation = '<?php echo $retainLocation ?>';
        		retainKeyword = '<?php echo $retainKeyword ?>';

        		
        		//This is to Retain the Form Values after submitting the Form.

        		if (keyword != ""){

        			document.getElementById('keyword').value = retainKeyword;
        		}

        		document.getElementById('category').value = category;
        		document.getElementById('distance').value = retainDistance;

        		if (from == 'locn'){

        			document.getElementById('locationRadio').checked = true;
        			document.getElementById('locationName').value = retainLocation;
        			document.getElementById('locationName').readOnly = false;
        		}

        		//Initialize all the Variable to build the Results HTML.

				var html_search_details = "";
				var html_event_details = "";
				var html_venue_details = "";
				var html_image_details = "";
				var html_photo_details = "";

				// Get the Status to decide which HTML to Build and Display.

				var eventStatus = '<?php echo $eventStatus ?>';

        	if (eventStatus == "search"){


        			//Get the Event Search Results JSON.

        			var jsonOfEventSearchResults = "";
					jsonOfEventSearchResults = <?php $eventJsonResults = (file_get_contents($ticketMasterSearchURL)); echo json_encode($eventJsonResults) ?>;			
					results_json = JSON.parse(jsonOfEventSearchResults);

        			// Start of Search Table.

        			var length = "";
    				
    				if(results_json['_embedded'] != undefined){
    					if(results_json['_embedded']['events'] != undefined)	length = results_json['_embedded']['events'].length;
    					else 	length = 0;
    				}else 	length = 0;

        			if(length > 0){

        				var date = "";
						var time = "";
						var event = "";
						var genre = "N/A";
						var venue = "N/A";
						var icon_url = "";
						var id = "";
						var lat_map = "";
						var lon_map = "";

        				html_search_details += '<tr>\
										<th id="table-row-1">Date</th>\
										<th id="table-row-2">Icon</th>\
										<th id="table-row-3">Event</th>\
										<th id="table-row-4">Genre</th>\
										<th id="table-row-5">Venue</th>\
									</tr>';

        				for(var i = 0; i < length; i++){

        					if(results_json['_embedded']['events'][i]['dates'] != undefined){
        						if(results_json['_embedded']['events'][i]['dates']['start'] != undefined){
        							if(results_json['_embedded']['events'][i]['dates']['start']['localDate'] != undefined){
        								date = results_json['_embedded']['events'][i]['dates']['start']['localDate'];
        							}
        						}		
        					}

        					if(results_json['_embedded']['events'][i]['dates'] != undefined){
        						if(results_json['_embedded']['events'][i]['dates']['start'] != undefined){
        							if(results_json['_embedded']['events'][i]['dates']['start']['localTime'] != undefined){
        								time = results_json['_embedded']['events'][i]['dates']['start']['localTime'];
        							}
        						}
        					}

        					if(results_json['_embedded']['events'][i]['name'] != undefined){
        						event = results_json['_embedded']['events'][i]['name'];
        					}
							
							if(results_json['_embedded']['events'][i]['classifications'] != undefined){
								if(results_json['_embedded']['events'][i]['classifications'][0]['segment'] != undefined){
									if(results_json['_embedded']['events'][i]['classifications'][0]['segment']['name'] != undefined){
										genre = results_json['_embedded']['events'][i]['classifications'][0]['segment']['name'];
									}
								}
							}
        					
        					if(results_json['_embedded']['events'][i]['_embedded'] != undefined){
        						if(results_json['_embedded']['events'][i]['_embedded']['venues'] != undefined){
        							if(results_json['_embedded']['events'][i]['_embedded']['venues'][0]['name'] != undefined){
        								venue = results_json['_embedded']['events'][i]['_embedded']['venues'][0]['name'];
        							}
        						}
        					}
							
							if(results_json['_embedded']['events'][i]['images'] != undefined){
								if(results_json['_embedded']['events'][i]['images'][0]['url'] != undefined){
									icon_url = results_json['_embedded']['events'][i]['images'][0]['url'];
								}
							}

							if(results_json['_embedded']['events'][i]['id'] != undefined){
								id = results_json['_embedded']['events'][i]['id'];
							}

							if(results_json['_embedded']['events'][i]['_embedded'] != undefined){
								if(results_json['_embedded']['events'][i]['_embedded']['venues'] != undefined){
									if(results_json['_embedded']['events'][i]['_embedded']['venues'][0]['location'] != undefined){
										if(results_json['_embedded']['events'][i]['_embedded']['venues'][0]['location']['latitude'] != undefined){
											lat_map = results_json['_embedded']['events'][i]['_embedded']['venues'][0]['location']['latitude'];
										}
									}
								}
							}

							if(results_json['_embedded']['events'][i]['_embedded'] != undefined){
								if(results_json['_embedded']['events'][i]['_embedded']['venues'] != undefined){
									if(results_json['_embedded']['events'][i]['_embedded']['venues'][0]['location'] != undefined){
										if(results_json['_embedded']['events'][i]['_embedded']['venues'][0]['location']['longitude'] != undefined){
											lon_map = results_json['_embedded']['events'][i]['_embedded']['venues'][0]['location']['longitude'];
										}
									}
								}
							}
        					
        					html_table_row = '<tr>\
        											<td>' + date + '<br>' + time + '</td>\
        											<td>'+'<img src="'+icon_url+'"style="width:70px;height:50px;margin:auto;">'+'</td>\
        											<td>' + '<a href=#/ onclick="getEventDetails(\'' + id + 
        											'\',\'' + venue + '\')"; >' + event + '</a>' + '</td>\
        											<td>'+ genre +'</td>\
        											<td>' + '<a href ="#/" onclick="initMap(' + lat_map + ',' + lon_map + '); getTheMapHere();">' + venue + '</a>' + '</td>\
												</tr>';

							html_search_details = html_search_details + html_table_row;

        				}
        				//html_search_details += '<br><br><br>';
        				document.getElementById('search-results-table').innerHTML = html_search_details;
        			}
        			else{

        				//No Record Found.
						document.getElementById('search-results-table').innerHTML = '<tr><td ><center>No Records have been found</center></td></tr>';
					}

					//End of Serach Table.

				}else if(eventStatus == "eventDetails"){

					//Start of Event Details.

					jsonOfEventDetails = <?php $eventDetails_result = (file_get_contents($full_event_details_url)); 
					echo json_encode($eventDetails_result); ?>;			
					event_details_results = JSON.parse(jsonOfEventDetails);	

					var eventName = "";
					var localdate = "";
					var localtime = "";
					var attractionLength = "";
					var attraction = "";
					var venue = "";
					var genre = "";
					var subGenre = "";
					var segment = "";
					var subType = "";
					var type = "";
					var Genres = "";
					var minPrice = "";
					var maxPrice = "";
					var currency = "";
					var pricerange = "";
					var status = "";
					var buyticket = "";
					var seatmap = "";
					var artistUrl = "";
					var htmlOfArtist = "";

					if(event_details_results['name'] != undefined){
						eventName = event_details_results['name'];
					}

					if(event_details_results['dates'] != undefined){
						if(event_details_results['dates']['start'] != undefined){
							if(event_details_results['dates']['start']['localDate'] != undefined){
								localdate = event_details_results['dates']['start']['localDate'];
							}
							if(event_details_results['dates']['start']['localTime'] != undefined){
								localtime = event_details_results['dates']['start']['localTime'];
							}
						}

						if(event_details_results['dates']['status'] != undefined){
							if(event_details_results['dates']['status']['code'] != undefined){
								status = event_details_results['dates']['status']['code'];
							}
						}
					}
					
					if(event_details_results['_embedded'] != undefined){
						if(event_details_results['_embedded']['attractions'] != undefined){
							attractionLength = event_details_results['_embedded']['attractions'].length;
							for(var i = 0; i < attractionLength; i++){
								attraction = event_details_results['_embedded']['attractions'][i]['name'];
								artistUrl = event_details_results['_embedded']['attractions'][i]['url'];
								htmlOfArtist += '<a href = "' + artistUrl + '" target = "_blank">' + attraction + '</a>';
								if(i != attractionLength - 1)	htmlOfArtist += " | ";
							}
						}

						if(event_details_results['_embedded']['venues'] != undefined){
							if(venue = event_details_results['_embedded']['venues'][0]['name'] != undefined){
								venue = event_details_results['_embedded']['venues'][0]['name'];
							}
						}
					}
					

					if(event_details_results['classifications'] != undefined){
						if(event_details_results['classifications'][0]['genre'] != undefined){
							if(event_details_results['classifications'][0]['genre']['name'] != undefined){
								genre = event_details_results['classifications'][0]['genre']['name'];
							}
						}
						if(event_details_results['classifications'][0]['subGenre'] != undefined){
							if(event_details_results['classifications'][0]['subGenre']['name'] != undefined){
								subGenre = event_details_results['classifications'][0]['subGenre']['name'];
							}
						}
						if(event_details_results['classifications'][0]['segment'] != undefined){
							if(event_details_results['classifications'][0]['segment']['name'] != undefined){
								segment = event_details_results['classifications'][0]['segment']['name'];
							}
						}
						if(event_details_results['classifications'][0]['subType'] != undefined){
							if(event_details_results['classifications'][0]['subType']['name'] != undefined){
								subType = event_details_results['classifications'][0]['subType']['name'];
							}
						}
						if(event_details_results['classifications'][0]['type'] != undefined){
							if(event_details_results['classifications'][0]['type']['name'] != undefined){
								type = event_details_results['classifications'][0]['type']['name'];
							}
						}

						if(subGenre != "" && subGenre != "Undefined")	Genres += subGenre;
						if(genre != "" && genre != "Undefined")	Genres += " | " + genre;
						if(segment != "" && segment != "Undefined")	Genres += " | " + segment;
						if(subType != "" && subType != "Undefined")	Genres += " | " + subType;
						if(type != "" && type != "Undefined")	Genres += " | " + type;
					}
					
					if(event_details_results['priceRanges'] != undefined){
						if(event_details_results['priceRanges'][0]['min'] != undefined){
							minPrice = event_details_results['priceRanges'][0]['min'];
						}
						if(event_details_results['priceRanges'][0]['max'] != undefined){
							maxPrice = event_details_results['priceRanges'][0]['max'];
						}
						if(event_details_results['priceRanges'][0]['currency'] != undefined){
							currency = event_details_results['priceRanges'][0]['currency'];
						}
						if(minPrice != "" && maxPrice == "" && currency != ""){
							pricerange = minPrice + " " + currency;
						}else if(minPrice == "" && maxPrice != "" && currency != ""){
							pricerange = maxPrice + " " + currency;
						}else if(minPrice != "" && maxPrice != "" && currency != ""){
							pricerange = minPrice + " - " + maxPrice + " " + currency;	
						}
						
					}
					
					if(event_details_results['url'] != undefined){
						buyticket = event_details_results['url'];
					}
				
					if(event_details_results['seatmap'] != undefined){
						if(event_details_results['seatmap']['staticUrl'] != undefined){
							seatmap = event_details_results['seatmap']['staticUrl'];
						}	
					}

					html_event_details += '<div class="event-details-html">';
					html_event_details += '<h3 style="text-align:center"><b>' + eventName + '</b></h3>';
					if(seatmap == ""){
						html_event_details += '<div class="event-info-withoutSeatMap">';
					}else{
						html_event_details += '<div class="event-info">';
					}
					
					if(localdate != ""){
						html_event_details += '<h4><b> Date </b></h4>' + localdate;
						if(localtime != ""){
							html_event_details += ' ' + localtime;
						}  
					}

					if(htmlOfArtist != ""){
						html_event_details += '<h4><b> Artist / Team </b></h4>' + '<div class="longText">' + htmlOfArtist + '</div>';
					}

					if(venue != ""){
						html_event_details += '<h4><b> Venue </b></h4>' + venue;
					}

					if(Genres != ""){
						html_event_details += '<h4><b> Genres </b></h4>' + Genres;
					}

					if(pricerange != ""){
						html_event_details += '<h4><b> Price Ranges </b></h4>' + pricerange;
					}

					if(status != ""){
						html_event_details += '<h4><b> Ticket Status </b></h4>' + status;
					}

					if(buyticket != ""){
						html_event_details += '<h4><b> Buy Ticket At: </b></h4><a href="' + buyticket + '" target="_blank"> Ticketmaster </a>';
					}

					html_event_details += '</div>';

					if(seatmap != ""){
						html_event_details += '<div class="seatMapDiv">';
						html_event_details += '<img src="' + seatmap + '" style="width:380px; height:240px; float: right;"></img>';
						html_event_details += '</div>';
					}

					html_event_details += '</div>';
					document.getElementById('event-details-div').innerHTML = html_event_details;

					// End of Event Details.

					//Start of Venue Details.

					//Get the JSON of the Venue Details.

					jsonOfVenueDetails = <?php sleep(2);  $venueDetails_result = (file_get_contents($full_venue_details_url)); 
					echo json_encode($venueDetails_result); ?>;			
					venue_details_results = JSON.parse(jsonOfVenueDetails);	

					html_venue_details = "";
					var venueLen = 0;
					if(venue_details_results['_embedded'] != undefined){
						if(venue_details_results['_embedded']['venues'] != undefined){
							venueLen = 1;
						}
					}

					html_venue_details = '<br><br><br><div class="venueDetailsClass"><center><span class="venue-click-open" id="venue-click-open">click to show venue info</span></center><center><span class="venue-click-close" id="venue-click-close">click to hide venue info</span></center><center><a href="#/" class="venue-down-arrow" id="venue-down-arrow" onclick="showVenueTable()"><img src="http://csci571.com/hw/hw6/images/arrow_down.png" class="style-arrow"></a></center><center><a href="#/" class="venue-up-arrow" id="venue-up-arrow" onclick="hideVenueTable()"><img src="http://csci571.com/hw/hw6/images/arrow_up.png" class="style-arrow"></a></center><center><table id="venue-table">'

					if(venueLen == 1){

						var venueName = "N/A";
						var venueLat = "N/A";
						var venueLon = "N/A";
						var venueAdd = "N/A";
						var venueCity = "N/A";
						var venueSCode = "N/A";
						var venuePCode = "N/A";
						var upcomingEvent = "N/A";
						var venueCityCode = "N/A";

						if(venue_details_results['_embedded']['venues'][0]['name'] != undefined){
							venueName = venue_details_results['_embedded']['venues'][0]['name'];
						}

						if(venue_details_results['_embedded']['venues'][0]['location'] != undefined){
							if(venue_details_results['_embedded']['venues'][0]['location']['longitude'] != undefined){
								venueLon = venue_details_results['_embedded']['venues'][0]['location']['longitude'];
							}
							if(venue_details_results['_embedded']['venues'][0]['location']['latitude'] != undefined){
								venueLat = venue_details_results['_embedded']['venues'][0]['location']['latitude'];
							}
						}

						if(venue_details_results['_embedded']['venues'][0]['address'] != undefined){
							if(venue_details_results['_embedded']['venues'][0]['address']['line1'] != undefined){
								venueAdd = venue_details_results['_embedded']['venues'][0]['address']['line1'];
							}
						}

						if(venue_details_results['_embedded']['venues'][0]['city'] != undefined){
							if(venue_details_results['_embedded']['venues'][0]['city']['name'] != undefined){
								venueCity = venue_details_results['_embedded']['venues'][0]['city']['name'];
							}
						}

						if(venue_details_results['_embedded']['venues'][0]['state'] != undefined){
							if(venue_details_results['_embedded']['venues'][0]['state']['stateCode'] != undefined){
								venueSCode = venue_details_results['_embedded']['venues'][0]['state']['stateCode'];
							}
						}

						if(venue_details_results['_embedded']['venues'][0]['postalCode'] != undefined){
							venuePCode = venue_details_results['_embedded']['venues'][0]['postalCode'];
						}

						if(venue_details_results['_embedded']['venues'][0]['url'] != undefined){
							upcomingEvent = venue_details_results['_embedded']['venues'][0]['url'];
						}

						if(venueCity != "N/A"){
							venueCityCode = venueCity;
							if(venueSCode != "N/A"){
								venueCityCode += ", " + venueSCode;
							}
						}

						html_venue_details = '<br><br><br><div class="venueDetailsClass"><center><span class="venue-click-open" id="venue-click-open">click to show venue info</span></center><center><span class="venue-click-close" id="venue-click-close">click to hide venue info</span></center><center><a href="#/" class="venue-down-arrow" id="venue-down-arrow" onclick="showVenueTable(); getMap('+ venueLat + ',' + venueLon + ');"><img src="http://csci571.com/hw/hw6/images/arrow_down.png" class="style-arrow"></a></center><center><a href="#/" class="venue-up-arrow" id="venue-up-arrow" onclick="hideVenueTable()"><img src="http://csci571.com/hw/hw6/images/arrow_up.png" class="style-arrow"></a></center><center><table id="venue-table"><tr>\
												<th style="text-align:right; padding-right:5px"><b>Name</b></th>\
    											<th style="text-align: center;">' + venueName + '</th>\
											</tr><tr>\
												<td style="text-align:right; padding-right:5px; width: 30%"><b>Map</b></td>\
    											<td id= "mapSize"><div>\
		<ul class="travellingMode">\
			<li><a href="#/" onclick="calculateAndDisplay(\'WALKING\'); "> Walk there </a></li>\
			<li><a href="#/" onclick="calculateAndDisplay(\'BICYCLING\'); "> Bike there </a></li>\
			<li><a href="#/" onclick="calculateAndDisplay(\'DRIVING\'); "> Drive there </a></li>\
		</ul>\
		</div><div id="venueMap"></div></td>\
												</tr><tr>\
												<td style = "text-align:right; padding-right:5px" ><b>Address</b></td>\
    											<td style = "text-align: center;" >' + venueAdd + '</td>\
											</tr><tr>\
												<td style = "text-align:right; padding-right:5px" ><b>City</b></td>\
    											<td style = "text-align: center;" >' + venueCityCode + '</td>\
											</tr><tr>\
												<td style = "text-align:right; padding-right:5px" ><b>Postal Code</b></td>\
    											<td style = "text-align: center;" >' + venuePCode + '</td>\
											</tr><tr>\
												<td style="text-align:right; padding-right:5px"><b>Upcoming Events</b></td>\
    											<td style = "text-align: center;" ><a href="' + upcomingEvent + '" target="_blank">'+ venueName + ' Tickets</a></td>\
											</tr></table></center></div>';

					}else{

						//No Venue Info Found.
						html_venue_details += '<tr><th><b>No Venue info Found</b></th></tr></table></center></div>';
					}	

					// End of Venue Details.

					//Start of Venue Photos

					var html_photo_details = '<br><br><div class="photos"><center><span class="photo-open-text" id="photo-open-text">click to show venue photos</span></center><center><span id="photo-close-text" class="photo-close-text">click to hide venue photos</span></center><center><a href="#/" id="downArrowPhoto" class="downArrowPhoto" onclick="showVenuePhotos()"><img src="http://csci571.com/hw/hw6/images/arrow_down.png" class="style-arrow"></a></center><center><a href="#/" id="upArrowPhoto" class="upArrowPhoto" onclick="hideVenuePhotos()"><img src="http://csci571.com/hw/hw6/images/arrow_up.png" class="style-arrow"></a></center><center><table id="venuePhotos">'; 

						var imagesLength = 0;

						if(venue_details_results['_embedded'] != undefined){
							if(venue_details_results['_embedded']['venues'] != undefined){
								if(venue_details_results['_embedded']['venues'][0]['images'] != undefined){
									imagesLength = venue_details_results['_embedded']['venues'][0]['images'].length;
								}
							}
						}

						if(imagesLength != 0){

								for(var i = 0; i < imagesLength; i++){

									var imageUrl = venue_details_results['_embedded']['venues'][0]['images'][i]['url'];
									html_photo_details += '<center><tr><td><img src="' + imageUrl + '" width="700px" height="350px" align="middle"></img></td></tr></center>';
								}

						}else{

							//No venue Photos Found.
							html_photo_details += '<tr><th><b>No Venue Photos Found<b></th></tr>';
						}

						html_photo_details += '</table></center></div>';
						html_venue_details += html_photo_details;
						html_venue_details += '<br><br><br><br><br><br>';

						//End of Venue Photos

						document.getElementById('venue-detials-div').innerHTML = html_venue_details;
				}
			}

			//When we Click on the Particular Event get the Event, Venue and Photo Details

			function getEventDetails(event_id, venue_name){

	        		document.getElementById('event_id').value = event_id;
	        		document.getElementById('venue_name').value = venue_name;
	        		document.getElementById('eventRequired').value = 'eventSelected';
	        		//Submit the form again now to wipe off the results table and display the Event Details, venue info and photos.
	        		var getForm = document.getElementById('form');
	        		getForm.submit();
        	}

        	//This is the Google Map to be displayed in the Venue Details Table.
        	//The Map will initially will have the Marker of the Destination's Position.

    		function getMap(destLat, destLong){
			
				console.log("Hello here getMap");
				console.log(destLat);
				console.log(destLong);
				destinationLatitude = destLat;
				destinationLongitude = destLong;
				uluru = {lat: destLat, lng: destLong}
				directionsDisplay = new google.maps.DirectionsRenderer;
				directionsService = new google.maps.DirectionsService;
				var map = new google.maps.Map(document.getElementById('venueMap'),{
					zoom: 14,
				    center: {lat: destLat, lng: destLong}
				});

				var marker = new google.maps.Marker({
			    	position: uluru,
			    	map: map
			  	});
				directionsDisplay.setMap(map);
				console.log(directionsService);
				console.log(directionsDisplay);
				console.log("This is taking a lot of time.");
			}

			function calculateAndDisplay(mode){

				//Get the Travelling Mode.

		    	var selectedMode = mode;

		    	//Get the Client Location.

		    	lat_start = <?php echo $lat ?>;
			    lon_start = <?php echo $lon ?>;

			    //Initialize the Directions Service.

			    directionsService.route({
			    origin: {lat: lat_start, lng: lon_start},
			    destination: {lat: destinationLatitude, lng: destinationLongitude}, 
			    travelMode: google.maps.TravelMode[selectedMode]
			    //Set the Travel Mode.

			    }, function(response, status){
			    if (status == 'OK') {

			      directionsDisplay.setDirections(response);
			    }else{
			    	//Cannot get the Directions.
			    	window.alert('Failed to get the Direction because of ' + status);
			    }
		  });
			}

			//Handling the behaviour of the Venue and Photo Arrows.

			function showVenueTable(){

				//We need to display the Venue Info Table and Close the Venue Photos.
				document.getElementById('venue-up-arrow').style.display = 'block';
				document.getElementById('venue-table').style.display = 'block';
				document.getElementById('venue-down-arrow').style.display = 'none';
				document.getElementById('venue-click-close').style.display = 'block';
				document.getElementById('venue-click-open').style.display = 'none';
				hideVenuePhotos();
			}

			function showVenuePhotos(){

				//Display the Venue Photos and Hide the Venue table if open.
				document.getElementById('upArrowPhoto').style.display = 'block';
				document.getElementById('venuePhotos').style.display = 'block';
				document.getElementById('downArrowPhoto').style.display = 'none';
				document.getElementById('photo-open-text').style.display = 'none';
				document.getElementById('photo-close-text').style.display = 'block';
				hideVenueTable();
			}

			function hideVenueTable(){

				// TO hide the Venue Table set the display to None.
				document.getElementById('venue-up-arrow').style.display = 'none';
				document.getElementById('venue-table').style.display = 'none';
				document.getElementById('venue-down-arrow').style.display = 'block';
				document.getElementById('venue-click-close').style.display = 'none';
				document.getElementById('venue-click-open').style.display = 'block';
			}

			function hideVenuePhotos(){
				document.getElementById('upArrowPhoto').style.display = 'none';
				document.getElementById('venuePhotos').style.display = 'none';
				document.getElementById('downArrowPhoto').style.display = 'block';
				document.getElementById('photo-open-text').style.display = 'block';
				document.getElementById('photo-close-text').style.display = 'none';
			}

			//Add a EventListener for the Mouse Click to find the position of the Cursor
			document.addEventListener('click', getCursorPosition, true);

			var xCoordinate, yCoordinate;

			function getCursorPosition(cursorObject){

				//Get the cursor's x and y co-oridnates.
			    xCoordinate = cursorObject.pageX;
			    yCoordinate= cursorObject.pageY;
			}

			//Sets the Map position to the Cursor Position When Clicked.

			function getTheMapHere(){

				//Give Position for the Map to be displayed when the venue is clicked.

				document.getElementById('mapOnVenueClicked').style.left = (xCoordinate + 2) + 'px';
				document.getElementById('mapOnVenueClicked').style.top = (yCoordinate + 6) + 'px';
				console.log("Almost Done!!!!");
				
			}

		</script>


		<script async defer
			src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAI23cKPZfphDztkBoixmq1yBh2PNNibzU&">
			
			//This is for the Google Maps without the Callback. This is to make the Map runs in Asynchronuously.

		</script>


		<script type="text/javascript">
			
			var destinationLatitude = 0;
			var destinationLongitude = 0;

		
			function initMap(destLat, destLong){
				
				//Get the Destination Geolocation.

				destinationLatitude = destLat;
				destinationLongitude = destLong;
				uluru = {lat: destLat, lng: destLong}

				//Initialize the Directions Display service.

				directionsDisplay = new google.maps.DirectionsRenderer;
				directionsService = new google.maps.DirectionsService;

				var map = new google.maps.Map(document.getElementById('map'), {
					zoom: 14,
				    center: {lat: destLat, lng: destLong}
				});

				//Set the Marker to the Destination.

				var marker = new google.maps.Marker({
			    	position: uluru,
			    	map: map
			  	});
				directionsDisplay.setMap(map);

				if(document.getElementById('travelMode').style.display == "block"){

					document.getElementById('travelMode').style.display = "none";
					document.getElementById('mapOnVenueClicked').style.display = "none";

				}else{
					
					document.getElementById('travelMode').style.display = "block";
					document.getElementById('mapOnVenueClicked').style.display = "block";
				}
			}

			function calculateAndDisplayRoute(directionsService, directionsDisplay, mode){

			  var selectedMode = mode;

			  //Get the Source GeoLocation from php.

			  lat_start = <?php echo $lat ?>;
			  lon_start = <?php echo $lon ?>;


			  directionsService.route({

			    origin: {lat: lat_start, lng: lon_start},
			    destination: {lat: destinationLatitude, lng: destinationLongitude}, 
			    travelMode: google.maps.TravelMode[selectedMode]
			    //Set the Travel Mode to either Walking, Driving or Cycling.

			  }, function(response, status){
			    if(status == 'OK'){

			      directionsDisplay.setDirections(response);
			    }else{

			      window.alert('Failed to get the Direction because of ' + status);
			    }
			  });
			}

		</script>
	</body>
</html>