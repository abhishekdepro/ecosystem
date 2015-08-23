/******************************Primary API server******************************/
/**************************built with Restify + MongoJS driver*****************/
/*
* @author  : Abhishek Dey
* @version : 1.0.0.0
* @date    : 8 Aug 2015
* @legend  : {
* //=====//: denotes start & end of functions/modules
* /*****'/': denotes difference in sub-modules or functional requirements
* }
*/

/********************Libraries and Requirements*********************/
var mongojs = require('mongojs');
var restify = require('restify');
var ip_addr = '127.0.0.1';
var port    =  process.env.PORT || 1337;
var token;
var server = restify.createServer({
    name : "Ecosquare API"
});

server.listen(port , function(){
    console.log('started %s', server.name);
});

server.use(restify.queryParser());
server.use(restify.bodyParser());
server.use(restify.CORS());

/*************************MongoDB Server****************************/
var connection_string = 'mongodb://example.com/21707';
var db = mongojs(connection_string, ['db_name']);
var user = db.collection("user");
var transactions = db.collection("transaction");

/******************************Routes******************************/

//==================================================================//
//USER Routes
var USER_PATH = '/user'
server.get({path  : USER_PATH , version : '0.0.1'} , findAllUsers);
server.get({path  : USER_PATH +'/:userId' , version: '0.0.1'} ,findUserbyID);
server.get({path  : USER_PATH +'/address/:UserAddress' , version: '0.0.1'} ,findUserbyAddress);
server.post({path : USER_PATH , version: '0.0.1'} ,createUser);
server.del({path  : USER_PATH +'/delete/:userId' , version: '0.0.1'} ,deleteUser);
//==================================================================//
//TRANSACTION Routes
var TRANSACTION_PATH = '/transaction'
server.get({path  : TRANSACTION_PATH , version : '0.0.1'} , findAllTransactions);
server.get({path  : TRANSACTION_PATH +'/:userId' , version: '0.0.1'} ,findUserbyID);
server.get({path  : TRANSACTION_PATH +'/address/:UserAddress' , version: '0.0.1'} ,findUserbyAddress);
server.post({path : TRANSACTION_PATH , version: '0.0.1'} , onTransactionStart);
server.del({path  : TRANSACTION_PATH +'/delete/:userId' , version: '0.0.1'} ,deleteUser);
//==================================================================//

/*****************************Functions****************************/

//==================================================================//
//generate token for auth and secure hashing
function generateToken(strength){
  require('crypto').randomBytes(strength, function(ex, buf) {
    token = buf.toString('hex');
    console.log(token);
  });
}
//==================================================================//
//Start User CRUD operations
function findAllUsers(req, res , next){ //finds all users listed
    res.setHeader('Access-Control-Allow-Origin','*'); //header set for CORS request
    user.find().limit(20).sort({postedOn : -1} , function(err , success){ //limit of 20 users
        //console.log(_token);
        console.log('Response error '+err);
        if(success){
            res.end(JSON.stringify(success,null,3)); //JSON response
        }else{
            return next(err);
        }

    });

}

function findUserbyID(req,res){
    user.findOne({_id:req.params.userId}, function(err, success){
        if(success){
            res.end(JSON.stringify(success,null,3));
        }else{
            res.end(err);
        }
    });
}

function findUserbyAddress(req,res){
    user.findOne({address:req.params.UserAddress}, function(err, success){
        if(success){
            res.end(JSON.stringify(success,null,3));
        }else{
            res.end(err);
        }
    });
}

function createUser(req , res , next){
    var _user = {};
    generateToken(16);
    console.log(token);
    _user._id = req.params.id;
    _user.name = req.params.name;
    _user.email = req.params.email;
    _user.address = req.params.address;
    _user.lat = req.params.lat;
    _user.lon = req.params.lon;
    _user.token = token;
    _user.joinDate = new Date();

    res.setHeader('Access-Control-Allow-Origin','*');

    user.save(_user , function(err , success){
        console.log('Response success '+success);
        console.log('Response error '+err);
        if(success){
            res.send(201 , _user);
            return next();
        }else{
            return next(err);
        }
    });
}

function deleteUser(req , res , next){
    res.setHeader('Access-Control-Allow-Origin','*');
    user.remove({_id:req.params.userId} , function(err , success){
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
//end User CRUD operations
//==================================================================//
//Transaction CRUD operations

function onTransactionStart(req, res, next){
  var transaction = {};
  var quantity = {};
  generateToken(8);
  console.log(token);
  transaction._id = token;
  transaction.u_id = req.params.u_id;
  transaction.emp_id = req.params.emp_id;
  transaction.lat = req.params.lat;
  transaction.lon = req.params.lon;
  transaction.quantity = quantity;
  transaction.quantity.paper = req.params.paper;
  transaction.quantity.plastic = req.params.plastic;
  transaction.mode = req.params.mode;
  transaction.date = new Date();
  transaction.status = req.params.status;

  res.setHeader('Access-Control-Allow-Origin','*');

  transactions.save(transaction , function(err , success){
      console.log('Response success '+success);
      console.log('Response error '+err);
      if(success){
          res.send(201 , transaction);
          return next();
      }else{
          return next(err);
      }
  });
}

function findAllTransactions(req, res , next){ //finds all users listed
    res.setHeader('Access-Control-Allow-Origin','*'); //header set for CORS request
    transactions.find().limit(20).sort({postedOn : -1} , function(err , success){
        //console.log(_token);
        console.log('Response error '+err);
        if(success){
            res.end(JSON.stringify(success,null,3)); //JSON response
        }else{
            return next(err);
        }

    });

}
