package Services

import Models.{Account, Biller, Database}


import scala.collection.mutable

class DatabaseService extends Database {

  override def addAccount(account: Account): mutable.Map[String, Account] = {
    accountMap += (account.userName -> account)
    accountMap
  }

  override def addBiller(accNo: Int, billers: List[Biller]): mutable.Map[Int, List[Biller]] = {
    billerMap += (accNo -> billers)
    billerMap
  }

  override def getAccounts: mutable.Map[String, Account] = accountMap

  override def getBillers: mutable.Map[Int, List[Biller]] = billerMap

  override def containsAccount(userName: String): Boolean = accountMap.contains(userName)

  override def getAccountNo(userName: String): Int = accountMap(userName).accountNo

  override def updateBalance(accNo: Int, deposit: Int): mutable.Map[String, Account] = {

    val account = getAccount(accNo)
      accountMap.update(account.userName, account.copy(initialAmount = deposit))
    getAccounts
  }

  override def getAccount(accNo: Int): Account = getAccounts.filter(accInfo => accInfo._2.accountNo == accNo).unzip._2.head

  override def updateBiller(accNo: Int, biller: Biller): List[Biller] = {

    val listOfBillers = getBillers(accNo)
    val oldBiller = listOfBillers.find(_.category == biller.category).get
    val index = listOfBillers.indexOf(oldBiller)

    getBillers.update(accNo, listOfBillers.updated(index, biller))
    getBillers(accNo)
  }
}
