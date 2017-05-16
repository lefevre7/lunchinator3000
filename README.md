# lunchinator3000

Steps to run:
1. Clone https://github.com/lefevre7/lunchinator3000
2. Run > Run > Edit Configurations
3. Press the + button
4. Add Configuration will pop up, pick Maven
5. Put the folder you cloned it in where it says "Working directory"
6. Put https://github.com/lefevre7/lunchinator3000
7. Put spring-boot:run on the "Command line" spot
8. Configure the JDK if nessessary:
  -Under Project Structure > Project SDK pick java version (1.8.0_121 is what I have)
9. Run it.

Using the App:
1. http://localhost:8080/api/create-ballot is to create a ballot
Example post (using Postman)
where raw and JSON are marked for the body and {"Content-Type":"application/json"} is marked on the headers:
{
    "endTime":"5/16/17 12:32",
    "voters":[
        {
           "name":"Bob",
           "emailAddress": "bob@gmail.com"
        },
        {
           "name":"Jim",
           "emailAddress": "jim@gmail.com"
        }
    ]

}

2. http://localhost:8080/api/ballot/f396be54-b960-46d7-b3f9-57ff92239187 in your browser or using Get in Postman
Where the {ballodId} is f396be54-b960-46d7-b3f9-57ff92239187 an obtained from the responce in step one from Postman.

A sample responce before endTime:
{
  "suggestion": {
    "id": 8,
    "name": "Firehouse Subs",
    "averageReview": 4,
    "topReviewer": "Ishmael",
    "review": "mi fringilla mi lacinia mattis. Integer eu lacus. Quisque imperdiet, erat nonummy ultricies ornare, elit elit fermentum risus, at fringilla purus mauris a nunc. In at pede. Cras vulputate velit eu sem. Pellentesque ut ipsum ac mi eleifend egestas. Sed pharetra, felis eget varius ultrices, mauris ipsum porta elit, a feugiat tellus lorem eu metus. In lorem."
  },
  "choices": [
    {
      "id": 7,
      "name": "Marco's Pizza",
      "averageReview": 4,
      "description": "Marco's Pizza, Marcos Pizza, Italian, Pizza Restaurant, Pizza Delivery, Pizza Carry-out, Online Pizza Ordering, Specialty Pizza, Order Pizza Online, Online Pizza, Pizza Specials, Pizza Deals, Hot Pizza Deals, Pizza Coupon, Pizza Menu, Fresh Baked Subs, Chicken Wings, Cheese Bread, Meatballs"
    },
    {
      "id": 8,
      "name": "Firehouse Subs",
      "averageReview": 4,
      "description": "Serving a variety of hot gourmet sub sandwiches. Made with premium meats and cheeses, steamed hot and piled high on a toasted sub roll."
    },
    {
      "id": 10,
      "name": "Popeye's",
      "averageReview": 3,
      "description": "Popeyes Louisiana Kitchen shows off its New Orleans heritage with authentic spicy & mild fried chicken, chicken tenders, seafood and signature sides."
    },
    {
      "id": 3,
      "name": "Buffalo Wild Wings",
      "averageReview": 3,
      "description": "B-Dubs® is your local sports restaurant, offering hand-spun wings, a range of cold drinks, and wall-to-wall live sports. Join the action now."
    },
    {
      "id": 9,
      "name": "Habit Burger",
      "averageReview": 3,
      "description": "Char-grilled burgers, sandwiches, salads, sides, kids meals, and milkshakes."
    }
  ]
}

A sample responce after endTime:
{
  "winner": {
    "id": 8,
    "datetime": "May 16, 2017 14:32PM",
    "name": "Firehouse Subs",
    "votes": 1
  },
  "choices": [
    {
      "id": 7,
      "name": "Marco's Pizza",
      "votes": 0
    },
    {
      "id": 3,
      "name": "Buffalo Wild Wings",
      "votes": 0
    },
    {
      "id": 9,
      "name": "Habit Burger",
      "votes": 0
    },
    {
      "id": 10,
      "name": "Popeye's",
      "votes": 0
    },
    {
      "id": 8,
      "name": "Firehouse Subs",
      "votes": 1
    }
  ]
}

3. http://localhost:8080/api/vote?id=8&ballotId=f396be54-b960-46d7-b3f9-57ff92239187&voterName=Bob&emailAddress=bob@gmail.com
To vote Example post to vote (using Postman)
This returns a 409 response if the voting has ended and 200 response if your vote counted.
