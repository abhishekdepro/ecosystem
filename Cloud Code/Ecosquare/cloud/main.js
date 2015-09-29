
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

function hasWhiteSpace(s) {
  return s.indexOf(' ') >= 0;
}




Parse.Cloud.afterSave(Parse.User, function(request) {

  var _id = request.object.get('username');
  if(hasWhiteSpace(request.object.get('Name'))){
    var _name = request.object.get('Name').split(' ').join('%20');
  }else{
    var _name = request.object.get('Name');
  }
  var _lat = request.object.get('Latitude');
  var _lon = request.object.get('Longitude');
  var _email = request.object.get('email');
  if(request.object.get('Code')!=""){
    var _code = request.object.get('Code');
  }else{
    var _code = undefined;
  }
  Parse.Cloud.httpRequest({

    url: "http://api.ecosquare.in/user?id="+_id+"&name="+_name+"&email="+_email+"&lat="+_lat+"&lon="+_lon+"&code="+_code,
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
