import pandas as pd
import firebase_admin
import os
from firebase_admin import credentials, firestore
def run():
    # # Use a service account
    data = os.path.abspath(os.path.dirname(__file__)) + "/serviceAccountKey.json"
    print(data)
    cred = credentials.Certificate(data)
    firebase_admin.initialize_app(cred, 
    {
    "databaseURL": "https://quiz-77b7a.firebaseio.com"
    })
    db = firestore.client()
    # for k in db.collection("Quizzes").get():
    #     print(k.id, k.to_dict())

    # quizpath = "Student\'s quizzes"
    quizpath = "Quizzes"
    # col_ref = db.collection('Quizzes') # col_ref is CollectionReference
    col_ref = db.collection(quizpath) # col_ref is CollectionReference
    results = col_ref.where('quizId', '==', 'maths').get() # one way to query
    # results = col_ref.where('quizId', '==', 'mathsstr').get() # one way to query
    # results = col_ref.order_by('date',direction='DESCENDING').limit(1).get() # another way - get the last document by date

    # questionsForQuiz = {
#     "question": "what is 2+2?",
#     "option1": "2",
#     "option2": "3",
#     "option3": "4",
#     "option4": "5",
#     "correct_answer": "2"
# }
    # fullset = [{'option1': '7', 'option2': '3', 'correct_answer': '7', 'questionID': '', 'question': 'what is 5+2?', 'option4': '5', 'option3': '4'}, {'option2': '3', 'option3': '4', 'correct_answer': '4', 'questionID': '', 'option4': '5', 'question': 'what is 2+2?', 'option1': '2'}]

    fullset = [{'options':['7',  '3', '4'], 'question': 'what is 5+2?', 'correct_answer': '7'},
     {'question': 'what is 2+2?', 'options':['2', '3', '4'], 'correct_answer': '4'},
      {'question': 'what is 3+2?', 'options':['2', '3', '4', '5'], 'correct_answer': '5'},
       {'question': 'what is 4+2?', 'options':['2', '3', '4','5','6'], 'correct_answer': '6'},
        {'question': 'what is 5+2?', 'options':['7','5', '3', '4'], 'correct_answer': '7'},
         {'question': 'what is 6+2?', 'options':['8', '3', '4'], 'correct_answer': '8'},
          {'question': 'what is 7+2?', 'options':['2', '3', '4', '9'], 'correct_answer': '9'},
           {'question': 'what is 8+2?', 'options':['2', '3', '4','10'], 'correct_answer': '10'},
            {'question': 'what is 9+2?', 'options':['2', '3', '4','11','12'], 'correct_answer': '11'},
             {'question': 'what is 10+2?', 'options':['2', '3', '4','13','14','12'], 'correct_answer': '12'}]

    fullset2 = [{'options':[''], 'question': 'what is 5+2?', 'correct_answer': '7'},
     {'question': 'what is 12+2?', 'options':[''], 'correct_answer': '14'},
      {'question': 'what is 13+2?', 'options':[''], 'correct_answer': '15'},
       {'question': 'what is 14+2?', 'options':[''], 'correct_answer': '16'},
        {'question': 'what is 15+2?', 'options':[''], 'correct_answer': '17'},
         {'question': 'what is 16+2?', 'options':[''], 'correct_answer': '18'},
          {'question': 'what is 17+2?', 'options':[''], 'correct_answer': '19'},
           {'question': 'what is 18+2?', 'options':[''], 'correct_answer': '20'},
            {'question': 'what is 19+2?', 'options':[''], 'correct_answer': '21'},
             {'question': 'what is 110+2?', 'options':[''], 'correct_answer': '112'}]
              
    # , 'questionID': '', 'option4': '5', 'question': 'what is 2+2?', 'option1': '2'}, {'option1': '2', 'question': 'what is 3+2?', 'questionID': '', 'option4': '5', 'correct_answer': '5', 'option3': '4', 'option2': '3'}, {'correct_answer': '3', 'option3': '4', 'option2': '3', 'questionID': '', 'question': 'what is 1+2?', 'option4': '5', 'option1': '2'}, {'questionID': '', 'option4': '5', 'option3': '14', 'option1': '2', 'correct_answer': '14', 'question': 'what is 12+2?', 'option2': '13'}, {'option1': '22', 'correct_answer': '24', 'question': 'what is 12+12?', 'option2': '33', 'questionID': '', 'option4': '25', 'option3': '24'}, {'option4': '5', 'option3': '9', 'option1': '10', 'option2': '11', 'correct_answer': '10', 'questionID': '', 'question': 'what is 12-2?'}, {'question': 'what is 4+4?', 'option4': '5', 'option2': '7', 'correct_answer': '8', 'option3': '8', 'option1': '6', 'questionID': ''}, {'option4': '10', 'option2': '8', 'option3': '9', 'correct_answer': '9', 'question': 'what is 4+5?', 'option1': '6', 'questionID': ''}, {'option1': '19', 'option2': '18', 'questionID': '', 'correct_answer': '20', 'question': 'what is 20+20-10-10?', 'option4': '20', 'option3': '21'}]
    for item in results:
        print(item.id)
        # print(item.questionsForQuiz.to_dict())
        doc = col_ref.document(item.id)
        arr = doc.get().get('quizQuestionList')

        # importing the data
        # for i in range(10):
        # arr.append(questionsForQuiz)
        # field_updates = {"questionsForQuiz": arr}
        # doc.update(field_updates)

        ## importing fullset data
        # 
        for i in fullset:
            arr.append(i)
        field_updates = {"quizQuestionList": arr}
        doc.update(field_updates)

        # exporting data
        print(arr)


if __name__ == "__main__":
    run()
