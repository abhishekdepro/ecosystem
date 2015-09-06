
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

Parse.Cloud.afterSave(Parse.User, function(request) {
  var _id = request.object.get('username');
  var _name = request.object.get('Name');
  var _lat = request.object.get('Latitude');
  var _lon = request.object.get('Longitude');
  var _email = request.object.get('email');
  Parse.Cloud.httpRequest({

    url: "http://ecosquare.herokuapp.com/user?id="+_id+"&name="+_name+"&email="+_email+"&address=hbtown&lat="+_lat+"&lon="+_lon,
    method: 'POST',
    headers:{
        'Content-Type': 'application/json'
    },
    success: function (httpResponse) {

        console.log(httpResponse.text);
        response.success(httpResponse);
    },
    error:function (httpResponse) {
        console.error('Request failed with response code ' + httpResponse.status);

        response.error(httpResponse.status);
    }

});
});
