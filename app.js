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
var connection_string = 'mongodb://example.com';
var db = mongojs(connection_string, ['db_name']);
var user = db.collection("user");
var transactions = db.collection("transaction");
var employees = db.collection("emp");
var referrals = db.collection("referral");

/******************************Routes******************************/

server.get({path  : '/test' , version : '0.0.1'} , test);


//==================================================================//
//USER Routes
var USER_PATH = '/user'
server.get({path  : USER_PATH +'/key/:token', version : '0.0.1'} , findAllUsers);
server.get({path  : USER_PATH +'/:userId' , version: '0.0.1'} ,findUserbyID);
server.get({path  : USER_PATH +'/address/:UserAddress' , version: '0.0.1'} ,findUserbyAddress);
server.post({path : USER_PATH , version: '0.0.1'} ,createUser);
server.post({path  : USER_PATH +'/update' , version: '0.0.1'} , updateUser);
server.del({path  : USER_PATH +'/delete/:userId' , version: '0.0.1'} ,deleteUser);
//==================================================================//
//TRANSACTION Routes
var TRANSACTION_PATH = '/transaction'
server.get({path  : TRANSACTION_PATH , version : '0.0.1'} , findAllTransactions);
server.get({path  : TRANSACTION_PATH +'/:userId' , version: '0.0.1'} ,findUserbyID);
server.get({path  : TRANSACTION_PATH +'/address/:UserAddress' , version: '0.0.1'} ,findUserbyAddress);
server.post({path : TRANSACTION_PATH , version: '0.0.1'} , onTransactionStart);
server.put({path  : TRANSACTION_PATH +'/update/:emp_id/:t_id' , version: '0.0.1'} , onTransactionEnd);
server.del({path  : TRANSACTION_PATH +'/delete/:userId' , version: '0.0.1'} ,deleteUser);
//==================================================================//
//EMPLOYEE Routes
var EMP_PATH = '/emp'
server.get({path  : EMP_PATH , version : '0.0.1'} , findAllEmployees);
server.get({path  : EMP_PATH + '/location', version : '0.0.1'} , getEmployeesbyLocation);
server.get({path  : EMP_PATH +'/:userId' , version: '0.0.1'} ,findEmployeebyID);
server.post({path : EMP_PATH , version: '0.0.1'} , createEmployee);
server.put({path  : EMP_PATH +'/location/update/:emp_id/:lat/:lon' , version: '0.0.1'} , onEmployeeLocationChanged);
//server.del({path  : EMP_PATH +'/delete/:userId' , version: '0.0.1'} ,deleteEmployee);
//==================================================================//

/*****************************Functions****************************/

//==================================================================//
//generate token for auth and secure hashing
function generateToken(strength){
  require('crypto').randomBytes(strength, function(ex, buf) {
    token = buf.toString('hex');
  });
}
function generateRef(strength){
  require('crypto').randomBytes(strength, function(ex, buf) {
    ref = buf.toString('hex');
  });
}

function getHome(req,res){
  res.end('/Views/index.html')
}

//==================================================================//
//Start User CRUD operations
function findAllUsers(req, res , next){ //finds all users listed
    res.setHeader('Access-Control-Allow-Origin','*'); //header set for CORS request
    if(req.params.token=="abhishekdepro"){
        user.find().limit(20).sort({postedOn : -1} , function(err , success){ //limit of 20 users
        //console.log(_token);
        console.log('Response error '+err);
        if(success){
            res.end(JSON.stringify(success,null,3)); //JSON response
        }else{
            return next(err);
        }

        });
    }else{
    user.findOne({token:req.params.token}, function(err, success){
        if(success){
            user.find().limit(20).sort({postedOn : -1} , function(err , success){ //limit of 20 users
        //console.log(_token);
        console.log('Response error '+err);
        if(success){
            res.end(JSON.stringify(success,null,3)); //JSON response
        }else{
            return next(err);
        }

        });
        }else{
            res.end(err);
        }
    });
    }

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

function test(req,res){
  var _token = await(generateToken(3));
  console.log(_token);
}

function createUser(req , res , next){
    var _user = {};
    require('crypto').randomBytes(8, function(ex, buf) {
      token = buf.toString('hex');
    });
    require('crypto').randomBytes(3, function(ex, buf) {
      ref = buf.toString('hex');
    });
    _user._id = req.params.id;
    _user.name = req.params.name;
    _user.email = req.params.email;
    _user.lat = req.params.lat;
    _user.lon = req.params.lon;
    _user.token = token;
    _user.ref = ref;
    if(typeof req.params.code != 'undefined'){
      _user.code=req.params.code;
      var _refadd = {};
      
      user.findOne({ref:req.params.code}, function(err, success){
          if(success){
              _refadd.from = success._id;
              _refadd.to = req.params.id;
              _refadd.code = req.params.code;
              referrals.save(_refadd,function(error,success){
                  if(success){
                    res.end(JSON.stringify(success,null,3));
                  }else{
                    res.end(error);
                  }
              });
          }else{
            res.end(err);
          }
      });
      
      
    }else{
      _user.code="N/A";
    }
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

function updateUser(req,res,next){
  var obj = req.body;
  console.log(obj);
  var query = {
    _id : obj.id
  }
  var change = {
    $set : { name : obj.name, email: obj.email}
  }
  user.update(query, change, function(err, success){
      if(err){
        return next(err);
      }else{
        res.end(JSON.stringify(success,null,3));
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
  var obj=req.body;
  generateToken(8);
  transaction._id = token;
  transaction.u_id = obj.u_id;
  transaction.emp_id = obj.emp_id;
  transaction.lat = obj.lat;
  transaction.lon = obj.lon;
  transaction.quantity = quantity;
  transaction.quantity.paper = obj.paper;
  transaction.quantity.plastic = obj.plastic;
  transaction.mode = obj.mode;
  transaction.date = new Date();
  transaction.status = obj.status;
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

function onTransactionEnd(req,res,next){
  var query = {
    _id : req.params.emp_id
  }
  var change = {
    $push : {transactions:req.params.t_id}
  }
  employees.update(query, change, function(err, success){
      if(err){
        return next(err);
      }else{
        res.end(JSON.stringify(success,null,3));
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

//end TRANSACTION CRUD operations
//==================================================================//
//EMPLOYEE CRUD operations
function createEmployee(req, res, next){
  var emp = {};
  var transactions = [];
  var obj=req.body;
  generateToken(16);
  emp._id = obj.id;
  emp.name = obj.name;
  emp.lat = obj.lat;
  emp.lon = obj.lon;
  emp.transactions = transactions;
  emp.photo = "http://api.randomuser.me/portraits/thumb/women/12.jpg";

  employees.save(emp , function(err , success){
      if(success){
          res.send(201 , transaction);
          return next();
      }else{
          return next(err);
      }
  });
}

function findEmployeebyID(req,res){
    employees.findOne({_id:req.params.userId}, function(err, success){
        if(success){
            res.end(JSON.stringify(success,null,3));
        }else{
            res.end(err);
        }
    });
}

function onEmployeeLocationChanged(req, res, next){
    var query = {
      _id : req.params.emp_id
    }
    var change = {
      $set : { lat:req.params.lat , lon:req.params.lon }
    }
    employees.update(query, change, function(err, success){
        if(err){
          return next(err);
        }else{
          res.end(JSON.stringify(success,null,3));
        }
    });
}

function getEmployeesbyLocation(req, res, next){
  employees.find({_id : {$ne : null}},{lat:1,lon:1}).limit(20).sort({postedOn : -1} , function(err , success){

      console.log('Response error '+err);
      if(success){
          res.end(JSON.stringify(success,null,3)); //JSON response
      }else{
          return next(err);
      }

  });
}

function findAllEmployees(req, res , next){ //finds all users listed
    res.setHeader('Access-Control-Allow-Origin','*'); //header set for CORS request
    employees.find().limit(20).sort({postedOn : -1} , function(err , success){
        //console.log(_token);
        console.log('Response error '+err);
        if(success){
            res.end(JSON.stringify(success,null,3)); //JSON response
        }else{
            return next(err);
        }

    });

}