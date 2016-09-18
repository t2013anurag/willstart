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
					// results = results[0];
					// var recommended = results.slice(0);
					// recommended = results.slice(1, recommended.length-1)

					getTweets(market[0].companies, results)
				});
			}
		})

		function getTweets(companies, results){
			var Twitter = require('twitter-node-client').Twitter;
			// var config = {
			// 		"consumerKey": "fmBYOhNym9vyQFMJnPdrhFDZn",
			// 		"consumerSecret": "QLV3AGB1PFEhP3xVYcg8DUvwlLanGtnc6odpU0Yx7kgFJLAFfe",
			// 		"accessToken": "2485459615-7UGNexY55OjxLOnrPvmsBl4gYOx9P5Yf5UoRwj4",
			// 		"accessTokenSecret": "igEDdbxBpluYvFxRoe71GOyTP2db5aEHeT8XbiRL1OC3I"
			// }

			var config = {
					"consumerKey": "CgfxMqnwY09eUQrw2BiZs8DD2",
					"consumerSecret": "8bJuonc26RUnwTYK03JDGURIUoTqnZSkXTssPbI7bIbJTCOXYT",
					"accessToken": "2336943036-MFCe46P79Hxh84u1dVLZoJvMk4IyNbVKSVx53I2",
					"accessTokenSecret": "y0bz4zxZBlZzTSmM1qhCvM7DmyaSzBOQueMs5O9Qibhp1"
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
					index++;
					if(index >= companies.length){
						mean = sum/companiesWithPoorReviews;
						suckingComs = companiesWithPoorReviews/companies.length;
						mean = mean*100;
						mean = mean.toFixed(2);
						suckingComs = suckingComs*100;
						suckingComs = suckingComs.toFixed(2);
						var reply = {
							'status' : 1,
							'message' : 'An error occured',
							'results': results,
							'mean': mean,
							'poorPercentage': suckingComs
						}
						res.status(200).json(reply);
					}
				});
			})
			// console.log(results)

		}
	},

	'score': function(req, res){
		var market = req.param('market');
		var user_features = JSON.parse(req.param('services'));

		console.log(user_features)
		// console.log(market);
		Markets.find({'category': market}).exec(function foundMarket(err, markets){
			if(err || !markets){
				var reply = {
					'status' : 100,
					'message' : 'An error occured'
				}
				res.status(200).json(reply);
			} else {
				company = markets[0].companies

				var map = {}

				if(market.toString() === "food") {
					map = {"online booking" : 0,
				     	"app": 0,
				     	"home delivery" : 0,
				     	"menu handpicking": 0,
				      	"meal categorization": 0,
				      	"provides coupons": 0,
				      	"restaurant options": 0,
				      	"sells raw food": 0,
				      	"cultural cuisine": 0,
				      	"juice only": 0,
				      	"sells booze": 0,
				      	"sells fast food": 0,
				      	"sells chocolates": 0,
				      	"analytics platform": 0,
				      	"tailored meals": 0,
				      	"provide nutrition content": 0,
				      	"gift dining": 0,
				      	"night deliveries": 0,
				      	"home cooked": 0}
				}
				else {
					map = {"Online Prescription": 0,
				      	"Specialist": 0,
				      	"Real Time Suggestions": 0,
				      	"Q & A": 0,
				      	"Risk Analysis and Report": 0,
				      	"Book Appointments": 0,
				      	"CRM": 0,
				      	"Fitness Tracker": 0,
				      	"Coaches": 0,
				      	"Meal Tracker": 0,
				      	"Home Delivery": 0,
				      	"Sells Medicines": 0,
				      	"Sexual Tests": 0,
				      	"Tailored Meals": 0,
				      	"Beauty Tips and Wellness": 0}
				}

				all_items = []

				_.each(company, function(companies) {
					k = companies["services_available"]
					_.each(k, function(item){
						map[item] += 1
					})
				})

				for(var i in map) {
					all_items.push(i)
				}
				var sum = 0, count = 0

				for(var i in map) {
					sum += map[i]
					count  += 1
				}

				var mean = 0
				mean = sum / count
				var recommendations = []
				var min_threshold = mean
				for(var i in map) {
					if(map[i] <= min_threshold)
						recommendations.push(i)
				}
				var l = recommendations.length
				var rem_items = []
				rem_items = _.difference(all_items, recommendations)
				var alpha = Math.random()*(all_items.length - min_threshold) + min_threshold;				
				for(var i in map){
					if(map[i] > alpha)
						recommendations.push(i)
				}
				var currentIndex = recommendations.length, temporaryValue, randomIndex;
				while (0 !== currentIndex) {
					randomIndex = Math.floor(Math.random() * currentIndex);
				    currentIndex -= 1;
				    temporaryValue = recommendations[currentIndex];
				    recommendations[currentIndex] = recommendations[randomIndex];
				    recommendations[randomIndex] = temporaryValue;
				  }
				p1 = []
				p1 = _.difference(recommendations, user_features)
				// console.log(recommendations)
				// console.log(user_features)
				// console.log(p1)
				var reply = {
					'status' : 108,
					'result' : p1
				}
				// console.log(reply)
				res.status(200).json(reply)
			}
		})
	}
};
