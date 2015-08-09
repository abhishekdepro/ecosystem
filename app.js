/******************************Primary API server******************************/
/**************************built with Restify + MongoJS driver*****************/
/*
* @author  : Abhishek Dey
* @version : 1.0.0.0
* @date    : 8 Aug 2015
*/

/********************Libraries and Requirements*********************/
var mongojs = require('mongojs');
var restify = require('restify');
var ip_addr = '127.0.0.1';
var port    =  process.env.PORT || 1337;
 
var server = restify.createServer({
    name : "Ecosquare API"
});
 
server.listen(port ,ip_addr, function(){
    console.log('%s listening at %s ', server.name , server.url);
});

server.use(restify.queryParser());
server.use(restify.bodyParser());
server.use(restify.CORS());

/*************************MongoDB Server****************************/
var connection_string = 'mongodb://207.46.227.159:27017/ecosquaredb';
var db = mongojs(connection_string, ['ecosquaredb']);
var user = db.collection("user");

/******************************Routes******************************/
var PATH = '/user'
server.get({path : PATH , version : '0.0.1'} , findAllJobs);
server.get({path : PATH +'/:userId' , version : '0.0.1'} , findJob);
server.post({path : PATH , version: '0.0.1'} ,postNewJob);
server.del({path : PATH +'/:userId' , version: '0.0.1'} ,deleteJob);

/******************************Functions******************************/
function findAllJobs(req, res , next){ //finds all users listed
    res.setHeader('Access-Control-Allow-Origin','*'); //header set for CORS request
    user.find().limit(20).sort({postedOn : -1} , function(err , success){ //limit of 20 users
        console.log('Response success '+success);
        console.log('Response error '+err);
        if(success){
            res.end(JSON.stringify(success,null,3)); //JSON response
            
        }else{
            return next(err);
        }
 
    });
 
}
 
function findJob(req, res , next){
    res.setHeader('Access-Control-Allow-Origin','*');
    user.findOne({_id:mongojs.ObjectId(req.params.jobId)} , function(err , success){
        console.log('Response success '+success);
        console.log('Response error '+err);
        if(success){
            res.send(200 , success);
            return next();
        }
        return next(err);
    })
}
 
function postNewJob(req , res , next){
    var _user = {};
    _user._id = req.params.id;
    _user.name = req.params.name;
    _user.email = req.params.email;
    _user.address = req.params.address;
    _user.joinDate = new Date();
 
    res.setHeader('Access-Control-Allow-Origin','*');
 
    user.save(_user , function(err , success){
        console.log('Response success '+success);
        console.log('Response error '+err);
        if(success){
            res.send(201 , user);
            return next();
        }else{
            return next(err);
        }
    });
}
 
function deleteJob(req , res , next){
    res.setHeader('Access-Control-Allow-Origin','*');
    user.remove({_id:mongojs.ObjectId(req.params.id)} , function(err , success){
        console.log('Response success '+success);
        console.log('Response error '+err);
        if(success){
            res.send(204);
            return next();      
        } else{
            return next(err);
        }
    })
 
}
