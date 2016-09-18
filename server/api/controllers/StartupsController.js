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
				getTweets(market[0].companies)
				// var options = {
				//   args: [market[0].companies, add_features, market]
				// };
				// PythonShell.run('../python/main.py', options, function (err, results) {
				//   if (err) throw err;
				// 	results = results[0];
				// 	var recommended = results.slice(0);
				// 	recommended = results.slice(1, recommended.length-1)
				// 	var reply = {
				// 		'status': 1,
				// 		'results': recommended
				// 	}
				// 	res.status(200).json(reply)
				// });
			}
		})

		function getTweets(companies){
			var Twitter = require('twitter-node-client').Twitter;
			var config = {
					"consumerKey": "fmBYOhNym9vyQFMJnPdrhFDZn",
					"consumerSecret": "QLV3AGB1PFEhP3xVYcg8DUvwlLanGtnc6odpU0Yx7kgFJLAFfe",
					"accessToken": "2485459615-7UGNexY55OjxLOnrPvmsBl4gYOx9P5Yf5UoRwj4",
					"accessTokenSecret": "igEDdbxBpluYvFxRoe71GOyTP2db5aEHeT8XbiRL1OC3I"
			}
			var twitter = new Twitter(config);
			var badTweetCount = [], index = 0, sum = 0, mean = 0, companiesWithPoorReviews = 0, suckingComs = 0;

			_.each(companies, function(company){
				var name = company.name;
				name = name.toLowerCase();
				var nameWithoutSpaces = name.replace(/ /g, '');
				twitter.getSearch({'q':''+ name +' OR '+ nameWithoutSpaces +' :(','count': '1000'}, function(err){
					console.log(err);
				}, function(resp){
					var tweets = JSON.parse(resp);
					tweets = tweets.statuses;
					if(tweets.length > 0){
						companiesWithPoorReviews++;
					}
					tweets = _.reject(tweets, function(tweet){
						var retVal = false;
						var date = new Date(tweet.created_at)
						if(date.getFullYear() < 2016){
							retVal = true;
						}
						return retVal;
					})
					var percent = tweets.length;
					percent = percent/1000;
					sum = sum + percent;
				});
				index++;
				if(index >= companies.length){
					mean = sum/companies.length;
					suckingComs = companiesWithPoorReviews/companies.length;
				}
			})
		}
	}
};
