# Mini Twitter

A Twitter like application where user can tweet and have followers

## Feature
1. Sign in
2. Sign up
3. Create Tweet
4. Follow user
5. View Tweet

## Use Case

### Functional Requirements

System shall provide capability for
- user to follow other user
- user to create tweet
- user to login to the system
- user to signup at the system
- user to see most recent 100 tweets from people they follow after login
- 

### Non-functional Requirements
Our service needs to be highly available.
Acceptable latency of the system is 200ms for timeline generation.
Consistency can take a hit (in the interest of availability); if a user doesn’t see a tweet for a while, it should be fine.


## Capacity Estimation and Constraints
### Given:
- Total user:10K
- each person tweets 10 message a day
- new tweets/day: 10 tweet a day * 10K user= 100k tweet a day
### Assumption:
- each user follow 200 people
- DAU: 10K
- 
### calculation

#### total tweet-views estimate 
- Let’s assume on average a user visits their timeline two times a day. On each page if a user sees 100 tweets, then our system will generate 2M/day total tweet-views:
10K DAU *(2* 100)=>2000k =2M/day

#### storage estimate
- Let’s say each tweet has 140 characters and we need two bytes to store a character without compression. Let’s assume we need 30 bytes to store metadata with each tweet (like ID, timestamp, user ID, etc.). Total storage we would need:
100k * (280 + 30) bytes => 31000 KB/day = 31 MB/day

#### bandwith estimate
- Bandwidth Estimates Since total ingress is 24TB per day, this would translate into 0.32KB/s
- 
100k tweet a day * 280 bytes per tweeet / 86400 second = 0.32KB/s


## System API
- POST /api/auth/signin
- POST /api/auth/signup
- POST /api/tweets 
- POST /users/follow
- GET /api/tweets/{tweetId}
- GET /api/users/{username}
  - return user profile
- GET /api/users/{username}/tweets
  - return user tweets
- GET /api/users/followers
  - return user followers
- GET /api/user/me
- GET /api/user/checkUsernameAvailability
- GET /api/user/checkEmailAvailability
