import sys
import nltk, string
from sklearn.feature_extraction.text import TfidfVectorizer

arg1 = ["online booking",
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
			result = cosine_sim(item, inp)
			# print result
			if result > 0:
				count = 1
				dictionary[item] = 1
			# else:
			# 	dictionary[item] = 0
				# print dictionary
	# print dictionary
	for key, value in dictionary.iteritems():
		# print key
		if value == 1:
			# print key
			recommended.append(key)
	print list(set(arg2) - set(recommended))
sys.stdout.flush()

run_for_all_inputs(arg1, arg2)
