/**
 * StartupsController
 *
 * @description :: Server-side logic for managing startups
 * @help        :: See http://sailsjs.org/#!/documentation/concepts/Controllers
 */

module.exports = {
	'index': function(req,res){
		if(req.param('market')){
			var market = req.param('market');
			Markets.find({'category':market}).exec(function foundMarket(err, market){
				if(err || !market){
					var reply = {
						'status': 0,
						'message': 'Server faced an error'
					}
					res.status(200).json(reply)
				}
				else{
					var reply = {
						'status': 1,
						'message': 'All market information has been fetched',
						'market': market
					}
					res.status(200).json(reply)
				}
			})
		}
		else{
			var reply = {
				'status': 0,
				'message': 'All parameters not passed'
			}
			res.status(200).json(reply)
		}
	},
	'features':function(req,res){
		var add_features = req.param('add_features');
		var market = req.param('market');
		// var spawn = require("child_process").spawn;
		// var process = spawn('python',["../python/test.py"]);

		// var sys   = require('sys'),
		//     exec  = require('child_process').execFile('../python/main.py', function (error, stdout, stderr) {
		// 	    sys.print('stdout: ' + stdout);
		// 	    sys.print('stderr: ' + stderr);
		// 	    if (error !== null) {
		// 	      console.log('exec error: ' + error);
		// 	    }
		// 	});

		var PythonShell = require('python-shell');

		var options = {
		  mode: 'text',
		  pythonPath: 'path/to/python',
		  pythonOptions: ['-u'],
		  scriptPath: 'path/to/my/scripts',
		  args: ['value1', 'value2', 'value3']
		};

		PythonShell.run('../python/main.py', function (err, results) {
		  if (err) throw err;
		  // results is an array consisting of messages collected during execution
		  console.log('results: %j', results);
		});

		// child = exec('../python/main.py',
		//   function (error, stdout, stderr) {
		//     sys.print('stdout: ' + stdout);
		//     sys.print('stderr: ' + stderr);
		//     if (error !== null) {
		//       console.log('exec error: ' + error);
		//     }
		// });

		// var fs = require('fs');
		// fs.stat('../python/main.py', function(err, stat) {
	  //   if(err == null) {
	  //       console.log('File exists');
	  //   } else if(err.code == 'ENOENT') {
	  //       // file does not exist
		// 			console.log('no');
	  //   } else {
	  //       console.log('Some other error: ', err.code);
	  //   }
		// });
		// Markets.find({'category':market}).exec(function foundMarket(err, market){
		// 	if(err || !market){
		// 		var reply = {
		// 			'status': 0,
		// 			'message': 'Server faced an error'
		// 		}
		// 		res.status(200).json(reply);
		// 	}
		// 	else{
		// 		// getFeatures(market[0].companies)
		// 	}
		// })

		// process.stdout.on('data', function (data){
		// 	console.log('fvfv f');
		// 	console.log(data);
		// 	console.log('coming');
		// });
	}
};
