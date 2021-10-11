# Environment

The project was built with help of:
* open JDK 11 (from [amazon-corretto](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html))
* Maven (3.5.3)
* JUnit 5.7.1

# Task definition

There are 4 cashboxes in a shop. Queue to each cashbox can't exceed 20 people.
* A trainee works at the 1st cashbox, and he can serve 10 customers per hour only
* A specialist at the 2nd cashbox can serve 13 customers per hour
* A specialist at the 3rd cashbox can serve 15 customers per hour
* at 4th cashbox - 17 customers per hour

Our task is to implement a finite state machine getting input symbols as follows:
* A - one customer goes to a cashbox and the system should specify cashbox number where the user should stay
* 1 = one customer leaves a cashbox number 1
* 2 = one customer leaves a cashbox number 2
* 3 = one customer leaves a cashbox number 3
* 4 = one customer leaves a cashbox number 4 

Output: state machine should return a cashbox number where a customer should stay to minimize overall customer's time in queue
Example:
input: ААААА
ouput: 43214

# Solution
* The main method implementing the logic of state machine is `CashboxDispatcher` 
  * It gets list of cachboxes `Cashbox` as input param
    * Each `Cashbox` has 2 params: number and processing time of one customer (in seconds)
    * Cashboxes list can be retrieved with `CashboxFactory.produce()` method
      * for the testing purposes you can use as fixed cashbox's throughput as a random specified for a certain cashbox
  * `CashboxDispatcher.dispatch(long time)` is responsible for customers dispatching
    * As an input param it gets a time when a customer visit the shop
    * The most important param is customers intensity (i.e. how often customers come to the shop)
    * Customers intensity and cashbox's throughput determines where a certain customer should stay
      * As a result `UserFlowGenerator` interface was introduced - it generates arrays of time points when customers come to the shop
      * Two implementations of `UserFlowGenerator` were implemented:
        * `EvenUserFlowGenerator` - even customers distribution
        * `FixedUserFlowGenerator` - you can specify certain time points when customers come to the shop
* `CashboxDispatcherUTest` is main test class for unit testing - it verifies main test scenarios
* Run test - `mvn clean test`