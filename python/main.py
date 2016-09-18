import sys
import nltk, string
from sklearn.feature_extraction.text import TfidfVectorizer

arg1 = []

arg_1 = ["online booking",
     	"app",
     	"home delivery",
     	"menu handpicking",
      	"meal categorization",
      	"provides coupons",
      	"restaurant options",
      	"sells raw food",
      	"cultural cuisine",
      	"juice only",
      	"sells booze",
      	"sells fast food",
      	"sells chocolates",
      	"analytics platform",
      	"tailored meals",
      	"provide nutrition content",
      	"gift dining",
      	"night deliveries"]

arg_2 = ["Online Prescription",
      	"Specialist",
      	"Real Time Suggestions",
      	"Q & A",
      	"Risk Analysis and Report",
      	"Book Appointments",
      	"CRM",
      	"Fitness Tracker",
      	"Coaches",
      	"Meal Tracker",
      	"Home Delivery",
      	"Sells Medicines",
      	"Sexual Tests",
      	"Tailored Meals",
      	"Beauty Tips and Wellness"]

market = sys.argv[3]
if market == "food":
	arg1 = arg_1
else:
	arg1 = arg_2

temp = sys.argv[2]
arg2 = temp.split(",")
# print arg2

stemmer = nltk.stem.porter.PorterStemmer()
remove_punctuation_map = dict((ord(char), None) for char in string.punctuation)

def stem_tokens(tokens):
    return [stemmer.stem(item) for item in tokens]

def normalize(text):
    return stem_tokens(nltk.word_tokenize(text.lower().translate(remove_punctuation_map)))

vectorizer = TfidfVectorizer(min_df=1)

def cosine_sim(text1, text2):
	tfidf = vectorizer.fit_transform([text1, text2])
	return ((tfidf * tfidf.T).A)[0,1]


def run_for_all_inputs(arg1, arg2):
	dictionary = {}
	recommended = []
	count = 0
	result = 0

	for key, value in dictionary.iteritems():
		dictionary[key] = 0

	for item in arg2:
		for inp in arg1:
			result = cosine_sim(item, inp.lower())
			if result > 0:
				count = 1
				dictionary[item] = 1
	
	for key, value in dictionary.iteritems():
		if value == 1:
			recommended.append(key)
	
	print list(set(arg2) - set(recommended))
	score = list(set(arg2) - set(recommended))
	# print score

	l = len(score)
	p = len(arg1)
	q = l/float(p)
	print round(q,4)*100
sys.stdout.flush()

run_for_all_inputs(arg1, arg2)
