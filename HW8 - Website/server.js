var express = require('express');
var request = require('request');
var geohash = require('ngeohash');


var app = express();

app.use(express.static("./"));

var google_api_key = "AIzaSyAI23cKPZfphDztkBoixmq1yBh2PNNibzU";
var ticketmaster_access_id = "GfAAasFz3JZu04YljkCaq0nGuy4hz3cb";

var SpotifyWebApi = require('spotify-web-api-node');

// credentials are optional
var spotifyApi = new SpotifyWebApi({

  clientId: '47d8c288e8fd49dab52c97c7d05ab310',
  clientSecret: 'fc4ecb0d801347a09dcfb65c96666c7c',
});

app.get('/getLatandLon', function(req, res){

	console.log(req.query);
	serachLocation = req.query.searchLocation;
	console.log(serachLocation);

	var locationToGeoCodeURL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
	var keyparam = "key=" + google_api_key;
	var googleGeoCodeURL = locationToGeoCodeURL + encodeURI(serachLocation) + "&" + keyparam;
	console.log(googleGeoCodeURL);

	request.get({
	    url: googleGeoCodeURL,
	    json: true,
	    headers: {'User-Agent': 'request'}
	  }, (err, response, data) => {
	    if(err){
	    	console.log('Error:', err);
	    }else if(res.statusCode !== 200){
	    	console.log('Status:', response.statusCode);
	    }else{
	      	res.setHeader('content-type', 'application/json');
			res.json(data);
	    }
	});
})

app.get('/searchResults', function(req,res){

	console.log("Serach Results on server side");
	console.log(req.query);

	var sourceLatitude = req.query.latitude;
	var sourceLongitude = req.query.longitude;
	console.log(geohash.encode(sourceLatitude, sourceLongitude));
	var geoCode = geohash.encode(sourceLatitude, sourceLongitude);

	var baseURL = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=";
	baseURL += ticketmaster_access_id + "&"; // includes API Key.
	var keyword = "keyword=" + encodeURI(req.query.keyword) + "&";
	var segmentId = "segmentId=" + req.query.segmentId + "&";
	var radius = "radius=" + req.query.radius + "&";
	var unit = "unit=" + req.query.unit + "&";
	var geoPoint = "geoPoint=" + geoCode + "&";
	var sort = "sort=date,asc";

	var full_tickermaster_searchURL = baseURL + keyword + segmentId + radius + unit + geoPoint + sort;
	console.log(full_tickermaster_searchURL);

	//full_tickermaster_searchURL = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=GfAAasFz3JZu04YljkCaq0nGuy4hz3cb&keyword=University+of+Southern+California&segmentId=KZFzniwnSyZfZ7v7nE&radius=10&unit=miles&geoPoint=9q5cs";

	request.get({
	    url: full_tickermaster_searchURL,
	    json: true,
	    headers: {'User-Agent': 'request'}
	  }, (err, response, data) => {
	    if(err){
	    	console.log('Error:', err);
	    }else if(res.statusCode !== 200) {
	    	console.log('Status:', response.statusCode);
	    }else{
	      	res.setHeader('content-type', 'application/json');
			res.json(data);
	    }
	});

})

app.get('/getEventDetailsJson', function(req, res){

	console.log("event details on server side");
	console.log(req.query);

	var eventId = req.query.eventId;

	var full_ticketmaster_eventDetails_url = "https://app.ticketmaster.com/discovery/v2/events/";
	full_ticketmaster_eventDetails_url += eventId + "?apikey=" + ticketmaster_access_id + "&";

	console.log(full_ticketmaster_eventDetails_url);

	request.get({
	    url: full_ticketmaster_eventDetails_url,
	    json: true,
	    headers: {'User-Agent': 'request'}
	  }, (err, response, data) => {
	    if(err){
	    	console.log('Error:', err);
	    }else if(res.statusCode !== 200){
	    	console.log('Status:', response.statusCode);
	    }else{
	      	res.setHeader('content-type', 'application/json');
			res.json(data);
	    }
	});
})

app.get('/getVenueDetailsJson', function(req, res){

	console.log("Venue details on server side");
	console.log(req.query);

	var venueName = req.query.venueName;

	var full_ticketmaster_venueDetails_url = "https://app.ticketmaster.com/discovery/v2/venues?apikey=GfAAasFz3JZu04YljkCaq0nGuy4hz3cb&keyword=";
	full_ticketmaster_venueDetails_url += encodeURI(venueName);

	console.log(full_ticketmaster_venueDetails_url);

	request.get({
	    url: full_ticketmaster_venueDetails_url,
	    json: true,
	    headers: {'User-Agent': 'request'}
	  }, (err, response, data) => {
	    if(err){
	    	console.log('Error:', err);
	    }else if(res.statusCode !== 200){
	    	console.log('Status:', response.statusCode);
	    }else{
	      	res.setHeader('content-type', 'application/json');
			res.json(data);
	    }
	});
})

app.get('/getUpcomingIdDetailsJson', function(req, res){

	console.log("Upcoming details ID on server side");
	console.log(req.query);

	var venueName = req.query.venueName;

	var full_ticketmaster_upcomingIdDetails_url = "https://api.songkick.com/api/3.0/search/venues.json?query=";
	full_ticketmaster_upcomingIdDetails_url += encodeURI(venueName) + "&apikey=l7paBhMMmbuYpzpC";

	console.log(full_ticketmaster_upcomingIdDetails_url);

	request.get({
	    url: full_ticketmaster_upcomingIdDetails_url,
	    json: true,
	    headers: {'User-Agent': 'request'}
	  }, (err, response, data) => {
	    if(err){
	    	console.log('Error:', err);
	    }else if(res.statusCode !== 200){
	    	console.log('Status:', response.statusCode);
	    }else{
	      	res.setHeader('content-type', 'application/json');
			res.json(data);
	    }
	});
})

app.get('/getUpcomingDetailsJson', function(req, res){

	console.log("Upcoming details on server side");
	console.log(req.query);

	var venueId = req.query.venueId;

	var full_ticketmaster_upcomingDetails_url = "https://api.songkick.com/api/3.0/venues/";
	full_ticketmaster_upcomingDetails_url += venueId + "/calendar.json?apikey=l7paBhMMmbuYpzpC";

	console.log(full_ticketmaster_upcomingDetails_url);

	request.get({
	    url: full_ticketmaster_upcomingDetails_url,
	    json: true,
	    headers: {'User-Agent': 'request'}
	  }, (err, response, data) => {
	    if(err){
	    	console.log('Error:', err);
	    }else if(res.statusCode !== 200){
	    	console.log('Status:', response.statusCode);
	    }else{
	      	res.setHeader('content-type', 'application/json');
			res.json(data);
	    }
	});
})

app.get('/getSpotifyDetailsJson', function(req, res){

	console.log("spotify Details on Server Side.");
	console.log(req.query.keyword);
	console.log(req.query.indexId);

	spotifyApi.searchArtists(req.query.keyword)
		  	.then(function(data){

		  		console.log("Inside Spotify");
		    	console.log('Search artists: ', data.body);
		    	var myJson = {
		    		i : req.query.indexId,
		    		result : data
		    	}
		    	res.setHeader('content-type', 'application/json');
				res.json(myJson);
		    	
		  	}, function(err) {
		  		console.log("Inside Spotify Error");
		  		spotifyApi.clientCredentialsGrant().then(
					function(data) {
						console.log('The access token expires in ' + data.body['expires_in']);
						console.log('The access token is ' + data.body['access_token']);

						// Save the access token so that it's used in future calls
						spotifyApi.setAccessToken(data.body['access_token']);
						
						spotifyApi.searchArtists(req.query.keyword)
						.then(function(data) {
						   	console.log(data);
						   	var myJson = {
		    					i : req.query.indexId,
		    					result : data
		    				}

							res.setHeader('content-type', 'application/json');
							res.json(myJson);

						}, function(err) {
						    console.error(err);
						});

						},
						function(err){
						console.log('Something went wrong when retrieving an access token', err);
						}
					);
		    	console.error(err);
		});
})

app.get('/getCustomGoogleJson', function(req, res){

	console.log("Custom Google Details Server Side.");
	console.log(req.query);

	var googleBase = "https://www.googleapis.com/customsearch/v1?q=" + encodeURI(req.query.keyword);
	googleBase += "&cx=006900729227908517983:mbi1w1wjkaq&imgSize=huge&imgType=news&num=8&searchType=image&key=AIzaSyAI23cKPZfphDztkBoixmq1yBh2PNNibzU";
	console.log(googleBase);

	request.get({
	    url: googleBase,
	    json: true,
	    headers: {'User-Agent': 'request'}
	  }, (err, response, data) => {
	    if(err){
	    	console.log('Error:', err);
	    }else if(res.statusCode !== 200){
	    	console.log('Status:', response.statusCode);
	    }else{
	    	var myJson = {
	    		i : req.query.indexId,
		    	result : data
	    	}
	      	res.setHeader('content-type', 'application/json');
			res.json(myJson);
	    }
	});
})

app.get('/autoCompleteTicketMaster', function(req, res){

	console.log("You are near AutoComplete in server Side");
	console.log(req.query.keyword);

	var autoCompleteBase = "https://app.ticketmaster.com/discovery/v2/suggest?apikey=GfAAasFz3JZu04YljkCaq0nGuy4hz3cb&keyword=";
	autoCompleteBase += req.query.keyword;

	request.get({
	    url: autoCompleteBase,
	    json: true,
	    headers: {'User-Agent': 'request'}
	  },(err, response, data) =>{
	    if(err){

	    	console.log('Error:', err);
	    }else if(res.statusCode !== 200){

	    	console.log('Status:', response.statusCode);
	    }else{

	      	res.setHeader('content-type', 'application/json');
			res.json(data);
	    }
	});
})


var server = app.listen(8081, function () {
   var host = server.address().address;
   var port = server.address().port;
   
   console.log("app listening at port %s", port);
})