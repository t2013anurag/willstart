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
		var add_features = req.param('services');
		var market = req.param('market');
		var PythonShell = require('python-shell');

		Markets.find({'category':market}).exec(function foundMarket(err, market){
			if(err || !market){
				var reply = {
					'status': 0,
					'message': 'Server faced an error'
				}
				res.status(200).json(reply)
			}
			else{
				var options = {
				  args: [market[0].companies, add_features, market]
				};
				PythonShell.run('../python/main.py', options, function (err, results) {
				  if (err) throw err;
					var reply = {
						'status': 1,
						'results': results[0]
					}
					res.status(200).json(reply)
				});
			}
		})
	}
};
