# Mini Twitter

A Twitter like application where user can tweet and have followers

[Mini-Twitter Server API ](http://demoserver.kuorita.com/swagger-ui.html#!/)

[Mini-Twitter Consumer API](http://democonsumer.kuorita.com/swagger-ui.html)

## Archetecture
![](https://s3-us-west-2.amazonaws.com/donot-delete-github-image/Screen+Shot+2019-06-27+at+8.55.06+PM.png)


## Requirements
### Functional Requirement
System shall provide capability for
- user to follow other user
- user to create tweet
- user to login to the system
- user to signup at the system
- user to see most recent 100 tweets from people they follow after login

### Non-functional Requirements
- Our service needs to be highly available.
- Acceptable latency of the system is 200ms for timeline generation.
- Consistency can take a hit (in the interest of availability); if a user doesn’t see a tweet for a while, it should be fine.

## Capacity Estimation and Constraints
### Given:
- 10K total user, each person tweets 10 message a day
- new tweets/day: 100k tweet a day
### Assumption:
- each user follow 200 people, DAU: 10K
### Calculation
#### total tweet-views estimate : 2M/day 
- assume on average a user visits their timeline two times a day. On each page if a user sees 100 tweets.
- 10K DAU * (2 * 100)=>2000k =2M/day
#### storage estimate : 31 MB/day
- Assume each tweet has 140 characters and we need two bytes to store a character without compression. 
- Assume we need 30 bytes to store metadata with each tweet 
- Total storage we would need: 31 MB/day
- 100k * (280 + 30) bytes => 31000 KB/day = 31 MB/day
#### bandwidth estimate: 0.32KB/s
- 100k tweet a day * 280 bytes per tweet / 86400 second = 0.32KB/s

## Technology Choice
- Server
  - Springboot, webmvc, spring security
- Consumer (calling application)
  - Springboot rest template
- Infrastructure
  - AWS EC2, RDS, S3, loadbalancer security group, autoscale group
- Deployment
  - Maven, AWS elasticbeanstalk
- Testing
  - Mockito, Springboot test 
- UI
  - swagger


## System API
[Mini-Twitter Consumer API](http://democonsumer.kuorita.com/swagger-ui.html)

![](https://s3-us-west-2.amazonaws.com/donot-delete-github-image/Screen+Shot+2019-06-27+at+8.35.26+PM.png)

[Mini-Twitter Server API ](http://demoserver.kuorita.com/swagger-ui.html#!/)

![](https://s3-us-west-2.amazonaws.com/donot-delete-github-image/Screen+Shot+2019-06-27+at+8.58.51+PM.png)


## Scale to 10k user
![](https://s3-us-west-2.amazonaws.com/donot-delete-github-image/Screen+Shot+2019-06-27+at+10.30.29+PM.png)



