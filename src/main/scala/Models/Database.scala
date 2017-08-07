package Models

import scala.collection.mutable

trait Database {

  protected val accountMap: mutable.Map[String, Account] = mutable.Map()
  protected val billerMap: mutable.Map[Int, List[Biller]] = mutable.Map()

  def addAccount(account: Account): mutable.Map[String, Account]

  def getAccounts: mutable.Map[String, Account]

  def addBiller(accNo: Int, biller: List[Biller]): mutable.Map[Int, List[Biller]]

  def getBillers: mutable.Map[Int, List[Biller]]

  def containsAccount(userName: String): Boolean

  def getAccountNo(userName: String): Int

  def updateBalance(accNo: Int, amount: Int): mutable.Map[String, Account]

  def updateBiller(accNo: Int, biller: Biller):List[Biller]

  def getAccount(accNo: Int): Account
}
