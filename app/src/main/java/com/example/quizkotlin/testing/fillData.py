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
    "databaseURL": "https://kotlinquiz-b000d.firebaseio.com"
    })
    db = firestore.client()
    # for k in db.collection("Quizzes").get():
    #     print(k.id, k.to_dict())

    col_ref = db.collection('Quizzes') # col_ref is CollectionReference
    results = col_ref.where('id', '==', 'Tester').get() # one way to query
    # results = col_ref.order_by('date',direction='DESCENDING').limit(1).get() # another way - get the last document by date

    questionsForQuiz = {
    "question": "What is 2+2?",
    "option1": "2",
    "option2": "3",
    "option3": "4",
    "option4": "5",
    "correct_answer": "2"
}
    # fullset = [{'option1': '7', 'option2': '3', 'correct_answer': '7', 'questionID': '', 'question': 'What is 5+2?', 'option4': '5', 'option3': '4'}, {'option2': '3', 'option3': '4', 'correct_answer': '4', 'questionID': '', 'option4': '5', 'question': 'What is 2+2?', 'option1': '2'}]

    fullset = [{'option1': '7', 'option2': '3', 'correct_answer': '7', 'questionID': '', 'question': 'What is 5+2?', 'option4': '5', 'option3': '4'}, {'option2': '3', 'option3': '4', 'correct_answer': '4', 'questionID': '', 'option4': '5', 'question': 'What is 2+2?', 'option1': '2'}, {'option1': '2', 'question': 'What is 3+2?', 'questionID': '', 'option4': '5', 'correct_answer': '5', 'option3': '4', 'option2': '3'}, {'correct_answer': '3', 'option3': '4', 'option2': '3', 'questionID': '', 'question': 'What is 1+2?', 'option4': '5', 'option1': '2'}, {'questionID': '', 'option4': '5', 'option3': '14', 'option1': '2', 'correct_answer': '14', 'question': 'What is 12+2?', 'option2': '13'}, {'option1': '22', 'correct_answer': '24', 'question': 'What is 12+12?', 'option2': '33', 'questionID': '', 'option4': '25', 'option3': '24'}, {'option4': '5', 'option3': '9', 'option1': '10', 'option2': '11', 'correct_answer': '10', 'questionID': '', 'question': 'What is 12-2?'}, {'question': 'What is 4+4?', 'option4': '5', 'option2': '7', 'correct_answer': '8', 'option3': '8', 'option1': '6', 'questionID': ''}, {'option4': '10', 'option2': '8', 'option3': '9', 'correct_answer': '9', 'question': 'What is 4+5?', 'option1': '6', 'questionID': ''}, {'option1': '19', 'option2': '18', 'questionID': '', 'correct_answer': '20', 'question': 'What is 20+20-10-10?', 'option4': '20', 'option3': '21'}]
    for item in results:
        print(item.id)
        # print(item.questionsForQuiz.to_dict())
        doc = col_ref.document(item.id)
        arr = doc.get().get('questionsForQuiz')

        # importing the data
        # for i in range(10):
        # arr.append(questionsForQuiz)
        # field_updates = {"questionsForQuiz": arr}
        # doc.update(field_updates)

        ## importing fullset data
        # 
        for i in fullset:
            arr.append(i)
        field_updates = {"questionsForQuiz": arr}
        doc.update(field_updates)

        # exporting data
        # print(arr.to_dict())


if __name__ == "__main__":
    run()
