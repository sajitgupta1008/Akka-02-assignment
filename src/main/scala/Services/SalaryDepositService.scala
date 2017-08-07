package Services

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.Future

class SalaryDepositService(database: DatabaseService) {


  def depositSalary(employeeMap: Map[Int, Int], depositActorRef: ActorRef): Future[List[String]] = {

    implicit val timeout = Timeout(10 seconds)
    val result = employeeMap.map { employee =>
      val (accNo, salary) = employee
      val account = database.getAccount(accNo)
      val billerMap = database.getBillers
      (depositActorRef ? (account, billerMap(accNo), salary)).mapTo[String]
    }.toList

    Future.sequence(result)
  }

}
