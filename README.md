Questionnaire Service
===

This is a rest service that allows getting and answering a questionnaire. You can configure the questionnaire using a yaml file.
The service exposes two differents endpoints:

* GET - /questionnaire/get - Get a questionnaire in a json structure.
* POST - /questionnaire/answer - Answer a questionnaire using json structure.

GET Questionnaire
===
This is a sample example on how can get the questionnaire using a curl:

```curl
curl -X GET \
  http://localhost:8080/questionnaire/get \
  -H 'cache-control: no-cache'
```

POST Questionnaire
===
This is a sample example on how you can answer the questionnaire using a curl.
Note: You must use the following json structure retrieved from /questionnaire/get and the questionnaire only allows one answer per question.

```curl
curl -X POST \
  http://localhost:8080/questionnaire/answer \
  -H 'Content-Type: application/json' \
  -H 'cache-control: no-cache' \
  -d '{
    "questions": [
        {
            "questionId": "e3d2cf03-f934-4d72-9c24-2563d4451bfc",
            "question": "Alternative transportation, as using the train or bike instead of a personal car, can help save energy.",
            "answers": [
                "True"
            ]
        },
        {
            "questionId": "1c98a9fe-b1dc-4e67-8610-333c0d41c903",
            "question": "Chickens are one of the biggest producers of greenhouse gases in the world, which means that changing our diet can decrease carbon footprint.",
            "answers": [
                "True"
            ]
        },
        {
            "questionId": "e26c6fe1-727c-4455-a3bf-175c59140ab1",
            "question": "A sustainable diet may include:",
            "answers": [
                "Biological vegetables"
            ]
        },
        {
            "questionId": "550711a7-010e-47ae-b281-3e279d2fd640",
            "question": "Simple measures as decreasing the use of plastics for food packaging contribute to the implementation of eco-friendly actions.",
            "answers": [
                "True"
            ]
        }
    ]
}'
```

Running Service
===
You can run the service using docker. You only need to run the following command:

```
docker build --tag green-questionnaire:1.0 .
docker run --publish 8080:8080 --name questionnaire green-questionnaire:1.0
```



