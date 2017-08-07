package Models

import Services.DatabaseService
import akka.actor.{Actor, ActorLogging, Props}

class SalaryDepositActor(database: DatabaseService) extends Actor with ActorLogging {
  override def receive: Receive = {

    case (account: Account, billers: List[Biller], salary: Int) =>

      database.updateBalance(account.accountNo, salary)

      billers.foreach {

        case biller@Biller("phone", _, _, _, _, _, _, _) =>
          val actorRef = context.actorOf(Props(classOf[BillProcessorPhoneActor], database))
          actorRef ! (database.getAccount(account.accountNo), biller)

        case biller@Biller("electricity", _, _, _, _, _, _, _) =>
          val actorRef = context.actorOf(Props(classOf[BillProcessorElectricityActor], database))
          actorRef ! (database.getAccount(account.accountNo), biller)

        case biller@Biller("internet", _, _, _, _, _, _, _) =>
          val actorRef = context.actorOf(Props(classOf[BillProcessorInternetActor], database))
          actorRef ! (database.getAccount(account.accountNo), biller)

        case biller@Biller("food", _, _, _, _, _, _, _) =>
          val actorRef = context.actorOf(Props(classOf[BillProcessorFoodActor], database))
          actorRef ! (database.getAccount(account.accountNo), biller)

        case biller@Biller("car", _, _, _, _, _, _, _) =>
          val actorRef = context.actorOf(Props(classOf[BillProcessorCarActor], database))
          actorRef ! (database.getAccount(account.accountNo), biller)
      }
      sender() ! "success"
  }

}

class BillProcessorPhoneActor(database: DatabaseService) extends Actor with ActorLogging {

  override def receive: Receive = {

    case (account: Account, biller: Biller) =>

      if (account.initialAmount >= biller.amount) {
        database.updateBalance(account.accountNo, account.initialAmount - biller.amount)
        val newBiller = biller.copy(executedIterations = biller.executedIterations + 1, totalIterations =
          biller.totalIterations + 1, paidAmount = biller.paidAmount + biller.amount, amount = 0)
        database.updateBiller(account.accountNo, newBiller)
        log.info(s"phone bill paid for account number : ${account.accountNo}")
      }
      else {
        database.updateBiller(account.accountNo, biller.copy(totalIterations = biller.totalIterations + 1))
        log.info(s"cannot pay phone bill for account no : ${account.accountNo}")
      }
  }

}

class BillProcessorElectricityActor(database: DatabaseService) extends Actor with ActorLogging {

  override def receive: Receive = {
    case (account: Account, biller: Biller) =>

      if (account.initialAmount >= biller.amount) {
        database.updateBalance(account.accountNo, account.initialAmount - biller.amount)
        val newBiller = biller.copy(executedIterations = biller.executedIterations + 1, totalIterations =
          biller.totalIterations + 1, paidAmount = biller.paidAmount + biller.amount, amount = 0)
        database.updateBiller(account.accountNo, newBiller)
        log.info(s"electricity bill paid for account number : ${account.accountNo}")
      }
      else {
        database.updateBiller(account.accountNo, biller.copy(totalIterations = biller.totalIterations + 1))
        log.info(s"cannot pay electricity bill for account no : ${account.accountNo}")
      }

  }
}

class BillProcessorInternetActor(database: DatabaseService) extends Actor with ActorLogging {

  override def receive: Receive = {
    case (account: Account, biller: Biller) =>

      if (account.initialAmount >= biller.amount) {
        database.updateBalance(account.accountNo, account.initialAmount - biller.amount)
        val newBiller = biller.copy(executedIterations = biller.executedIterations + 1, totalIterations =
          biller.totalIterations + 1, paidAmount = biller.paidAmount + biller.amount, amount = 0)
        database.updateBiller(account.accountNo, newBiller)
        log.info(s"Internet bill paid for account number : ${account.accountNo}")
      }
      else {
        database.updateBiller(account.accountNo, biller.copy(totalIterations = biller.totalIterations + 1))
        log.info(s"cannot pay internet bill for account no : ${account.accountNo}")
      }

  }
}

class BillProcessorFoodActor(database: DatabaseService) extends Actor with ActorLogging {

  override def receive: Receive = {
    case (account: Account, biller: Biller) =>

      if (account.initialAmount >= biller.amount) {
        database.updateBalance(account.accountNo, account.initialAmount - biller.amount)
        val newBiller = biller.copy(executedIterations = biller.executedIterations + 1, totalIterations =
          biller.totalIterations + 1, paidAmount = biller.paidAmount + biller.amount, amount = 0)
        database.updateBiller(account.accountNo, newBiller)
        log.info(s"Food bill paid for account number : ${account.accountNo}")
      }
      else {
        database.updateBiller(account.accountNo, biller.copy(totalIterations = biller.totalIterations + 1))
        log.info(s"cannot pay food bill for account no : ${account.accountNo}")
      }

  }
}

class BillProcessorCarActor(database: DatabaseService) extends Actor with ActorLogging {

  override def receive: Receive = {
    case (account: Account, biller: Biller) =>

      if (account.initialAmount >= biller.amount) {
        database.updateBalance(account.accountNo, account.initialAmount - biller.amount)
        val newBiller = biller.copy(executedIterations = biller.executedIterations + 1, totalIterations =
          biller.totalIterations + 1, paidAmount = biller.paidAmount + biller.amount, amount = 0)
        database.updateBiller(account.accountNo, newBiller)
        log.info(s"car bill paid for account number : ${account.accountNo}")
      }
      else {
        database.updateBiller(account.accountNo, biller.copy(totalIterations = biller.totalIterations + 1))
        log.info(s"cannot pay car bill for account no : ${account.accountNo}")
      }

  }
}

object SalaryDepositActor {
  def props(database: DatabaseService): Props = Props(classOf[SalaryDepositActor], database)
}
